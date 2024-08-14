plugins {
    id("jinProject.android.feature")
}

android {
    namespace = "com.jinproject.features.watch"
}

dependencies {
    implementation(project(":features:alarm"))
    implementation(libs.constraintLayout)

    implementation(libs.billing.ktx)

    // compose NumberPicker
    implementation("com.chargemap.compose:numberpicker:1.0.3")
}