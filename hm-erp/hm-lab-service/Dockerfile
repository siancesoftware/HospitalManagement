# Build from the REPOSITORY ROOT:
#   docker build -f hm-lab-service/Dockerfile -t hm-lab-service:local .

FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
COPY hm-common ./hm-common
COPY hm-security ./hm-security
COPY hm-lab-service ./hm-lab-service
RUN --mount=type=cache,target=/root/.m2 \
    mvn -q -pl hm-lab-service -am -DskipTests package

FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S hm && adduser -S hm -G hm
WORKDIR /app
COPY --from=build /workspace/hm-lab-service/target/hm-lab-service.jar app.jar
USER hm
EXPOSE 8202
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
