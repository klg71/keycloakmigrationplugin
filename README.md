# Keycloakmigration Gradle Plugin ![Maven metadata URL](https://img.shields.io/maven-metadata/v/https/plugins.gradle.org/m2/de/klg71/keycloakmigrationplugin/maven-metadata.xml.svg?label=gradle)
This project is inspired by the database migration tool liquibase.
It aims to provide a similar mechanism for Keycloak.


The main repository is located at [keycloak_migration](https://github.com/klg71/keycloakmigration). This repository contains only the gradle plugin.

> To version 0.1.0 there has been a major change to the hashing logic and you need to update all your hashes! 
>
## Setup

```gradle
plugins {
  id "de.klg71.keycloakmigrationplugin" version "x.x.x"
}
```

## Usage
### Groovy

    task keycloakMigrateLocal(type: KeycloakMigrationTask) {
      group = "keycloak"
      description = "Migrate the keycloak instance"

      migrationFile = "migration/keycloak-changelog.yml"
      adminUser = "admin"
      adminPassword = "admin"
      baseUrl = "http://localhost:8080"
      realm = "master"
      parameters = [USERNAME: "testUser", PASSWORD: "testPassword"]
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
      parameters = [USERNAME: "testUser", PASSWORD: "testPassword"]
    }

> Don't use this task in build pipelines! This ist just for manual hash migration.


### Kotlin

    register<KeycloakMigrationTask>("keycloakMigrateLocal") {
        group = "keycloak"
        description = "Migrate the keycloak instance"

        migrationFile = "migration/keycloak-changelog.yml"
        adminUser = "admin"
        adminPassword = "admin"
        baseUrl = "http://localhost:8080/auth"
        realm = "master"
        parameters = mapOf(
                "USER_NAME" to "testUser",
                "PASSWORD" to "password"
        )
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
        parameters = mapOf(
                "USER_NAME" to "testUser",
                "PASSWORD" to "password"
        )
    }
> Don't use this task in build pipelines! This ist just for manual hash migration.

    
For usage information of the migration api please see the containing repository:  [keycloak_migration](https://github.com/klg71/keycloakmigration)

## In gradle docker container in kubernetes

According to container spec: https://kubernetes.io/docs/reference/generated/kubernetes-api/v1.10/ (Container v1 core)

With a volume `migration-volume` containing the files:
- build.gradle.kts (or build.gradle)
- settings.gradle.kts (or settings.gradle)
- keycloak-change.yml

```yaml

    - name: keycloak-migration
      image: gradle:6.2.2-jre11
      command: ['/bin/bash','-c','cp ./project/* . && gradle keycloakMigrateK8s --stacktrace --info && tail -f /dev/null']
      workingDir: /home/gradle
      securityContext:
        runAsUser: 0
      volumeMounts:
      - name: migration-volume
        mountPath: /home/gradle/project
```

Notes
>> You can't run the container as init container because it needs keycloak up and running

>> The addition `&& tail -f /dev/null` is needed so that Kubernetes wont restart this container forever.
 Alternatively you can implement it as Job
