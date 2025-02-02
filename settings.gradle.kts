pluginManagement {
    includeBuild("build-logic")
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
        maven("https://jitpack.io")
    }
}

rootProject.name = "Mint"
include(":app")
include(
    "feature:login",
    "feature:home",
    "feature:user",
    "feature:search",
    "feature:notification",
    "feature:announcement",
    "feature:favorites",
    "feature:antenna"
)
include(
    "core:network",
    ":core:data",
    "core:model",
    "core:dataStore",
    "core:designsystem",
    "core:screens",
    "core:ui",
    "core:resValues",
    "core:domain"
)
include(":misskey-streaming")
include(":feature:channel")
