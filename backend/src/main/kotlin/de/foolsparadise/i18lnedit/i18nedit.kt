package de.foolsparadise.i18lnedit

import com.google.gson.Gson
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.request.receiveText
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import mu.KotlinLogging
import java.io.File

private val log = KotlinLogging.logger {}

private val gson = Gson()

data class Config(
    val gitProjectPath: String,
    val gitUri: String
)

fun main() {

    val configFilePath = System.getenv("CONFIG_FILE")
    log.info { "config file: $configFilePath" }

    val config: Config = gson.fromJson(File(configFilePath).readText(), Config::class.java)

    val gitService = GitService(config.gitProjectPath, config.gitUri)
    val translationItemsService = TranslationItemsService(config.gitProjectPath)

    if (!gitService.checkIfAlreadyCloned()) {
        log.info { "clone repo from ${config.gitUri} to ${config.gitProjectPath}" }
        gitService.clone()
    } else {
        log.info { "repo already cloned (${config.gitProjectPath}). do a pull instead" }
        gitService.pull()
    }

    translationItemsService.parseTranslations()

    val server = embeddedServer(Netty, port = 8080) {
        routing {

            get("/config") {
                call.respondText(gson.toJson(config), ContentType.Application.Json)
            }

            get("/items") {
                call.respondText(gson.toJson(translationItemsService.translationItems), ContentType.Application.Json)
            }

            post("/update") {
                val changes =
                    gson.fromJson<List<TranslationItemsService.TranslationItem>>(call.receiveText(), List::class.java)

                translationItemsService.updateTranslations(changes)
                translationItemsService.exportTranslations()

                gitService.commitAndPush()

                call.respondText(gson.toJson(translationItemsService.translationItems), ContentType.Application.Json)
            }
        }
    }
    server.start(wait = true)
}
