# Keycloakmigration Gradle Plugin ![Maven metadata URL](https://img.shields.io/maven-metadata/v/https/plugins.gradle.org/m2/de/klg71/keycloakmigrationplugin/maven-metadata.xml.svg?label=gradle)

This project is inspired by the database migration tool liquibase.
It aims to provide a similar mechanism for Keycloak.


The main repository is located at [keycloak_migration](https://github.com/klg71/keycloakmigration). This repository contains only the gradle plugin.

# Setup

```gradle
plugins {
  id "de.klg71.keycloakmigrationplugin" version "0.0.x"
}
```

# Usage
## Groovy

    task keycloakMigrateLocal(type: KeycloakMigrationTask) {
      group = "keycloak"
      description = "Migrate the keycloak instance"

      migrationFile = "migration/keycloak-changelog.yml"
      adminUser = "admin"
      adminPassword = "admin"
      baseUrl = "http://localhost:8080"
      realm = "master"
    }

## Kotlin

    register<KeycloakMigrationTask>("keycloakMigrateLocal") {
        group = "keycloak"
        description = "Migrate the keycloak instance"

        migrationFile = "migration/keycloak-changelog.yml"
        adminUser = "admin"
        adminPassword = "admin"
        baseUrl = "http://localhost:8080/auth"
        realm = "master"
    }
