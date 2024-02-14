# Stage 1: Build the application
FROM maven:3.8.4-openjdk-17 AS scm_backend_builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create a minimal JRE image
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=scm_backend_builder /app/target/smartcontact-0.0.1-SNAPSHOT.jar mss-scm.jar

EXPOSE 8080

# Specify the default command to run on startup
CMD ["java", "-jar", "mss-scm.jar"]