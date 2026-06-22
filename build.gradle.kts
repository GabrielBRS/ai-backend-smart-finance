import com.google.protobuf.gradle.*

plugins {
    java
    id("org.springframework.boot") version "4.1.0"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.graalvm.buildtools.native") version "1.1.1"
    id("com.google.protobuf") version "0.10.0"
}

group = "org.sgt.ai.backend.smartfinance"
version = "0.0.1-SNAPSHOT"
description = "ai-backend-smart-finance"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()
}

extra["springAiVersion"] = "2.0.0"

// Alinha o protobuf-java gerenciado pelo BOM do Boot com o protoc/gRPC abaixo.
extra["protobuf.version"] = "3.25.8"

dependencyManagement {
    imports {
        mavenBom("org.springframework.ai:spring-ai-bom:${property("springAiVersion")}")
    }
}

dependencies {
    // ===== Dependencias originais =====
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-restclient")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jdbc-test")
    testImplementation("org.springframework.boot:spring-boot-starter-jdbc-test")
    testImplementation("org.springframework.boot:spring-boot-starter-restclient-test")
    testImplementation("org.springframework.boot:spring-boot-starter-thymeleaf-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testCompileOnly("org.projectlombok:lombok")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testAnnotationProcessor("org.projectlombok:lombok")

    // ===== IA - LLM + agentes (Ollama no .201; chat + embedding) =====
    implementation("org.springframework.ai:spring-ai-starter-model-ollama")

    // ===== IA - retrieval (pgvector no .200) =====
    implementation("org.springframework.ai:spring-ai-starter-vector-store-pgvector")

    // ===== IA - RAG (QuestionAnswerAdvisor, RetrievalAugmentationAdvisor, etc.) =====
    implementation("org.springframework.ai:spring-ai-rag")

    // ===== IA - ingestao de documentos =====
    implementation("org.springframework.ai:spring-ai-pdf-document-reader")
    implementation("org.springframework.ai:spring-ai-tika-document-reader")

    // ===== IA - memoria =====
    // Na 2.0 use InMemoryChatMemoryRepository (no core) p/ dev, ou um
    // ChatMemoryRepository custom contra Redis/Valkey no .200 p/ producao.

    // ===== gRPC - cliente do motor de score deterministico (Rust/CUDA no .202) =====
    implementation("io.grpc:grpc-protobuf:1.82.0")
    implementation("io.grpc:grpc-stub:1.82.0")
    runtimeOnly("io.grpc:grpc-netty-shaded:1.82.0")
    compileOnly("org.apache.tomcat:annotations-api:6.0.53")  // p/ @Generated no codigo gerado
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.8"
    }
    plugins {
        // idempotente: nao re-cria o locator se o daemon reavaliar o script
        maybeCreate("grpc").artifact = "io.grpc:protoc-gen-grpc-java:1.82.0"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.plugins.maybeCreate("grpc")
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}