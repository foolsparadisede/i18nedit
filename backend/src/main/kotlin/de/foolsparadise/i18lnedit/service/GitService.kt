package de.foolsparadise.i18lnedit.service

import mu.KotlinLogging
import org.eclipse.jgit.api.Git
import java.io.File

class GitService(val gitProjectRoot: String, val uri: String) {
    private val log = KotlinLogging.logger {}

    var rootDir = File(gitProjectRoot)

    fun checkIfAlreadyCloned(): Boolean {
        return rootDir.exists()
    }

    fun clone() {
        log.info { "execute git clone on $uri to $gitProjectRoot" }

        Git.cloneRepository()
            .setURI(uri)
            .setDirectory(File(gitProjectRoot))
            .call()
    }

    fun pull() {
        log.info { "execute git pull on $gitProjectRoot" }

        Git.open(File(gitProjectRoot)).pull().call()
    }

    fun commitAndPush(changesCount: Int) {
        log.info { "execute git commit and push on $gitProjectRoot" }

        val root = File(gitProjectRoot)

        Git.open(root).add().addFilepattern(".").call()

        if (Git.open(root).status().call().changed.size == 0) {
            log.warn { "git detected no changes -> abort commit" }
            return
        }

        Git.open(root).commit().setMessage("i18nedit: updated $changesCount translations").call()
        Git.open(root).push().call()
    }


}