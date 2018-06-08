package com.github.william_hill_online.wiremock.tasks

import com.github.william_hill_online.wiremock.GradleWiremockPlugin
import com.github.william_hill_online.wiremock.GradleWiremockPluginExtension
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction

class WiremockStopTask extends DefaultTask {

    @TaskAction
    def runIt() {
        stopWiremock(project)
    }

    static def stopWiremock(final Project project) {
        def pluginExt = project[GradleWiremockPlugin.PLUGIN_EXTENSION_NAME] as GradleWiremockPluginExtension
        def wiremockInstance = pluginExt.wiremockInstance
        if (wiremockInstance && wiremockInstance.isRunning()) {
            println 'stopping Wiremock'
            wiremockInstance.stop()
            println "Wiremock stopped"
        }
        println 'Wiremock not running'
    }
}
