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
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        maven("https://jitpack.io") {
            content {
                includeGroupByRegex("com\\.github\\..*")
            }
        }
    }
}

rootProject.name = "Easify-Android"
include(
    ":easify-validation",
    ":easify-format-core",
    ":easify-persian-core",
    ":easify-fintech-core",
    ":easify-state-core",
    ":easify-form-core",
    ":easify-network-core",
    ":easify-auth-core",
    ":easify-location-core",
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
    ":easify-camera",
    ":easify-state",
    ":easify-auth",
    ":easify-android"
)
