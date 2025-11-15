import com.android.build.gradle.LibraryExtension
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("maven-publish")
    id("signing")
}

// load gradle.properties
val localProps = Properties()
val propsFile = rootProject.file("gradle.properties")
if (propsFile.exists()) {
    localProps.load(propsFile.inputStream())
}

android {
    namespace = "com.priyanshparekh.recyclerplus"
    compileSdk = 35

    afterEvaluate {
        // Task for sources jar
        val androidSourcesJar by tasks.register<Jar>("androidSourcesJar") {
            archiveClassifier.set("sources")
            val android = extensions.findByType<LibraryExtension>()
            from(android?.sourceSets?.getByName("main")?.java?.srcDirs)
        }

        // Task for javadoc jar (placeholder)
        val androidJavadocsJar by tasks.register<Jar>("androidJavadocsJar") {
            archiveClassifier.set("javadoc")
            from("$projectDir/README.md")
        }

        tasks.register("generateCheckSums") {
            group = "verification"
            description = "Generates MD5 and SHA1 checksums for all release artifacts"

            doLast {

                val artifactDir = File(System.getProperty("user.home") +
                        "/.m2/repository/io/github/pparekh2009/recyclerplus/1.0.0")

                if (!artifactDir.exists()) {
                    println("Artifact directory not found: $artifactDir")
                    return@doLast
                }
//                val artifacts = listOf(
//                    file("$buildDir/outputs/aar/${project.name}-release.aar"),
//                    file("$buildDir/libs/${project.name}-release-sources.jar"),
//                    file("$buildDir/libs/${project.name}-release-javadoc.jar"),
//                    file("$buildDir/publishing/release/${project.name}-1.0.0.pom"),
//                )

                artifactDir.listFiles { f -> f.isFile && !f.name.endsWith(".md5") && !f.name.endsWith(".sha1") && !f.name.endsWith(".asc")}
                    ?.forEach { artifact ->
                        if (artifact.exists()) {
                            ant.withGroovyBuilder {
                                "checksum"("file" to artifact, "algorithm" to "MD5", "todir" to artifact.parent)
                                "checksum"("file" to artifact, "algorithm" to "SHA1", "todir" to artifact.parent)
                            }
                        }
                    }
            }
        }

        publishing {
            publications {
                create<MavenPublication>("release") {
                    groupId = project.findProperty("GROUP") as String
                    artifactId = project.findProperty("POM_ARTIFACT_ID") as String
                    version = project.findProperty("VERSION_NAME") as String

                    // Publish the AAR file
                    artifact("$buildDir/outputs/aar/${project.name}-release.aar")

                    // Attach sources + javadoc jars
                    artifact(androidSourcesJar)
                    artifact(androidJavadocsJar)

                    pom {
                        name.set("RecyclerPlus")
                        description.set("A Recyclerview utility library for Android")
                        url.set("https://github.com/pparekh2009/RecyclerPlus")
                        licenses {
                            license {
                                name.set("MIT License")
                                url.set("https://opensource.org/licenses/MIT")
                            }
                        }
                        developers {
                            developer {
                                id.set("pparekh2009")
                                name.set("Priyansh Parekh")
                            }
                        }
                        scm {
                            connection.set("scm:git:github.com/pparekh2009/RecyclerPlus.git")
                            developerConnection.set("scm:git:ssh://github.com/pparekh2009/RecyclerPlus.git")
                            url.set("https://github.com/pparekh2009/RecyclerPlus")
                        }
                    }
                }
            }

            repositories {
                maven {
                    name = "sonatype"
                    url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                    credentials {
                        username = project.findProperty("SONATYPE_USERNAME") as String?
                        password = project.findProperty("SONATYPE_PASSWORD") as String?
                    }
                }
            }
        }

        tasks.matching { it.name == "signReleasePublication" }.configureEach {
            dependsOn(tasks.named("bundleReleaseAar"))
        }

        tasks.matching { it.name == "publishToMavenLocal" }.configureEach {
            finalizedBy(tasks.named("generateCheckSums"))
        }



        signing {
            sign(publishing.publications["release"])
        }
    }

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