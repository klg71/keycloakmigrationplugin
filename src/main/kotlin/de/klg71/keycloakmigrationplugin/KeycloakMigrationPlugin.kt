package de.klg71.keycloakmigrationplugin

import de.klg71.keycloakmigration.MigrationArgs
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.nio.file.Paths


open class KeycloakMigrationPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.create<KeycloakMigrationTask>("keycloakMigrate", KeycloakMigrationTask::class.java).run {
            description = "Execute keycloak migrations"
            group = "keycloakmigration"
        }
    }
}

open class GradleMigrationArgs(private val adminUser: String, private val adminPassword: String, private val migrationFile: String, private val baseUrl: String, private val realm: String, private val clientId: String) : MigrationArgs {
    override fun adminUser() = adminUser
    override fun adminPassword() = adminPassword
    override fun baseUrl() = baseUrl
    override fun migrationFile() = migrationFile
    override fun realm() = realm
    override fun clientId() = clientId
}

open class KeycloakMigrationTask : DefaultTask() {
    @Input
    var adminUser = "admin"
    @Input
    var adminPassword = "admin"
    @Input
    var migrationFile = "keycloak-changelog.yml"
    @Input
    var baseUrl = "http://localhost:8080"
    @Input
    var realm = "master"
    @Input
    var clientId = "admin-cli"


    @Suppress("unused")
    @TaskAction
    fun migrate() {
        GradleMigrationArgs(adminUser, adminPassword, Paths.get(project.projectDir.toString(), migrationFile).toString(), baseUrl, realm, clientId)
                .let {
                    de.klg71.keycloakmigration.migrate(it)
                }
    }

}
