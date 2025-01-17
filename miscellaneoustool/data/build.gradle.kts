import com.google.protobuf.gradle.GenerateProtoTask

plugins {
    id("jinProject.android.library")
    id("jinProject.android.hilt")
    id("jinProject.android.protobuf")
    id("jinProject.android.room")
}

android {
    namespace = "com.jinproject.data"

    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
    room {
        schemaDirectory("$projectDir/schemas")
    }
    sourceSets {
        getByName("androidTest").assets.srcDirs(files("$projectDir/schemas"))
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core"))

    testImplementation(libs.jUnit)
    androidTestImplementation(libs.test.ext)
    androidTestImplementation(libs.test.espresso)

    implementation(libs.coroutines.core)

    implementation(libs.datastore)
}

androidComponents {
    onVariants(selector().all()) { variant ->
        afterEvaluate {
            val protoTask =
                project.tasks.getByName("generate" + variant.name.replaceFirstChar { it.uppercaseChar() } + "Proto") as GenerateProtoTask

            project.tasks.getByName("ksp" + variant.name.replaceFirstChar { it.uppercaseChar() } + "Kotlin") {
                dependsOn(protoTask)
                (this as org.jetbrains.kotlin.gradle.tasks.AbstractKotlinCompileTool<*>).setSource(
                    protoTask.outputBaseDir
                )
            }
        }
    }
}