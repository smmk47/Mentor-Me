plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")

}

android {
    namespace = "com.example.myapplication1"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myapplication1"
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {



    implementation ("io.agora.rtc:full-sdk:4.2.6")

    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("com.google.firebase:firebase-messaging:23.4.1")
    implementation("com.google.android.gms:play-services-cast-framework:21.4.0")
    // CameraX core library
    val camerax_version  = "1.1.0-alpha10"
    implementation ("androidx.camera:camera-core:$camerax_version")

// CameraX Camera2 extension library
    implementation ("androidx.camera:camera-camera2:$camerax_version")

// CameraX Lifecycle library
    implementation ("androidx.camera:camera-lifecycle:$camerax_version")

// CameraX View library
    implementation ("androidx.camera:camera-view:1.0.0-alpha25")



    implementation ("androidx.appcompat:appcompat:1.3.0")
    implementation ("androidx.core:core-ktx:1.6.0")
    implementation ("androidx.camera:camera-camera2:1.1.0")
    implementation ("androidx.camera:camera-lifecycle:1.1.0")
    implementation ("androidx.camera:camera-view:1.1.0")
    implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.5.30")


    implementation ("com.squareup.picasso:picasso:2.71828")
    implementation ("com.google.firebase:firebase-storage:20.0.0")

    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")


    implementation ("androidx.appcompat:appcompat:1.0.0")
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation  ("androidx.cardview:cardview:1.0.0")
    implementation ("com.google.android.material:material:<latest_version>")
    implementation ("androidx.recyclerview:recyclerview:1.2.1")
    implementation ("androidx.recyclerview:recyclerview:1.1.0")
    implementation ("androidx.recyclerview:recyclerview-selection:1.1.0")
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("com.google.firebase:firebase-database:20.3.1")
    implementation("androidx.camera:camera-core:1.3.2")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation ("androidx.test.ext:junit-ktx:1.1.3")
    androidTestImplementation ("androidx.test.espresso:espresso-intents:3.4.0")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.6.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.6.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:32.7.4"))

    // When using the BoM, you don't specify versions in Firebase library dependencies

    // Add the dependency for the Firebase SDK for Google Analytics
    implementation("com.google.firebase:firebase-analytics")


    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")


    implementation ("com.akexorcist:screenshot-detection:1.0.2")

    implementation("com.android.volley:volley:1.2.1")
    //implementation ("org.xmlpull:xmlpull:1.1.9c")

    implementation ("org.jsoup:jsoup:1.14.3")


    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation ("androidx.test.espresso:espresso-intents:3.4.0")
    implementation ("com.android.volley:volley:1.2.1")

}