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
import java.lang.RuntimeException

private val log = KotlinLogging.logger {}

private val gson = Gson()

fun String.pluralize(count: Int, plural: String?): String? {
    return if (count > 1) {
        plural ?: this + 's'
    } else {
        this
    }
}

fun loadConfig() : Config {
    val configFilePath = System.getenv("CONFIG_FILE")
    if (configFilePath.isNullOrEmpty())
        throw RuntimeException("CONFIG_FILE environment-variable missing or empty")

    val configFile = File(configFilePath)

    if (!configFile.exists())
        throw RuntimeException("config file does not exist")

    log.info { "config file: ${configFile.absolutePath}" }

    return gson.fromJson(configFile.readText(), Config::class.java)
}

fun main() {

    val config = loadConfig()

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
