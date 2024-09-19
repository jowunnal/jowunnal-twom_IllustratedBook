plugins {
    id("jinProject.android.feature")
    kotlin("plugin.parcelize")
}

android {
    namespace = "com.jinproject.features.alarm"
    compileSdk = 34
}

dependencies {
    implementation(libs.billing.ktx)

    // compose NumberPicker
    implementation("com.chargemap.compose:numberpicker:1.0.3")
}