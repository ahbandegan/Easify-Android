plugins {
    id("com.android.library")
    id("maven-publish")
}

android {
    namespace = "ir.amirhesambandegan.easify_android_umbrella"
    compileSdk = 37

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    api(project(":easify-biometric"))
    api(project(":easify-bluetooth"))
    api(project(":easify-context"))
    api(project(":easify-file"))
    api(project(":easify-fintech"))
    api(project(":easify-form"))
    api(project(":easify-format"))
    api(project(":easify-haptic"))
    api(project(":easify-image"))
    api(project(":easify-lifecycle"))
    api(project(":easify-location"))
    api(project(":easify-network"))
    api(project(":easify-notification"))
    api(project(":easify-permission"))
    api(project(":easify-persian"))
    api(project(":easify-security"))
    api(project(":easify-sensor"))
    api(project(":easify-storage"))
    api(project(":easify-ui"))
    api(project(":easify-validation"))
    api(project(":easify-camera"))
    api(project(":easify-state"))
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "ir.amirhesambandegan.easify-android"
                artifactId = "easify-android"
                version = "2.2.0"
            }
        }
    }
}

