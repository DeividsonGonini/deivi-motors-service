# =========================
# 1️⃣ BUILD STAGE
# =========================
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom.xml files first for Maven cache
COPY pom.xml .

# Copy source code
COPY src ./src

# Run build project
RUN mvn clean package -q -DskipTests

# =========================
# 2️⃣ RUNTIME STAGE
# =========================
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
