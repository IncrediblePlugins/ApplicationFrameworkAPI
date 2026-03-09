plugins {
    `java-library`
    `maven-publish`
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.realityforge.org.jetbrains.annotations:org.jetbrains.annotations:1.7.0")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    withSourcesJar()
    withJavadocJar()
}

group = "com.github.angeschossen"
version = "1.0.2"
description = "ApplicationFrameworkAPI"
java.sourceCompatibility = JavaVersion.VERSION_1_8

tasks {
    jar {
        archiveFileName.set("ApplicationFrameworkAPI.jar")
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = project.description
            version = project.version.toString()

            from(components["java"])
        }
    }
}