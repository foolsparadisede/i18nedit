package de.foolsparadise.i18lnedit.service

import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import de.foolsparadise.i18lnedit.pluralize
import mu.KotlinLogging
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.transport.JschConfigSessionFactory
import org.eclipse.jgit.transport.OpenSshConfig
import org.eclipse.jgit.transport.SshTransport
import org.eclipse.jgit.transport.Transport
import org.eclipse.jgit.util.FS
import java.io.File

class GitService(val gitProjectRoot: String, val uri: String, val sshKeyPath: String?) {
    private val log = KotlinLogging.logger {}

    var rootDir = File(gitProjectRoot)

    fun checkIfAlreadyCloned(): Boolean {
        return rootDir.exists()
    }

    fun clone() {
        log.info { "execute git clone on $uri to $gitProjectRoot" }

        Git.cloneRepository()
            .setTransportConfigCallback { setTransport(it) }
            .setURI(uri)
            .setDirectory(File(gitProjectRoot))
            .call()
    }

    private fun setTransport(transport: Transport?) {
        (transport as SshTransport).sshSessionFactory = object : JschConfigSessionFactory() {
            override fun configure(hc: OpenSshConfig.Host?, session: Session?) {}

            override fun createDefaultJSch(fs: FS?): JSch {
                val defaultJSch = super.createDefaultJSch(fs)
                if (sshKeyPath != null)
                    defaultJSch.addIdentity(sshKeyPath)
                return defaultJSch
            }
        }
    }

    fun pull() {
        log.info { "execute git pull on $gitProjectRoot" }

        Git.open(File(gitProjectRoot)).pull()
            .setTransportConfigCallback { setTransport(it) }
            .call()
    }

    fun commitAndPush(changesCount: Int) {
        log.info { "execute git commit and push on $gitProjectRoot" }

        val root = File(gitProjectRoot)

        Git.open(root).add().addFilepattern(".").call()

        val status = Git.open(root).status().call()

        if (!status.hasUncommittedChanges()) {
            log.warn { "git detected no changes -> abort commit" }
            return
        }

        val translations = "translation".pluralize(changesCount, "translations")
        Git.open(root).commit().setMessage("i18nedit: updated $changesCount $translations").call()
        Git.open(root).push()
            .setTransportConfigCallback { setTransport(it) }
            .call()
    }


}