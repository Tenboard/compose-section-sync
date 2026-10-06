import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    id("com.vanniktech.maven.publish.base") version "0.34.0"
    signing
}

android {
    namespace = "io.github.tenboard.section_sync"
    compileSdk = 37

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

publishing {
    publications {
        register<MavenPublication>("release") {
            afterEvaluate {
                from(components["release"])
            }
        }
    }

    repositories {
        maven {
            name = "transfer"
            url = uri(
                layout.buildDirectory.dir("maven-repository")
            )
        }
    }
}

mavenPublishing {
    coordinates("io.github.tenboard", "compose-section-sync", "0.1.0-beta01")
    publishToMavenCentral(automaticRelease = false)
    signAllPublications()

    pom {
        name.set("Compose Section Sync")
        description.set("Bidirectional synchronization between section tabs and Jetpack Compose LazyVerticalGrid scrolling.")
        url.set("https://github.com/Tenboard/compose-section-sync")
        licenses {
            license {
                name.set("MIT License")
                url.set("https://opensource.org/license/mit")
                distribution.set("repo")
            }
        }
        developers {
            developer {
                id.set("Tenboard")
                name.set("Tenboard")
                url.set("https://github.com/Tenboard")
            }
        }
        scm {
            url.set("https://github.com/Tenboard/compose-section-sync")
            connection.set("scm:git:https://github.com/Tenboard/compose-section-sync.git")
            developerConnection.set("scm:git:ssh://git@github.com/Tenboard/compose-section-sync.git")
        }
    }
}

signing {
    useGpgCmd()
}

tasks.withType<Zip>().matching {
    it.name in setOf("bundleReleaseAar", "sourceReleaseJar", "javaDocReleaseJar")
}.configureEach {
    from(rootProject.file("LICENSE")) {
        into("META-INF/compose-section-sync")
    }
}

tasks.withType<Jar>().matching { it.name == "javaDocReleaseJar" }.configureEach {
    from(rootProject.file("README.md"))
}

dependencies {
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.runtime)

    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}
