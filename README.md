## Gradle Wiremock Plugin ##

Ultra-lightweight, super-simple WireMock Maven Plugin. It is based on the [WireMock](http://wiremock.org/docs/running-standalone/).

Plugin is available on the [Gradle Plugin Portal](https://plugins.gradle.org/plugin/com.williamhill.wiremock).


Gradle will copy your resources from src/main/resources/ (or the specified one) to target/classes/. WireMock Gradle Plugin will start WireMock on localhost:8081 before the desired task run and use your mock definitions. When task process execution finishes, WireMock will be stopped as well.

### Usage ###

Enable the plugin in your gradle build:

```groovy
plugins {
  id 'com.williamhill.wiremock' version '0.1'
}
```

You can now declare a dependency on a running WireMock instance from any of your tasks:

```groovy
task integrationTest(type: Test) {
    runWithWiremock = true
}
```

Add your mock definitions to the following folders:
```
src/main/resources/mappings/
src/main/resources/__files/
```

### Configuration ###

Configure your WireMock instance inside a ```wiremock``` block:

```
wiremock {
    dir "src/test/resources/mappings/"
    params "--port=8081"
}
```

The `wireMock` configuration block can be declared at either the project or the task level. Task-level configuration inherits from any project-level configuration provided.

See WireMock [manual](http://wiremock.org/docs/running-standalone/) for detailed information on available command line options.