plugins {
    java
    id("org.springframework.boot") version "4.1.0"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.graalvm.buildtools.native") version "1.1.1"
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

dependencyManagement {
    imports {
        mavenBom("org.springframework.ai:spring-ai-bom:${property("springAiVersion")}")
    }
}

dependencies {
    // ===== DEPENDÊNCIAS ORIGINAIS =====
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

    // ===== IA — LLM + AGENTES (Ollama no .201; chat + embedding) =====
// ===== IA — LLM + AGENTES (Ollama no .201) =====
    implementation("org.springframework.ai:spring-ai-starter-model-ollama")

    // ===== IA — RETRIEVAL (pgvector no .200) =====
    implementation("org.springframework.ai:spring-ai-starter-vector-store-pgvector")

    // ===== IA — RAG (traz QuestionAnswerAdvisor, RetrievalAugmentationAdvisor, etc.) =====
    implementation("org.springframework.ai:spring-ai-rag")

    // ===== IA — INGESTÃO DE DOCUMENTOS =====
    implementation("org.springframework.ai:spring-ai-pdf-document-reader")
    implementation("org.springframework.ai:spring-ai-tika-document-reader")

    // ===== IA — MEMÓRIA =====
    // Na 2.0 use InMemoryChatMemoryRepository (no core) p/ dev, ou um
    // ChatMemoryRepository custom contra Redis/Valkey no .200 p/ produção.

    // REMOVIDO: spring-ai-starter-model-transformers
    //   → Você foi de Ollama (nomic-embed-text). O embedding ONNX local virou
    //     dependência morta E gera um segundo bean EmbeddingModel (ambiguidade).
}

tasks.withType<Test> {
    useJUnitPlatform()
}
