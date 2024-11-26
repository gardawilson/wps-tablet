pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // Repository untuk jCIFS
        maven {
            url = uri("https://repo1.maven.org/maven2/")
        }
        // Tambahkan JitPack repository
        maven {
            url = uri("https://jitpack.io")
            content {
                includeGroup("com.github")
            }
        }
    }
}

rootProject.name = "My Application"
include(":app")