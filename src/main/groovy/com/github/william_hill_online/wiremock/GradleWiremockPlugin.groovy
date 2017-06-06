package com.github.william_hill_online.wiremock

import com.github.tomakehurst.wiremock.standalone.WireMockServerRunner
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

class GradleWiremockPlugin implements Plugin<Project> {

    static final String PLUGIN_EXTENSION_NAME = 'wiremock'
    static final String TASK_GROUP_NAME = 'Wiremock'

    @Override
    void apply(final Project project) {
        configureTaskProperties(project)
        addStartWiremockTask(project)
        addStopWiremockTask(project)

        extendAllTasksWithCassandraOptions(project)

        project.afterEvaluate {
            configureTasksRequiringCassandra(project)
        }
    }

    private static void configureTaskProperties(Project project) {
        project.extensions.create(PLUGIN_EXTENSION_NAME, GradleWiremockPluginExtension)
    }

    private static void addStartWiremockTask(Project project) {
        project.task(group: TASK_GROUP_NAME, description: 'Start a Wiremock instance', 'startWiremock').doFirst {
            startWiremockFromProject(project)
        }
    }

    private static void addStopWiremockTask(Project project) {
        project.task(group: TASK_GROUP_NAME, description: 'Stop a Wiremock instance', 'stopWiremock').doFirst {
//            stopWiremock()
        }
    }

    def static startWiremockFromProject(final Project project) {
        def pluginExt = project[PLUGIN_EXTENSION_NAME] as GradleWiremockPluginExtension
        def params = pluginExt.getAllParams()
        String wireMockParams = Arrays.toString(params).replaceAll("[\\[\\]]", "").replaceAll(", ", " ")
        println("Starting WireMock with following params: $wireMockParams")
        WireMockServerRunner.main(params)
    }

    private static void extendAllTasksWithCassandraOptions(Project project) {
        project.tasks.each {
            extend(it)
        }

        project.tasks.whenTaskAdded {
            extend(it)
        }
    }

    private static void extend(Task task) {
        task.ext.runWithWiremock = false
        task.extensions.add(PLUGIN_EXTENSION_NAME, GradleWiremockPluginExtension)
    }

    private static Iterable<Task> configureTasksRequiringCassandra(Project project) {
        project.tasks.each {
            def task = it
            if (task.runWithWiremock) {
                task.doFirst {
                    startWiremockFromProject(project)
                }
            }
        }
    }
}
