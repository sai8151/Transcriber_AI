plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.android.speachtoanswer"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.android.speachtoanswer"
        minSdk = 22
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

//    implementation("androidx.camera.core:camera-core:1.1.0-alpha01")
//    implementation("androidx.camera.camera2:camera2:1.1.0-alpha01")

    //implementation("com.google.firebase:firebase-ml-vision:24.1.0")
//    implementation("com.google.firebase:firebase-core:21.1.1")
//    implementation("com.google.android.gms:play-services-mlkit-text-recognition:19.0.0")
//    implementation("com.google.firebase:firebase-ml-vision:24.1.0")
//    implementation("com.google.firebase:firebase-crashlytics-buildtools:2.9.9")
//    implementation("androidx.camera:camera-lifecycle:1.2.3")
//    implementation("androidx.camera:camera-view:1.2.3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}