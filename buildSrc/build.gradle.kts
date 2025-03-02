import java.net.URI

plugins {
    `kotlin-dsl`
}

repositories {
    mavenLocal()
    maven {
        url = URI.create("https://maven.aliyun.com/nexus/content/groups/public")
    }
    mavenCentral()
}