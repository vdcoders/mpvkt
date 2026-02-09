import com.vanniktech.maven.publish.SonatypeHost
import io.gitlab.arturbosch.detekt.Detekt

plugins {
  alias(libs.plugins.ksp)
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.compose.compiler)
  alias(libs.plugins.room)
  alias(libs.plugins.detekt)
  alias(libs.plugins.about.libraries)
  alias(libs.plugins.kotlin.serialization)
}
version = "0.0.4"
android {
  namespace = "live.mehiz.mpvkt"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        vectorDrawables.useSupportLibrary = true
        consumerProguardFiles("consumer-rules.pro")
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      isShrinkResources = false
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  kotlinOptions {
    jvmTarget = "17"
  }

  buildFeatures {
    compose = true
    viewBinding = true
    buildConfig = true
  }

  composeCompiler {
    includeSourceInformation = true
  }

  packaging {
    resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
  }
}

kotlin {
  compilerOptions {
    freeCompilerArgs.addAll("-Xwhen-guards", "-Xcontext-parameters")
  }
}

room {
  schemaDirectory("$projectDir/schemas")
}

dependencies {
  implementation(libs.androidx.activity.compose)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.ui)
  implementation(libs.androidx.ui.graphics)
  implementation(libs.androidx.material3.android)
  implementation(libs.androidx.ui.tooling.preview)
  debugImplementation(libs.androidx.ui.tooling)
  implementation(libs.bundles.compose.navigation3)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.compose.constraintlayout)
  implementation(libs.androidx.material3.icons.extended)
  implementation(libs.androidx.compose.animation.graphics)
  implementation(libs.material)
  implementation(libs.androidx.preferences.ktx)
  implementation(libs.androidx.documentfile)
  implementation(libs.mediasession)
  implementation(libs.saveable)

  implementation(libs.mpv.lib)

  implementation(platform(libs.koin.bom))
  implementation(libs.bundles.koin)

  implementation(libs.seeker)
  implementation(libs.compose.prefs)
  implementation(libs.bundles.about.libs)
  implementation(libs.simple.icons)

  implementation(libs.room.runtime)
  ksp(libs.room.compiler)
  implementation(libs.room.ktx)

  implementation(libs.detekt.gradle.plugin)
  detektPlugins(libs.detekt.rules.compose)
  detektPlugins(libs.detekt.formatter)

  implementation(libs.kotlinx.immutable.collections)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.truetype.parser)
  implementation(libs.fsaf)
}

detekt {
  parallel = true
  allRules = false
  buildUponDefaultConfig = true
  config.setFrom("$projectDir/config/detekt/detekt.yml")
}

tasks.withType<Detekt>().configureEach {
  setSource(files(project.projectDir))
  exclude("**/build/**")
  reports {
    html.required.set(true)
    md.required.set(true)
  }
}
fun getVersionName(): String = project.version.toString()
fun getGroupId(): String = "io.github.vdcoders"

val sourceJar by tasks.registering(org.gradle.jvm.tasks.Jar::class) {
    archiveClassifier.set("sources")
    from(android.sourceSets["main"].java.srcDirs)
}

afterEvaluate {
    mavenPublishing {
        publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
        signAllPublications()

        coordinates(
            getGroupId(),
            "mpvkt",
            getVersionName()
        )

        pom {
            name.set("mpvkt Android library")
            description.set("The mpv library used by mpvkt.")
            inceptionYear.set("2025")
            url.set("https://github.com/vdcoders/mpvkt")

            licenses {
                license {
                    name.set("MIT License")
                    url.set("https://opensource.org/license/mit/")
                    distribution.set("repo")
                }
            }

            developers {
                developer {
                    id.set("vdcoders")
                    name.set("vCoderz")
                    url.set("https://github.com/vdcoders")
                }
            }

            scm {
                url.set("https://github.com/vdcoders/mpvkt")
                connection.set("scm:git:git://github.com/vdcoders/mpvkt.git")
                developerConnection.set("scm:git:ssh://git@github.com/vdcoders/mpvkt.git")
            }
        }
    }
}
