plugins {
    java
}

version = "2.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    //compile(fileTree(mapOf("dir" to "libs", "include" to  "*.jar")))
    implementation("org.apache.pdfbox:pdfbox:2.0.16")
    implementation("org.apache.pdfbox:preflight:2.0.16")
    implementation("commons-cli:commons-cli:1.4")

    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.jar {
    manifest {
        attributes(mapOf("Main-Class" to "org.tuurneckebroeck.pdfutil.PdfUtil"))
    }

    // Voeg dependencies toe
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }

    // FIXME Classfiles worden 2 keer gepackaged!
}
