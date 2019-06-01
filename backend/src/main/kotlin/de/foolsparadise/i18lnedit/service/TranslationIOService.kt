package de.foolsparadise.i18lnedit.service

import com.google.gson.GsonBuilder
import com.google.gson.internal.LinkedTreeMap
import de.foolsparadise.i18lnedit.models.Language
import de.foolsparadise.i18lnedit.models.TranslationItem
import de.foolsparadise.i18lnedit.models.TranslationString
import mu.KotlinLogging
import java.io.File
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class TranslationIOService(
    private val gitProjectPath: String,
    private val relativeTranslationFilePath: String
) {
    private val log = KotlinLogging.logger {}

    private val gson = GsonBuilder().setPrettyPrinting().create()

    data class TranslationKVItem(
        val key: String,
        val value: String
    )

    val translationItems = mutableListOf<TranslationItem>()

    private fun importTranslation(language: String, key: String, value: String) {
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

    private fun importTranslationFile(file: File): List<TranslationKVItem> {
        val languageTranslation = gson.fromJson<HashMap<String, Any>>(file.readText(), HashMap::class.java)

        val importedItems = mutableListOf<TranslationKVItem>()

        languageTranslation.entries.forEach {
            when {
                it.value is String -> importedItems.add(TranslationKVItem(it.key, it.value.toString()))

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
                        importedItems.add(TranslationKVItem(it.key + "." + nested.key, nested.value.toString()))
                    }
                }
            }
        }

        return importedItems
    }

    fun importTranslationFiles() {
        val dir = File(gitProjectPath).resolve(relativeTranslationFilePath)
        log.info { "import translation files from ${dir.absolutePath}" }

        if (!dir.exists()) {
            log.warn { "translation dir does not exist (${dir.absolutePath}" }
            return
        }

        dir.listFiles().filter { it.name.endsWith(".json") }.forEach { file ->
            try {
                val translations = importTranslationFile(file)

                translations.forEach { item -> importTranslation(file.nameWithoutExtension, item.key, item.value) }

                log.info { "import translation ${file.nameWithoutExtension} (${translations.size} items)" }

            } catch (ex: Exception) {
                log.error(ex) { "import translation ${file.absolutePath} failed" }
            }
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

        val translationDir = File(gitProjectPath).resolve(relativeTranslationFilePath)
        if (!translationDir.exists())
            translationDir.mkdirs()

        translationFiles.entries.forEach {
            val exportFile = translationDir.resolve(it.key + ".json")
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

    fun listAvailableLanguages(): List<Language> {
        val languages = HashSet<String>()

        translationItems.forEach {
            it.translations.forEach {
                languages.add(it.language)
            }
        }

        return languages.map { Language(it, Locale.forLanguageTag(it).displayLanguage) }
    }
}