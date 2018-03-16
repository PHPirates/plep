// Gradle file from https://github.com/PHPirates/kotlin-template-project

import org.gradle.api.plugins.ExtensionAware

import org.junit.platform.gradle.plugin.FiltersExtension
import org.junit.platform.gradle.plugin.EnginesExtension
import org.junit.platform.gradle.plugin.JUnitPlatformExtension

group = "deltadak"
version = "0.1"

// Latest version as of 2018-03-13: JUnit 5.1.0 = Platform 1.1.0 + Jupiter 5.1.0 + Vintage 5.1.0

// JUnit 5
buildscript {
    repositories {
        mavenCentral()
        jcenter()
        maven { url = uri("https://dl.bintray.com/jetbrains/spek") }
    }
    dependencies {
        classpath("org.junit.platform:junit-platform-gradle-plugin:1.1.0")
    }
}

apply {
    plugin("org.junit.platform.gradle.plugin")
}

// Kotlin configuration.
plugins {
    application
    kotlin("jvm") version "1.2.30"
    java // Required by at least JUnit.
}

application {
    mainClassName = "nl.deltadak.plep.Main"
}

dependencies {
    // Plep dependencies
    // JDBC driver for database
    // https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc
    // 'compile' is deprecated, now it is 'api' but that fails to build with
//    Could not find method api() for arguments [org.xerial:sqlite-jdbc:3.18.0] on object of type org.gradle.api.internal.artifacts.dsl.dependencies.DefaultDependencyHandler.
    compile("org.xerial:sqlite-jdbc:3.18.0")

    // Kotlin
    compile(kotlin("stdlib"))
    // To "prevent strange errors".
    compile(kotlin("reflect"))
    // Kotlin reflection.
    compile(kotlin("test"))
    compile(kotlin("test-junit"))

    // Built-in Kotlin test framework.
    testCompile("io.kotlintest:kotlintest:2.0.7")

    // JUnit 5
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.1.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.1.0")
    testRuntime("org.junit.platform:junit-platform-console:1.1.0")

    // Kotlintests are not run anyway when using JUnit 5 as well.
    testCompile("io.kotlintest:kotlintest:2.0.7")

    // JavaFX tests using TestFX
    testCompile("org.testfx:testfx-core:4.0.12-alpha")
    testCompile("org.testfx:testfx-junit:4.0.12-alpha")
    // Only needed for headless testing.
//    testCompile("org.testfx:openjfx-monocle:8u76-b04") // jdk-9+181 for Java 9

    // Spek
    testCompile("org.jetbrains.spek:spek-api:1.1.5")
    testRuntime("org.jetbrains.spek:spek-junit-platform-engine:1.1.5")
}

repositories {
    jcenter()
}