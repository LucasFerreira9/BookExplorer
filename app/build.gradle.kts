import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.bookexplorer"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.bookexplorer"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        //load the values from api_keys.properties file
        val propName = "BOOKS_API_KEY"
        val keystoreFile = rootProject.file("api_keys.properties")
        val properties = Properties()
        properties.load(keystoreFile.inputStream())

        buildConfigField(
                "String",
                propName,
                "\"${properties.getProperty(propName)}\""
        )
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.room)
    annotationProcessor(libs.room.compiler)
    implementation(libs.gson)
    implementation(libs.okHttp)

    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}