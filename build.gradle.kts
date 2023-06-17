// https://v3.leedo.me/devs/51
// build.gradle.kts
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.6.21"

    id("org.springframework.boot") version "2.7.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    // ✅ KAPT(Kotlin Annotation Processing Tool)를 설치합니다
    kotlin("kapt") version kotlinVersion
    // ✅ Intellij에서 사용할 파일을 생성하는 플러그인입니다
    idea
}

group = "com.group"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    runtimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation ("com.querydsl:querydsl-jpa:5.0.0")
    implementation ("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly ("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly ("io.jsonwebtoken:jjwt-jackson:0.11.5") // or 'io.jsonwebtoken:jjwt-gson:JJWT_RELEASE_VERSION' for gson
    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
    implementation ("org.jsoup:jsoup:1.16.1")

    // ✅ querydsl을 설치합니다
    implementation("com.querydsl:querydsl-jpa:5.0.0")
    // ✅ KAPT(Kotlin Annotation Processing Tool)를 설정합니다
    //    kotlin 코드가 아니라면 kapt 대신 annotationProcessor를 사용합니다
    //    JPAAnnotationProcessor를 사용하기 위해 마지막에 :jpa를 붙입니다
    kapt("com.querydsl:querydsl-apt:5.0.0:jpa")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")

    implementation("org.springframework.cloud:spring-cloud-starter-aws:2.2.6.RELEASE")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.getByName<Jar>("jar") {
    enabled = false
}

// ✅ QClass를 Intellij가 사용할 수 있도록 경로에 추가합니다
idea {
    module {
        val kaptMain = file("build/generated/source/kapt/main")
        sourceDirs.add(kaptMain)
        generatedSourceDirs.add(kaptMain)
    }
}