plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    id("maven-publish")
}

android {
    namespace = "ru.duckrya.compose.calendar"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        targetSdk = 36
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true

        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.compose.ui)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)
    coreLibraryDesugaring(libs.desugar.jdk.libs)
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "ru.duckrya"
                artifactId = "compose-calendar"
                version = "1.0.0"
            }
        }
    }
}