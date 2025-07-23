# =================================================================
# Stage 1: The "Builder" Stage
# This stage builds the application and creates the executable .jar file.
# =================================================================
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml file to download dependencies first.
# This takes advantage of Docker's layer caching. If the pom.xml doesn't change,
# the dependencies won't be re-downloaded on subsequent builds.
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the application's source code
COPY src ./src

# Build the application, skipping the tests for a faster build.
# The result will be a .jar file in the /app/target/ directory.
RUN mvn package -DskipTests


# =================================================================
# Stage 2: The "Runner" Stage
# This stage creates the final, small, and secure image that will
# actually run the application.
# =================================================================
FROM eclipse-temurin:21-jre-jammy

# Set the working directory inside the final container
WORKDIR /app

# Copy the .jar file that was created in the "builder" stage
# into this new stage. We rename it to app.jar for simplicity.
COPY --from=builder /app/target/*.jar app.jar

# Expose the port that the application runs on.
# For user-service, this is likely 8081.
EXPOSE 8082

# The command that will be executed when the container starts.
# This runs the Spring Boot application.
ENTRYPOINT ["java", "-jar", "app.jar"]
