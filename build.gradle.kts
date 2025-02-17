// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("com.google.dagger.hilt.android") version libs.versions.hilt.get() apply false
}
buildscript {
    dependencies {
        classpath(libs.secrets.gradle.plugin)
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.8.1")
    }
}