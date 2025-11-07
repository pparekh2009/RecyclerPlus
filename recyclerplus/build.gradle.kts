import com.android.build.gradle.LibraryExtension

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("maven-publish")
    id("signing")
}

android {
    namespace = "com.priyanshparekh.recyclerplus"
    compileSdk = 35

//    afterEvaluate {
//        // Task for sources jar
//        val androidSourcesJar by tasks.register<Jar>("androidSourcesJar") {
//            archiveClassifier.set("sources")
//            val android = extensions.findByType<LibraryExtension>()
//            from(android?.sourceSets?.getByName("main")?.java?.srcDirs)
//        }
//
//        // Task for javadoc jar (placeholder)
//        val androidJavadocsJar by tasks.register<Jar>("androidJavadocsJar") {
//            archiveClassifier.set("javadoc")
//            from("$projectDir/README.md")
//        }
//
//        tasks.register("generateCheckSums") {
//            group = "verification"
//            description = "Generates MD5 and SHA1 checksums for all release artifacts"
//
//            doLast {
//                val artifacts = listOf(
//                    file("$buildDir/outputs/aar/${project.name}-release.aar"),
//                    file("$buildDir/libs/${project.name}-release-sources.jar"),
//                    file("$buildDir/libs/${project.name}-release-javadoc.jar"),
//                    file("$buildDir/publishing/release/${project.name}-1.0.0.pom"),
//                )
//
//                artifacts.forEach { artifact ->
//                    if (artifact.exists()) {
//                        ant.withGroovyBuilder {
//                            "checksum"("file" to artifact, "algorithm" to "MD5", "todir" to artifact.parent)
//                            "checksum"("file" to artifact, "algorithm" to "SHA1", "todir" to artifact.parent)
//                        }
//                    }
//                }
//            }
//        }
//
//        publishing {
//            publications {
//                create<MavenPublication>("release") {
//                    groupId = "io.github.pparekh2009"
//                    artifactId = "recyclerplus"
//                    version = "1.0.0"
//
//                    // Publish the AAR file
//                    artifact("$buildDir/outputs/aar/${project.name}-release.aar")
//
//                    // Attach sources + javadoc jars
//                    artifact(androidSourcesJar)
//                    artifact(androidJavadocsJar)
//
//                    pom {
//                        name.set("RecyclerPlus")
//                        description.set("A Recyclerview utility library for Android")
//                        url.set("https://github.com/pparekh2009/RecyclerPlus")
//                        licenses {
//                            license {
//                                name.set("The Apache License, Version 2.0")
//                                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
//                            }
//                        }
//                        developers {
//                            developer {
//                                id.set("pparekh2009")
//                                name.set("Priyansh Parekh")
//                            }
//                        }
//                        scm {
//                            connection.set("scm:git:github.com/pparekh2009/RecyclerPlus.git")
//                            developerConnection.set("scm:git:ssh://github.com/pparekh2009/RecyclerPlus.git")
//                            url.set("https://github.com/pparekh2009/RecyclerPlus")
//                        }
//                    }
//                }
//            }
//
//            repositories {
//                maven {
//                    name = "OSSRH"
//                    url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
//                    credentials {
//                        username = findProperty("ossrhUsername") as String?
//                        password = findProperty("ossrhPassword") as String?
//                    }
//                }
//            }
//        }
//
////        signing {
////            sign(publishing.publications["release"])
////        }
//    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}