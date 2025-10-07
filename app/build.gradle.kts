plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.weba"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.weba"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        /*compose = true  // Включить поддержку Compose*/
        viewBinding = true
    }

    /*composeOptions {
        kotlinCompilerExtensionVersion = "1.9.0"  // Версия должна соответствовать Compose
    }*/

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(project(":domain"))

    //Koin
    implementation(libs.koin.core)
    implementation(libs.koin.android)

    //Compose
    /*implementation("androidx.compose.ui:ui:1.9.0")
    implementation("androidx.compose.material:material:1.9.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.9.0")
    implementation("androidx.activity:activity-compose:1.10.1")*/

    implementation (libs.androidx.recyclerview)
    implementation (libs.androidx.swiperefreshlayout)
}