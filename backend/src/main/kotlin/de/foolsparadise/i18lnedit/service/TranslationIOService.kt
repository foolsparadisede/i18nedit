package de.foolsparadise.i18lnedit.service

import com.google.gson.GsonBuilder
import com.google.gson.internal.LinkedTreeMap
import de.foolsparadise.i18lnedit.models.TranslationItem
import de.foolsparadise.i18lnedit.models.TranslationString
import mu.KotlinLogging
import java.io.File
import java.util.*
import kotlin.collections.HashMap

class TranslationIOService(
    private val gitProjectPath: String,
    private val relativeTranslationFilePath: String
) {
    private val log = KotlinLogging.logger {}

    private val gson = GsonBuilder().setPrettyPrinting().create()

    val translationItems = mutableListOf<TranslationItem>()

    private fun addTranslationItem(language: String, key: String, value: String) {
        val translation = TranslationString(
            language = language,
            string = value
        )

        val tKey = translationItems.find { it.key == key }

        if (tKey != null) {
            tKey.translations.add(translation)
        } else {
            translationItems.add(TranslationItem(key, mutableListOf(translation), UUID.randomUUID().toString()))
        }
    }

    private fun importTranslationFile(file: File) {
        val languageTranslation = gson.fromJson<HashMap<String, Any>>(file.readText(), HashMap::class.java)

        languageTranslation.entries.forEach {
            when {
                it.value is String -> addTranslationItem(file.nameWithoutExtension, it.key, it.value.toString())

                // flattening nested keys
                //
                // handles cases like this:
                // {
                //    "button.save.label": "save",
                //    "projects: {   <----- THIS IS SHIT
                //       "button.save.label": "save"
                //       "button.cancel.label": "cancel"
                //     }
                // }
                //
                else -> {
                    (it.value as LinkedTreeMap<*, *>).entries.forEach { nested ->

                        addTranslationItem(
                            file.nameWithoutExtension,
                            it.key + "." + nested.key,
                            nested.value.toString()
                        )
                    }
                }
            }
        }
    }

    fun importTranslationFiles() {
        log.info { "import translation files from $gitProjectPath" }

        val dir = File(gitProjectPath).resolve(relativeTranslationFilePath)
        if (!dir.exists()) {
            log.warn { "translation dir does not exist (${dir.absolutePath}" }
            return
        }

        dir.listFiles().filter { it.name.endsWith(".json") }.forEach { file ->
            log.info { "import translation file ${file.name}" }
            importTranslationFile(file)
        }
    }

    fun exportTranslations() {
        //                             language        key     value
        val translationFiles = HashMap<String, HashMap<String, String>>()

        // group items by language
        translationItems.forEach { item ->
            item.translations.forEach { string ->
                val file = translationFiles[string.language]
                if (file != null) {
                    file.put(item.key, string.string)
                } else {
                    translationFiles.put(string.language, hashMapOf(Pair(item.key, string.string)))
                }
            }
        }

        log.info { "export ${translationFiles.size} translations" }

        translationFiles.entries.forEach {
            val exportFile = File(gitProjectPath).resolve(relativeTranslationFilePath).resolve(it.key + ".json")
            log.info { "export translation - write file ${exportFile.absolutePath}" }
            exportFile.writeText(gson.toJson(it.value))
        }
    }

    fun updateTranslations(changes: List<TranslationItem>) {
        log.info { "update translations (${changes.size} changes)" }

        changes.forEach { change ->

            if (change.id.isNullOrEmpty()) {
                log.info { "new item with key ${change.key}" }
                change.id = UUID.randomUUID().toString()
                translationItems.add(change)
                return@forEach
            }

            val item = translationItems.find { change.id == it.id }

            if (change.deleted != null && change.deleted) {
                log.info { "item with id ${change.id} deleted" }
                translationItems.remove(item)
                return@forEach
            }

            log.info { "item with id ${change.id} replaced" }

            translationItems.remove(item)
            translationItems.add(change)
        }
    }
}