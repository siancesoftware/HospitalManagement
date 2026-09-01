# Build from the REPOSITORY ROOT (multi-module reactor - hm-common/hm-security
# must be built first so hm-auth-service can resolve them):
#   docker build -f hm-auth-service/Dockerfile -t hm-auth-service:local .
# docker-compose.yml at the repo root already does this for you.

FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
COPY hm-common ./hm-common
COPY hm-security ./hm-security
COPY hm-auth-service ./hm-auth-service
RUN --mount=type=cache,target=/root/.m2 \
    mvn -q -pl hm-auth-service -am -DskipTests package

FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S hm && adduser -S hm -G hm
WORKDIR /app
COPY --from=build /workspace/hm-auth-service/target/hm-auth-service.jar app.jar
USER hm
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
