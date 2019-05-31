package de.foolsparadise.i18lnedit

import com.google.gson.Gson
import de.foolsparadise.i18lnedit.models.Config
import de.foolsparadise.i18lnedit.service.GitService
import de.foolsparadise.i18lnedit.service.TranslationItemsService
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import mu.KotlinLogging
import java.io.File

private val log = KotlinLogging.logger {}

private val gson = Gson()

fun main() {

    val configFilePath = System.getenv("CONFIG_FILE")
    log.info { "config file: $configFilePath" }

    val config: Config = gson.fromJson(File(configFilePath).readText(), Config::class.java)

    val gitService = GitService(config.gitProjectPath, config.gitUri)
    val translationItemsService = TranslationItemsService(config.gitProjectPath)

    val controller = Controller(gitService, translationItemsService, config)
    controller.initGit();

    val server = embeddedServer(Netty, port = 8080) {
        install(CORS) {
            anyHost()
        }

        routing {
            get("/config") { controller.config(call) }
            get("/items") { controller.items(call) }
            post("/reimport") { controller.reimport(call) }
            post("/update") { controller.update(call) }
        }
    }
    server.start(wait = true)
}
