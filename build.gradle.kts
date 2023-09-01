plugins {
    `java-library`
    `maven-publish`
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }

    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

val ftpAntTask by configurations.creating

dependencies {
    compileOnly("net.punchtree:punchtree-util:0.1.0-SNAPSHOT")
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")

    ftpAntTask("org.apache.ant:ant-commons-net:1.10.12") {
        module("commons-net:commons-net:1.4.1") {
            dependencies("oro:oro:2.0.8:jar")
        }
    }
}

group = "net.punchtree"
version = "0.0.1-SNAPSHOT"
description = "Minigames++2"
java.sourceCompatibility = JavaVersion.VERSION_17

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}

val ftpHostUrl: String by project
val ftpUsername: String by project
val ftpPassword: String by project
val localOutputDir: String? by project

task("uploadToServer") {
    doLast{
        ant.withGroovyBuilder {
            "taskdef"("name" to "ftp", "classname" to "org.apache.tools.ant.taskdefs.optional.net.FTP", "classpath" to ftpAntTask.asPath)
            "ftp"("server" to ftpHostUrl, "userid" to ftpUsername, "password" to ftpPassword, "remoteDir" to "/default/plugins") {
                "fileset"("dir" to "build/libs") {
                    "include"("name" to rootProject.name + "-" + version + ".jar")
                }
            }
        }
    }
}

task("buildAndPublish") {
    dependsOn("build")
    dependsOn("uploadToServer")
    tasks.findByName("uploadToServer")!!.mustRunAfter("build")
}

val buildLocal by tasks.registering(Copy::class) {
    group = "build"
    description = "Builds the shaded JAR locally without publishing to the live server."

    from("build/libs/${project.name}-${project.version}.jar")
    into(provider {
        if (localOutputDir != null) {
            localOutputDir?.let { project.file(it) }
        } else {
            logger.warn("Environment variable LOCAL_OUTPUT_DIR is not set. Using the default output directory.")
            project.file("build/libs")
        }
    })
    dependsOn("build")
}