plugins {
    alias(libs.plugins.android.library)
    id("maven-publish")
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "ir.amirhesambandegan.easify_state"
    compileSdk = 37

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "ir.amirhesambandegan.easify-android"
                artifactId = "easify-state"
                version = "2.2.0"
            }
        }
    }
}

dependencies {
    api(libs.androidx.core.ktx)
    api(libs.kotlinx.coroutines.android)
    api(libs.kotlinx.coroutines.core)

    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.ui.graphics)
    api(libs.androidx.compose.activity)

    api(libs.androidx.lifecycle.viewmodel.ktx)
    api(libs.androidx.lifecycle.runtime.compose)
    api(libs.androidx.appcompat)
    api(libs.material)

    testImplementation(libs.junit)
}