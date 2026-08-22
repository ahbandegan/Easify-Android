pluginManagement {
    repositories {
        maven { url = uri("https://maven.myket.ir/") }
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        maven("https://jitpack.io")
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { url = uri("https://maven.myket.ir/") }
        google()
        maven("https://jitpack.io")
        mavenCentral()
    }
}

rootProject.name = "Easify-Android"
include(
    ":easify-biometric",
    ":easify-bluetooth",
    ":easify-context",
    ":easify-file",
    ":easify-fintech",
    ":easify-form",
    ":easify-format",
    ":easify-haptic",
    ":easify-image",
    ":easify-lifecycle",
    ":easify-location",
    ":easify-network",
    ":easify-notification",
    ":easify-permission",
    ":easify-persian",
    ":easify-security",
    ":easify-sensor",
    ":easify-storage",
    ":easify-ui",
    ":easify-validation",
    ":easify-camera"
)
include(":easify-android")
