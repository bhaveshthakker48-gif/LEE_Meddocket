plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")// Apply the Kotlin Kapt plugin
    id ("dagger.hilt.android.plugin")
    id ("kotlin-parcelize")

  //  id ("com.google.devtools.ksp")
}




android {
    namespace = "org.impactindiafoundation.iifllemeddocket"
    compileSdk = 35

    defaultConfig {
        applicationId = "org.impactindiafoundation.iifllemeddocket"
        minSdk = 27
        targetSdk = 35
        versionCode = 94
        versionName = "2.58"

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

        debug {
            isMinifyEnabled = false
           /* proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )*/
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    viewBinding.enable=true
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    kapt {
        arguments {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }


}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.0-beta01")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.0-beta01")
    implementation("com.google.android.play:app-update-ktx:2.1.0")
    implementation("com.google.android.play:app-update:2.1.0")
    implementation("androidx.compose.foundation:foundation-android:1.7.0-beta01")
    implementation("androidx.activity:activity:1.9.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.14")
    implementation ("com.squareup.retrofit2:converter-scalars:2.11.0")

    //Coroutine
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation ("com.google.code.gson:gson:2.11.0")
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0")
    // ViewModel utilities for Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")
    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.0")
    // Lifecycles only (without ViewModel or LiveData)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")

    // Saved state module for ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.8.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")


    //qrScanner library
    implementation ("com.journeyapps:zxing-android-embedded:4.3.0")


    implementation ("com.intuit.ssp:ssp-android:1.1.1")
    implementation ("com.intuit.sdp:sdp-android:1.1.1")

    implementation ("androidx.room:room-runtime:2.6.1")
    implementation ("androidx.room:room-ktx:2.6.1")
 /*   kapt( "androidx.room:room-compiler:2.2.5")*/

    implementation ("androidx.room:room-runtime:2.6.1")
    kapt ("androidx.room:room-compiler:2.6.1")


    //kapt ("androidx.room:room-compiler:2.4.0")
    //ksp("androidx.room:room-compiler:2.4.0")

    implementation ("androidx.room:room-runtime:2.6.1")
    implementation ("androidx.room:room-ktx:2.6.1")
    kapt ("androidx.room:room-compiler:2.6.1")

    implementation ("com.karumi:dexter:6.2.3")
    implementation ("androidx.webkit:webkit:1.12.0-alpha01")

    implementation ("com.google.zxing:core:3.5.3")
    implementation ("com.journeyapps:zxing-android-embedded:4.3.0")

    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")

    implementation("androidx.lifecycle:lifecycle-livedata:2.8.0")

    implementation ("com.github.bumptech.glide:glide:4.16.0")

   // implementation ("com.google.android.play:core:1.6.0")
    


    //implementation("com.google.android.play:app-update:2.1.0")

   // implementation("com.google.android.play:app-update-ktx:2.1.0")

   // implementation ("com.google.android.play:core:1.10.0")
    //implementation ("com.google.android.play:core:2.1.0")


   /* implementation("com.example:library1:1.0") {
        exclude(group = "com.example.duplicategroup", module = "duplicatelib")
    }*/



    // Activity KTX for viewModels()
    implementation("androidx.activity:activity-ktx:1.8.1")
    implementation("androidx.fragment:fragment-ktx:1.6.2")

    //Dagger - Hilt
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")
    kapt("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.5.0")

    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")

    //chucker
    debugImplementation("com.github.chuckerteam.chucker:library:4.0.0")
    releaseImplementation("com.github.chuckerteam.chucker:library-no-op:4.0.0")

    //Lottie
    //implementation("com.airbnb.android:lottie:3.4.0")

    implementation("com.github.GrenderG:Toasty:1.5.2")

    //image picker
    implementation("com.github.colourmoon:image-picker:v1.0.1")



    // CameraX core library
    //def camerax_version = "1.1.0-alpha10"
    implementation ("androidx.camera:camera-core:1.1.0-alpha10")
//    implementation ("androidx.camera:camera-camera2:1.1.0-alpha10")
//    implementation ("androidx.camera:camera-lifecycle:1.1.0-alpha10")
//    implementation ("androidx.camera:camera-view:1.1.0-alpha10")
//    implementation ("androidx.camera:camera-extensions:1.1.0-alpha10") // Optional

    // ML Kit Barcode Scanning dependency
    implementation("com.google.mlkit:barcode-scanning:17.2.0")
    // CameraX dependencies
    implementation ("androidx.camera:camera-camera2:1.1.0")
    implementation ("androidx.camera:camera-lifecycle:1.1.0")
    implementation ("androidx.camera:camera-view:1.0.0-alpha32")

    //qr code scanner
    implementation ("me.dm7.barcodescanner:zxing:1.9.8")


//    implementation ("com.google.android.play:core:2.1.0")
    implementation ("com.airbnb.android:lottie:6.0.0")

    implementation ("com.google.mlkit:barcode-scanning:17.1.0")


}