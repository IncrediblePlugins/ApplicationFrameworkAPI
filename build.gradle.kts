import java.net.HttpURLConnection
import java.net.URI
import java.util.Base64

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

tasks {
    jar {
        archiveFileName.set("ApplicationFrameworkAPI.jar")
    }

    withType<PublishToMavenRepository>().configureEach {
        doLast {
            val repoName = repository.url.toString().trimEnd('/').substringAfterLast('/')
            println("Published ${publication.groupId}:${publication.artifactId}:${publication.version}")
            println("Browse at: https://nexus.incredibleplugins.com/#browse/browse:$repoName")
        }
    }
}

tasks.register("publishJavadocToNexus") {
    dependsOn("javadocJar")
    description = "Extracts the javadoc jar and uploads its contents to the Nexus raw repository."
    group = "publishing"

    doLast {
        val javadocJar = tasks.named<Jar>("javadocJar").get().archiveFile.get().asFile
        val extractDir = layout.buildDirectory.dir("javadoc-site").get().asFile

        // Extract javadoc jar into build/javadoc-site
        if (extractDir.exists()) extractDir.deleteRecursively()
        copy {
            from(zipTree(javadocJar))
            into(extractDir)
        }

        val username = (project.findProperty("nexusUsername") as String?
            ?: System.getenv("NEXUS_USERNAME")
            ?: error("nexusUsername not set"))
        val password = (project.findProperty("nexusPassword") as String?
            ?: System.getenv("NEXUS_PASSWORD")
            ?: error("nexusPassword not set"))
        val credentials = Base64.getEncoder()
            .encodeToString("$username:$password".toByteArray())

        val baseUrl = "https://nexus.incredibleplugins.com/repository/plugin-javadoc-public" +
            "/${project.group.toString().replace('.', '/')}" +
            "/${project.description}/${project.version}"

        val latestUrl = "https://nexus.incredibleplugins.com/repository/plugin-javadoc-public" +
            "/${project.group.toString().replace('.', '/')}" +
            "/${project.description}/latest"

        extractDir.walkTopDown().filter { it.isFile }.forEach { file ->
            val relativePath = file.relativeTo(extractDir).path.replace('\\', '/')
            for (uploadUrl in listOf(baseUrl, latestUrl)) {
                val connection = URI("$uploadUrl/$relativePath").toURL()
                    .openConnection() as HttpURLConnection
                connection.requestMethod = "PUT"
                connection.doOutput = true
                connection.setRequestProperty("Authorization", "Basic $credentials")
                connection.setRequestProperty("Content-Type", "application/octet-stream")
                file.inputStream().use { it.copyTo(connection.outputStream) }
                val code = connection.responseCode
                if (code !in 200..299) {
                    throw GradleException("Failed to upload $relativePath to $uploadUrl — HTTP $code: ${connection.responseMessage}")
                }
            }
            println("Uploaded: $relativePath")
        }

        println("Javadoc published to $baseUrl/index.html")
        println("Latest javadoc at $latestUrl/index.html")
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

    repositories {
        maven {
            name = "nexus"
            url = uri(
                "https://nexus.incredibleplugins.com/repository/plugin-maven-public/"
            )
            credentials {
                username = project.findProperty("nexusUsername") as String? ?: System.getenv("NEXUS_USERNAME")
                password = project.findProperty("nexusPassword") as String? ?: System.getenv("NEXUS_PASSWORD")
            }
        }
    }
}