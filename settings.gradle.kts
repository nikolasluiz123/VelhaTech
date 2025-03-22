pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "VelhaTech"
include(":app")
include(":velha-tech-core")
include(":velha-tech-firebase-models")
include(":velha-tech-firebase-data-access")
include(":velha-tech-compose-components")
include(":velha-tech-firebase-auth")
include(":velha-tech-firebase-apis")
