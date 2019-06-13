package de.foolsparadise.i18lnedit

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.foolsparadise.i18lnedit.models.Config
import de.foolsparadise.i18lnedit.models.TranslationItem
import de.foolsparadise.i18lnedit.service.GitService
import de.foolsparadise.i18lnedit.service.TranslationIOService
import io.ktor.application.ApplicationCall
import io.ktor.http.ContentType
import io.ktor.http.charset
import io.ktor.request.contentType
import io.ktor.request.receiveStream
import io.ktor.request.receiveText
import io.ktor.response.respondText
import kotlinx.io.charsets.Charset
import mu.KotlinLogging

class Controller(
    private val gitService: GitService,
    private val translationIOService: TranslationIOService,
    private val config: Config
) {
    private val log = KotlinLogging.logger {}

    private val gson = Gson()

    /**
     * Receive the request as String.
     * If there is no Content-Type in the HTTP header specified use ISO_8859_1 as default charset, see https://www.w3.org/International/articles/http-charset/index#charset.
     * But use UTF-8 as default charset for application/json, see https://tools.ietf.org/html/rfc4627#section-3
     */
    private suspend fun ApplicationCall.receiveTextWithCorrectEncoding(): String {
        fun ContentType.defaultCharset(): Charset = when (this) {
            ContentType.Application.Json -> Charsets.UTF_8
            else -> Charsets.ISO_8859_1
        }

        val contentType = request.contentType()
        val suitableCharset = contentType.charset() ?: contentType.defaultCharset()
        return receiveStream().bufferedReader(charset = suitableCharset).readText()
    }

    fun initGit() {
        if (!gitService.checkIfAlreadyCloned()) {
            log.info { "clone repo from ${config.gitUri} to ${config.gitProjectPath}" }
            gitService.clone()
        } else {
            log.info { "repo already cloned (${config.gitProjectPath}). pull instead" }
            gitService.pull()
        }

        translationIOService.importTranslationFiles()
    }

    suspend fun config(call: ApplicationCall) {
        call.respondText(
            gson.toJson(config),
            ContentType.Application.Json
        )
    }

    suspend fun items(call: ApplicationCall) {
        call.respondText(
            gson.toJson(translationIOService.translationItems),
            ContentType.Application.Json
        )
    }

    suspend fun reimport(call: ApplicationCall) {
        gitService.pull()
        translationIOService.translationItems.clear()
        translationIOService.importTranslationFiles()

        call.respondText(
            gson.toJson(translationIOService.translationItems),
            ContentType.Application.Json
        )
    }

    suspend fun update(call: ApplicationCall) {
        val listType = object : TypeToken<List<TranslationItem>>() {}.type
        val changes: List<TranslationItem> = gson.fromJson(call.receiveTextWithCorrectEncoding(), listType)

        if (changes.isNotEmpty()) {
            translationIOService.updateTranslations(changes)
            translationIOService.exportTranslations()
            gitService.commitAndPush(changes.size)
        }

        call.respondText(
            gson.toJson(translationIOService.translationItems),
            ContentType.Application.Json
        )
    }

    suspend fun languages(call: ApplicationCall) {
        call.respondText(
            gson.toJson(translationIOService.listAvailableLanguages()),
            ContentType.Application.Json
        )
    }
}