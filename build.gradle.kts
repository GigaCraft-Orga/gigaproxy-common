plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "dev.grafjojo"
version = "1.0"

repositories {
    mavenLocal()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }

    maven {
        name = "henkelmax.public"
        url = uri("https://maven.maxhenkel.de/repository/public")
    }

}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.3.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.3.0-SNAPSHOT")
    implementation("dev.grafjojo:gigacraft-core:1.0")
    implementation("de.maxhenkel.configbuilder:configbuilder:2.0.1")
}

tasks {

    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    assemble {
        dependsOn("shadowJar")
    }

}
java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}
