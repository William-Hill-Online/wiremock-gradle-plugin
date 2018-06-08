package com.github.william_hill_online.wiremock.tasks

import com.github.tomakehurst.wiremock.standalone.WireMockServerRunner
import com.github.william_hill_online.wiremock.GradleWiremockPlugin
import com.github.william_hill_online.wiremock.GradleWiremockPluginExtension
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction

class WiremockStartTask extends DefaultTask {

    @TaskAction
    def runIt() {
        startWiremockFromProject(project)
    }

    def static startWiremockFromProject(final Project project) {
        def pluginExt = project[GradleWiremockPlugin.PLUGIN_EXTENSION_NAME] as GradleWiremockPluginExtension
        def params = pluginExt.getAllParams()
        println("Starting WireMock with following params: $params")
        pluginExt.wiremockInstance = new WireMockServerRunner()
        pluginExt.wiremockInstance.run(params)
    }
}
