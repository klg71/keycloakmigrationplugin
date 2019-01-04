import groovy.lang.GroovyObject
import org.jfrog.gradle.plugin.artifactory.dsl.PublisherConfig
import org.jfrog.gradle.plugin.artifactory.dsl.ResolverConfig

plugins {
    kotlin("jvm") version "1.3.0"
    id("com.gradle.plugin-publish") version "0.10.0"
     `maven-publish`
    id("com.jfrog.artifactory") version "4.8.1"
}

dependencies {
    compile(kotlin("stdlib"))
    compile("de.klg71:keycloakmigration:0.0.2-SNAPSHOT")
    compile(kotlin("reflect"))
    implementation(gradleApi())
    implementation(localGroovy())
}

repositories {
    jcenter()
    maven {
        setUrl("https://artifactory.klg71.de/artifactory/libs-releases")
        credentials {
            username = project.findProperty("artifactory_user") as String
            password = project.findProperty("artifactory_password") as String
        }
    }
    maven {
        setUrl("https://artifactory.klg71.de/artifactory/keycloakmigration")
        credentials {
            username = project.findProperty("artifactory_user") as String
            password = project.findProperty("artifactory_password") as String
        }
    }
}

pluginBundle {
    website = "https://www.klg71.de"
    vcsUrl = "https://github.com/klg71/keycloakmigrationplugin"
    tags = listOf("keycloak", "migration")
    version = "0.0.5"

    plugins {
        create("keycloakmigrationplugin") {
            id = "de.klg71.keycloakmigrationplugin"
            displayName = "keycloakmigration"
            description = "Plugin to provide liquibase like migrations for keycloak"
        }
    }
}


publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
        }
    }
}

artifactory {
    setContextUrl("https://artifactory.klg71.de/artifactory")
    publish(delegateClosureOf<PublisherConfig> {
        repository(delegateClosureOf<GroovyObject> {
            setProperty("repoKey", "keycloakmigration-plugin")
            setProperty("username", project.findProperty("artifactory_user"))
            setProperty("password", project.findProperty("artifactory_password"))
            setProperty("maven", false)

        })
        defaults(delegateClosureOf<GroovyObject> {
            invokeMethod("publications", "mavenJava")
        })
    })
    resolve(delegateClosureOf<ResolverConfig> {
        setProperty("repoKey", "libs-release")
    })
}
