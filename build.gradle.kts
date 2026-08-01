// Execute Tasks in SubModule: gradle MODUL:clean build
plugins {
    id("de.freese.gradle.conventions").apply(false)
    id("io.spring.dependency-management").apply(false)
}

allprojects {
    plugins.apply("base")

    tasks.named<Delete>("clean") {
        delete(layout.projectDirectory.dir("bin"))
        delete("logs")
        delete("out")
        delete("target")
    }
}

subprojects {
    plugins.apply("de.freese.gradle.conventions")
    plugins.apply("io.spring.dependency-management")

//    if (file("src/main/java").isDirectory()) {
//        apply plugin: "name.remal.sonarlint"
//    }

    extensions.configure(io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension::class.java) {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:" + property("version_springBoot"))
        }

        dependencies {
            dependency("commons-cli:commons-cli:" + property("version_commonsCli"))
        }
    }

    // pluginManager.withPlugin("java") {
    //     dependencies {
    //         testImplementation("org.junit.jupiter:junit-jupiter")
    //         testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    //     }
    // }
}

