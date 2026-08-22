plugins {
    alias(libs.plugins.android.library)
    id("maven-publish")
}

android {
    namespace = "ir.amirhesambandegan.easify_context"
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
                artifactId = "easify-context"
                version = "2.1.0"
            }
        }
    }
}

dependencies {
    api(libs.androidx.core.ktx)
    api(libs.kotlinx.coroutines.android)
    api(libs.kotlinx.coroutines.core)

    api(libs.androidx.appcompat)
}


