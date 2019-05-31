package de.foolsparadise.i18lnedit

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.foolsparadise.i18lnedit.models.Config
import de.foolsparadise.i18lnedit.models.TranslationItem
import de.foolsparadise.i18lnedit.service.GitService
import de.foolsparadise.i18lnedit.service.TranslationItemsService
import io.ktor.application.ApplicationCall
import io.ktor.http.ContentType
import io.ktor.request.receiveText
import io.ktor.response.respondText
import mu.KotlinLogging

class Controller(
    private val gitService: GitService,
    private val translationItemsService: TranslationItemsService,
    private val config: Config
) {
    private val log = KotlinLogging.logger {}

    private val gson = Gson()

    fun initGit() {
        if (!gitService.checkIfAlreadyCloned()) {
            log.info { "clone repo from ${config.gitUri} to ${config.gitProjectPath}" }
            gitService.clone()
        } else {
            log.info { "repo already cloned (${config.gitProjectPath}). do a pull instead" }
            gitService.pull()
        }

        translationItemsService.importTranslationFiles()
    }

    suspend fun config(call: ApplicationCall) {
        call.respondText(gson.toJson(config), ContentType.Application.Json)
    }

    suspend fun items(call: ApplicationCall) {
        call.respondText(gson.toJson(translationItemsService.translationItems), ContentType.Application.Json)
    }

    suspend fun reimport(call: ApplicationCall) {
        gitService.pull()
        translationItemsService.translationItems.clear()
        translationItemsService.importTranslationFiles()
    }

    suspend fun update(call: ApplicationCall) {
        val listType = object : TypeToken<List<TranslationItem>>() {}.type
        val changes: List<TranslationItem> = gson.fromJson(call.receiveText(), listType)

        if (changes.isNotEmpty()) {
            translationItemsService.updateTranslations(changes)
            translationItemsService.exportTranslations()
            gitService.commitAndPush(changes.size)
        }

        call.respondText(
            gson.toJson(translationItemsService.translationItems),
            ContentType.Application.Json
        )
    }
}