package com.github.william_hill_online.wiremock

import com.github.tomakehurst.wiremock.standalone.WireMockServerRunner
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

class GradleWiremockPlugin implements Plugin<Project> {

    static final String PLUGIN_EXTENSION_NAME = 'wiremock'
    static private WireMockServerRunner serverRunner

    @Override
    void apply(final Project project) {
        configureTaskProperties(project)

        project.afterEvaluate {
            configureTasksRequiringWiremock(project)
        }
    }

    private static void configureTaskProperties(Project project) {
        project.extensions.create(PLUGIN_EXTENSION_NAME, GradleWiremockPluginExtension)
        project.tasks.each { extend(it) }
        project.tasks.whenTaskAdded { extend(it) }
    }

    def static startWiremockFromProject(final Project project) {
        def pluginExt = project[PLUGIN_EXTENSION_NAME] as GradleWiremockPluginExtension
        def params = pluginExt.getAllParams()
        println("Starting WireMock with following params: $params")
        serverRunner = new WireMockServerRunner()
        serverRunner.main(params)
    }

    private static void extend(Task task) {
        task.ext.runWithWiremock = false
        task.extensions.add(PLUGIN_EXTENSION_NAME, GradleWiremockPluginExtension)
    }

    private static Iterable<Task> configureTasksRequiringWiremock(Project project) {
        project.tasks.each {
            if (it.runWithWiremock) {
                it.doFirst {
                    startWiremockFromProject(project)
                }
                it.doLast {
                    stopWiremock()
                }
            }
        }
    }

    static def stopWiremock() {
        serverRunner.stop()
    }
}
