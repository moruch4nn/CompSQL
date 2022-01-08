plugins {
    java
    kotlin("jvm") version "1.6.0"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    `maven-publish`
}

buildscript {
    repositories {
        maven { url = uri("https://plugins.gradle.org/m2/") }
    }
    dependencies {
        classpath("gradle.plugin.com.github.jengelman.gradle.plugins:shadow:7.0.0")
    }
}

group = "dev.moru3"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0-native-mt")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.0")
}

apply(plugin = "com.github.johnrengelman.shadow")

tasks.getByName("build") {
    dependsOn("shadowJar")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            this.groupId = "dev.moru3"
            this.artifactId = "compsql"
            this.version = project.version.toString()

            from(components["kotlin"])
        }
    }
}