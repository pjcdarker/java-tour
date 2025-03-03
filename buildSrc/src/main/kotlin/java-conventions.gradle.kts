import java.net.URI

plugins {
    id("java")
    id("idea")
}

group = "com.pjcdarker"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenLocal()
    maven {
        url = URI.create("https://maven.aliyun.com/nexus/content/groups/public")
    }
    mavenCentral()
}


dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:${Libs.junit5.version}")
    testImplementation("org.junit.jupiter:junit-jupiter-params:${Libs.junit5.version}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${Libs.junit5.version}")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:${Libs.junit5.platformVersion}")

    testImplementation("org.mockito:mockito-core:${Libs.mockitoVersion}")
}

tasks.test {
    useJUnitPlatform()
}


object Libs {
    const val mockitoVersion = "5.15.2"
    object junit5 {
        const val version = "5.12.0"
        const val platformVersion = "1.12.0"
    }
}