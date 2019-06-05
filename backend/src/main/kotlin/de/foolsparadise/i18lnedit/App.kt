package de.foolsparadise.i18lnedit

import com.google.gson.Gson
import de.foolsparadise.i18lnedit.models.Config
import de.foolsparadise.i18lnedit.service.GitService
import de.foolsparadise.i18lnedit.service.TranslationIOService
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.*
import io.ktor.response.respond
import io.ktor.response.respondFile
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
    if (configFilePath.isNullOrEmpty()) {

        var gitProjectPath: String = "/i18nedit/project"
        var sshKeyPath: String = "/i18nedit/id_rsa"
        var relativeTranslationFilePath = ""

        if (System.getenv("GIT_PROJECT_PATH") != null) {
            gitProjectPath = System.getenv("GIT_PROJECT_PATH");
        }

        if (System.getenv("SSH_KEY_PATH") != null) {
            sshKeyPath = System.getenv("SSH_KEY_PATH");
        }

        if (System.getenv("RELATIVE_TRANSLATION_FILE_PATH") != null) {
            relativeTranslationFilePath = System.getenv("RELATIVE_TRANSLATION_FILE_PATH");
        }

        var conf = Config(
            gitProjectPath = gitProjectPath,
            relativeTranslationFilePath = relativeTranslationFilePath,
            sshKeyPath = sshKeyPath,
            gitUri = System.getenv("GIT_URI"),
            gitBranch = System.getenv("GIT_BRANCH")
        )

        if (!conf.gitUri.isNullOrEmpty()) {
            return conf
        }

        throw RuntimeException("CONFIG INVALID you must define either the CONFIG_FILE or at least the GIT_URI environment-variable")
    }


    val configFile = File(configFilePath)

    if (!configFile.exists())
        throw RuntimeException("config file does not exist")

    log.info { "config file: ${configFile.absolutePath}" }

    return gson.fromJson(configFile.readText(), Config::class.java)
}

fun main(args: Array<String>) {

    val config = loadConfig()

    val gitService = GitService(
        config.gitProjectPath,
        config.gitUri,
        config.sshKeyPath,
        config.gitBranch
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
                    files(File(System.getenv("APP_HOME") + "/www"))
                    default("index.html")
                }
            }

            route("api") {
                get("/config") { controller.config(call) }
                get("/items") { controller.items(call) }
                get("/languages") { controller.languages(call) }
                post("/reimport") { controller.reimport(call) }
                post("/update") { controller.update(call) }
                get("/*") {call.respond(HttpStatusCode.NotFound)}
            }


            get("/{...}") {
                call.respondFile(File(System.getenv("APP_HOME") + "/www/index.html"))
            }
        }
    }
    server.start(wait = true)
}
