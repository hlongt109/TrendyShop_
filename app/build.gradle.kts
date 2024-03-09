plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.trendyshopteam.trendyshop"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.trendyshopteam.trendyshop"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        dataBinding = true
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity:1.8.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // firebase
    implementation(platform("com.google.firebase:firebase-bom:32.7.1"))
    implementation("com.google.firebase:firebase-analytics")
    implementation( "com.google.firebase:firebase-storage")
    implementation( "com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-auth")
    implementation ("com.google.firebase:firebase-firestore")
    implementation ("com.firebaseui:firebase-ui-database:8.0.0")
    // animation
    implementation ("com.airbnb.android:lottie:6.3.0")
    // glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation ("com.github.dhaval2404:imagepicker:2.1")
    //thư viện ảnh
    implementation("com.github.bumptech.glide:glide:4.12.0")
    //data binding
    implementation("androidx.databinding:databinding-runtime:7.1.0")
}