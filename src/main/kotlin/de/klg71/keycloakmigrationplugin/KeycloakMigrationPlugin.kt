package de.klg71.keycloakmigrationplugin

import de.klg71.keycloakmigration.MigrationArgs
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction


open class KeycloakMigrationPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.create<KeycloakMigrationExtension>("keycloakmigration", KeycloakMigrationExtension::class.java)
        project.tasks.create<KeycloakMigrationTask>("keycloakMigrate", KeycloakMigrationTask::class.java).run {
            description="Execute keycloak migrations"
            group="keycloakmigration"
        }
    }
}

open class KeycloakMigrationExtension {
    var migrationFile = "keycloak-changelog.yml"
    var baseUrl = "http://localhost:8080"
    var adminUser = "admin"
    var adminPassword = "admin"
    var realm = "master"
}

open class GradleMigrationArgs(private val adminUser: String, private val adminPassword: String, private val migrationFile: String, private val baseUrl: String, private val realm: String) : MigrationArgs {
    override fun adminUser() = adminUser
    override fun adminPassword() = adminPassword
    override fun baseUrl() = baseUrl
    override fun migrationFile() = migrationFile
    override fun realm() = realm
}

open class KeycloakMigrationTask : DefaultTask() {
    @Suppress("unused")
    @TaskAction
    fun migrate() {
        project.extensions.findByType(KeycloakMigrationExtension::class.java)!!.let {
            GradleMigrationArgs(it.adminUser, it.adminPassword, it.migrationFile, it.baseUrl, it.realm)
        }.let {
            de.klg71.keycloakmigration.migrate(it)
        }
    }

}
