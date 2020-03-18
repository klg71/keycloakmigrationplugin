import groovy.lang.GroovyObject
import org.jfrog.gradle.plugin.artifactory.dsl.PublisherConfig
import org.jfrog.gradle.plugin.artifactory.dsl.ResolverConfig

plugins {
    kotlin("jvm") version "1.3.41"
    id("com.gradle.plugin-publish") version "0.10.0"
     `maven-publish`
    id("signing")
    id("com.jfrog.artifactory") version "4.8.1"
    id("java-gradle-plugin")
}

dependencies {
    compile(kotlin("stdlib"))
    compile("de.klg71.keycloakmigration:keycloakmigration:0.1.18")
    compile(kotlin("reflect"))
    implementation(gradleApi())
    implementation(localGroovy())
}

repositories {
    mavenCentral()
    jcenter()
}

pluginBundle {
    website = "https://www.klg71.de"
    vcsUrl = "https://github.com/klg71/keycloakmigrationplugin"
    tags = listOf("keycloak", "migration")

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

val publications = project.publishing.publications.withType(MavenPublication::class.java).map {
    with(it.pom) {
        withXml {
            val root = asNode()
            root.appendNode("name", "keycloakmigrationplugin")
            root.appendNode("description", "Keycloak configuration as migration files, gradle plugin")
            root.appendNode("url", "https://github.com/klg71/keycloakmigrationplugin")
        }
        licenses {
            license {
                name.set("MIT License")
                url.set("https://github.com/klg71/keycloakmigrationplugin")
                distribution.set("repo")
            }
        }
        developers {
            developer {
                id.set("klg71")
                name.set("Lukas Meisegeier")
                email.set("MeisegeierLukas@gmx.de")
            }
        }
        scm {
            url.set("https://github.com/klg71/keycloakmigration")
            connection.set("scm:git:git://github.com/klg71/keycloakmigrationplugin.git")
            developerConnection.set("scm:git:ssh://git@github.com/klg71/keycloakmigrationplugin.git")
        }
    }
}

signing{
    sign(publishing.publications["mavenJava"])
}

artifactory {
    setContextUrl("https://artifactory.klg71.de/artifactory")
    publish(delegateClosureOf<PublisherConfig> {
        repository(delegateClosureOf<GroovyObject> {
            setProperty("repoKey", "keycloakmigration-plugin")
            setProperty("username", project.findProperty("artifactory_user"))
            setProperty("password", project.findProperty("artifactory_password"))
            setProperty("maven", true)

        })
        defaults(delegateClosureOf<GroovyObject> {
            invokeMethod("publications", "mavenJava")
        })
    })
    resolve(delegateClosureOf<ResolverConfig> {
        setProperty("repoKey", "libs-release")
    })
}
