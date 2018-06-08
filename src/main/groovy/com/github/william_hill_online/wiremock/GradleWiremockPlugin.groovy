package com.github.william_hill_online.wiremock

import com.github.william_hill_online.wiremock.tasks.WiremockStartTask
import com.github.william_hill_online.wiremock.tasks.WiremockStopTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

class GradleWiremockPlugin implements Plugin<Project> {

    public static final String PLUGIN_EXTENSION_NAME = 'wiremock'
    static final String TASK_GROUP_NAME = 'wiremock'

    @Override
    void apply(final Project project) {
        configureTaskProperties(project)

        project.task('startWiremock', group: TASK_GROUP_NAME, description: 'Start a wiremock instance', type: WiremockStartTask)
        project.task('stopWiremock', group: TASK_GROUP_NAME, description: 'Stop a wiremock instance', type: WiremockStopTask)
        project.afterEvaluate {
            configureTasksRequiringWiremock(project)
        }
    }

    private static void configureTaskProperties(Project project) {
        project.extensions.create(PLUGIN_EXTENSION_NAME, GradleWiremockPluginExtension)
        project.tasks.each { extend(it) }
        project.tasks.whenTaskAdded { extend(it) }
    }

    private static void extend(Task task) {
        task.ext.runWithWiremock = false
        task.extensions.add(PLUGIN_EXTENSION_NAME, GradleWiremockPluginExtension)
    }

    private static Iterable<Task> configureTasksRequiringWiremock(Project project) {
        project.tasks.each {
            if (it.runWithWiremock) {
                it.dependsOn 'startWiremock'
                it.finalizedBy 'stopWiremock'
            }
        }
    }
}
