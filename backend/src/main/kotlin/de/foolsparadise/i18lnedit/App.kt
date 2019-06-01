package de.foolsparadise.i18lnedit

import com.google.gson.Gson
import de.foolsparadise.i18lnedit.models.Config
import de.foolsparadise.i18lnedit.service.GitService
import de.foolsparadise.i18lnedit.service.TranslationIOService
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.http.content.file
import io.ktor.http.content.static
import io.ktor.http.content.staticRootFolder
import io.ktor.response.respondRedirect
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import mu.KotlinLogging
import java.io.File

private val log = KotlinLogging.logger {}

private val gson = Gson()

fun String.pluralize(count: Int, plural: String?): String? {
    return if (count > 1) {
        plural ?: this + 's'
    } else {
        this
    }
}

fun loadConfig(): Config {
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

    val gitService = GitService(
        config.gitProjectPath,
        config.gitUri,
        config.sshKeyPath
    )

    val translationIOService = TranslationIOService(
        config.gitProjectPath,
        config.relativeTranslationFilePath
    )

    val controller = Controller(
        gitService,
        translationIOService,
        config
    )

    controller.initGit();

    val server = embeddedServer(Netty, port = 8080) {
        install(CORS) {
            anyHost()
        }

        routing {
            if (System.getenv("APP_HOME") != null) {
                static("/") {
                    staticRootFolder = File(System.getenv("APP_HOME") + "/www")
                    file("index.html")
                }
            }

            route("api") {
                get("/config") { controller.config(call) }
                get("/items") { controller.items(call) }
                get("/languages") { controller.languages(call) }
                post("/reimport") { controller.reimport(call) }
                post("/update") { controller.update(call) }
            }

            get("/{...}") {
                call.respondRedirect("/index.html")
            }
        }
    }
    server.start(wait = true)
}
