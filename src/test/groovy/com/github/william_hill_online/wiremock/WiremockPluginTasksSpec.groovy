package com.github.william_hill_online.wiremock

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class WiremockPluginTasksSpec extends Specification {

    @Rule final TemporaryFolder testProjectDir = new TemporaryFolder()
    File buildFile
    List pluginClasspath

    def setup() throws IOException {
        buildFile = testProjectDir.newFile("build.gradle")
        def resource = getClass().classLoader.getResource('plugin-classpath.txt')
        pluginClasspath = resource.readLines().collect { new File(it) }
    }

    def 'startWiremock task should start a wiremock instance'() {
        given:
        buildFile << """
                    plugins {
                        id 'com.williamhill.wiremock'
                    }
                    
                    wiremock {
                        dir "test/mappings"
                        params "--port=9090"
                    }
                    
                    task integrationTests() {
                        runWithWiremock = true
                    }
                    """

        when:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments("integrationTests")
                .withPluginClasspath(pluginClasspath)
                .build()

        then:
        result.getOutput().contains("Starting WireMock with following params: [--root-dir=test/mappings, --port=9090]")
        result.task(":integrationTests").getOutcome() == TaskOutcome.SUCCESS
    }
}
