# ========================================
# 🏗️ Etapa 1 — Build do projeto com Maven
# ========================================
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

# Copia o pom.xml e baixa dependências (cache)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o código-fonte e compila
COPY src ./src
RUN mvn clean package -DskipTests

# ==========================================
# 🚀 Etapa 2 — Runtime leve com Java 21 JRE
# ==========================================
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copia apenas o .jar gerado na etapa de build
COPY --from=builder /app/target/*.jar app.jar

# Porta padrão do Spring Boot
EXPOSE 8080

# Variável de ambiente opcional
ENV SPRING_PROFILES_ACTIVE=docker

# Executa o aplicativo
ENTRYPOINT ["java", "-jar", "app.jar"]
