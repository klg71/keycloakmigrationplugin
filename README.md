# Keycloakmigration Gradle Plugin ![Maven metadata URL](https://img.shields.io/maven-metadata/v/https/plugins.gradle.org/m2/de/klg71/keycloakmigrationplugin/maven-metadata.xml.svg?label=gradle)
This project is inspired by the database migration tool liquibase.
It aims to provide a similar mechanism for Keycloak.


The main repository is located at [keycloak_migration](https://github.com/klg71/keycloakmigration). This repository contains only the gradle plugin.

> To version 0.1.0 there has been a major change to the hashing logic and you need to update all your hashes! 
>
# Setup

```gradle
plugins {
  id "de.klg71.keycloakmigrationplugin" version "x.x.x"
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
    
Ignore and replace failing hashes:

    task keycloakMigrateCorrectHashesLocal(type: KeycloakMigrationCorrectHashesTask) {
      group = "keycloak"
      description = "Migrate the keycloak instance"

      migrationFile = "migration/keycloak-changelog.yml"
      adminUser = "admin"
      adminPassword = "admin"
      baseUrl = "http://localhost:8080"
      realm = "master"
    }

> Don't use this task in build pipelines! This ist just for manual hash migration.


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
    
Ignore and replace failing hashes:

    register<KeycloakMigrationCorrectHashesTask>("keycloakMigrateCorrectHashesLocal") {
        group = "keycloak"
        description = "Migrate the keycloak instance"

        migrationFile = "migration/keycloak-changelog.yml"
        adminUser = "admin"
        adminPassword = "admin"
        baseUrl = "http://localhost:8080/auth"
        realm = "master"
    }
> Don't use this task in build pipelines! This ist just for manual hash migration.

    
For usage information of the migration api please see the containing repository:  [keycloak_migration](https://github.com/klg71/keycloakmigration)

