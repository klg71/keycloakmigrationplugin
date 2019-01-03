package de.klg71.keycloakmigration

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction


class KeycloakMigrationPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.create<KeycloakMigrationExtension>("keycloakmigration", KeycloakMigrationExtension::class.java)
        project.tasks.create<KeycloakMigrationTask>("keycloakMigrate", KeycloakMigrationTask::class.java) {
            it.migrate()
        }
    }
}

class KeycloakMigrationExtension {
    var migrationFile = "keycloak-changelog.yml"
    var baseUrl = "http://localhost:8080"
    var adminUser = "admin"
    var adminPassword = "admin"
}

class GradleMigrationArgs(private val adminUser: String, private val adminPassword: String, private val migrationFile: String, private val baseUrl: String) : MigrationArgs {
    override fun adminUser() = adminUser
    override fun adminPassword() = adminPassword
    override fun baseUrl() = adminPassword
    override fun migrationFile() = migrationFile
}

open class KeycloakMigrationTask : DefaultTask() {
    @Suppress("unused")
    @TaskAction
    fun migrate() {
        project.extensions.findByType(KeycloakMigrationExtension::class.java)!!.let {
            GradleMigrationArgs(it.adminUser, it.adminPassword, it.migrationFile, it.baseUrl)
        }.let {
            de.klg71.keycloakmigration.migrate(it)
        }
    }

}
