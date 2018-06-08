package com.github.william_hill_online.wiremock

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Ignore
import spock.lang.Specification

class WiremockPluginTasksSpec extends Specification {

    @Rule final TemporaryFolder testProjectDir = new TemporaryFolder()
    File buildFile

    def setup() throws IOException {
        buildFile = testProjectDir.newFile("build.gradle")
    }

    def 'starting a task which is set to runWithWiremock should start a wiremock instance'() {
        given:
        buildFile << """
                    plugins {
                        id 'com.github.william-hill-online.wiremock'
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
                .withPluginClasspath()
                .build()

        then:
        result.getOutput().contains("Starting WireMock with following params: [--root-dir=test/mappings, --port=9090]")
        result.task(":integrationTests").getOutcome() == TaskOutcome.SUCCESS
    }

    @Ignore
    def 'startWiremock task should start a wiremock instance'() {
        given:
        buildFile << """
                    plugins {
                        id 'com.github.william-hill-online.wiremock'
                    }
                    
                    wiremock {
                        dir "test/mappings"
                        params "--port=9090"
                    }
                    """

        when:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments("startWiremock")
                .withPluginClasspath()
                .build()

        then:
        result.getOutput().contains("Starting WireMock with following params: [--root-dir=test/mappings, --port=9090]")
        result.task(":startWiremock").getOutcome() == TaskOutcome.SUCCESS
    }

    def 'stopWiremock task should stop a running wiremock instance'() {
        given:
        buildFile << """
                    plugins {
                        id 'com.github.william-hill-online.wiremock'
                    }
                    
                    wiremock {
                        dir "test/mappings"
                        params "--port=9090"
                    }
                    """

        when:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withDebug(true)
                .withArguments("startWiremock", "stopWiremock")
                .withPluginClasspath()
                .build()

        then:
        result.getOutput().contains("Starting WireMock with following params: [--root-dir=test/mappings, --port=9090]")
        result.getOutput().contains("Wiremock stopped")
        result.task(":startWiremock").getOutcome() == TaskOutcome.SUCCESS
        result.task(":stopWiremock").getOutcome() == TaskOutcome.SUCCESS
    }
}
