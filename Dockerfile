# Stage 1: Build
FROM maven:3.8-eclipse-temurin-17 AS builder

# Set working directory
WORKDIR /app

# Copy pom.xml first (for better layer caching)
COPY pom.xml .

# Download dependencies first (for better layer caching)
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the application with debug output
RUN mvn clean package -DskipTests -X

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-jammy

# Set working directory
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/target/ehd-backend-*.jar app.jar

# Expose the application port
EXPOSE 8080

# Set environment variables with defaults
ENV DB_URL=jdbc:postgresql://localhost:5432/ehd_db
ENV DB_USERNAME=postgres
ENV DB_PASSWORD=postgres
ENV JWT_SECRET=your-secret-key

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"] 