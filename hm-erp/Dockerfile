# Multi-stage Dockerfile for HM-ERP Microservices
# Build: docker build --build-arg SERVICE_NAME=hm-patient-service -t siance/hm-patient-service .

ARG SERVICE_NAME=hm-patient-service

# Stage 1: Build
FROM eclipse-temurin:21-jdk-alpine AS builder
ARG SERVICE_NAME
WORKDIR /app
COPY . .
RUN ./mvnw -pl ${SERVICE_NAME} -am package -DskipTests -q

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine
ARG SERVICE_NAME
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
WORKDIR /app
COPY --from=builder /app/${SERVICE_NAME}/target/*.jar app.jar
RUN chown -R appuser:appgroup /app
USER appuser
EXPOSE 8080
ENV JAVA_OPTS="-XX:+UseG1GC -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
