package de.foolsparadise.i18lnedit

import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import mu.KotlinLogging
import java.io.File
import java.util.*
import kotlin.collections.HashMap

class TranslationItemsService(val gitProjectPath: String) {
    private val log = KotlinLogging.logger {}

    data class TranslationItem(
        val key: String,
        val translations: MutableList<TranslationString>,
        var id: String?,
        val deleted: Boolean? = false
    )

    data class TranslationString(
        val language: String,
        val string: String
    )

    private val gson = Gson()

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

    fun parseTranslations() {
        val dir = File(gitProjectPath)

        dir.listFiles().filter { it.name.endsWith(".json") }.forEach { file ->

            log.info { "parse translation file ${file.name}" }

            val languageTranslation = gson.fromJson<HashMap<String, Any>>(file.readText(), HashMap::class.java)

            languageTranslation.entries.forEach {
                when {
                    it.value is String -> addTranslationItem(file.nameWithoutExtension, it.key, it.value.toString())

                    // special nested case
                    it.value !is String -> {
                        (it.value as LinkedTreeMap<String, String>).entries.forEach { nested ->

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
    }

    fun exportTranslations() {
        val translationFiles = HashMap<String, HashMap<String, String>>()

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

        translationFiles.entries.forEach {
            val exportFile = File(gitProjectPath).resolve(it.key + ".json")
            log.info { "export translation - write file ${exportFile.absolutePath}" }
            exportFile.writeText(gson.toJson(it.value))
        }
    }

    fun updateTranslations(changes: List<TranslationItem>) {
        log.info { "update translations (${changes.size} changes)" }

        changes.forEach { change ->

            if (change.id.isNullOrEmpty()) {
                log.info { "new item" }
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
            translationItems.remove(item);
            translationItems.add(change)
        }
    }
}