package de.foolsparadise.i18lnedit

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

        Git.open(File(gitProjectRoot)).add().addFilepattern(".").call()
        Git.open(File(gitProjectRoot)).commit().setMessage("updated $changesCount translations").call()
        Git.open(File(gitProjectRoot)).push().call()
    }


}