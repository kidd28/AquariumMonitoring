plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.capstone.aquariummonitoring"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.capstone.aquariummonitoring"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.intuit.sdp:sdp-android:1.1.0")

    implementation ("com.google.firebase:firebase-analytics")
    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
    implementation ("com.google.firebase:firebase-auth:22.1.2")
    implementation ("com.google.firebase:firebase-auth")
    implementation ("com.google.firebase:firebase-database:20.2.2")
    implementation ("com.google.firebase:firebase-storage:20.2.1")
    implementation ("com.google.firebase:firebase-core:21.1.1")
    implementation ("com.google.firebase:firebase-storage")
    implementation ("com.google.firebase:firebase-core:21.1.1")
}
