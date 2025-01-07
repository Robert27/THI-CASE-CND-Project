FROM gradle:8.11.1-jdk23 AS builder

WORKDIR /tmp
COPY build.gradle /tmp
COPY settings.gradle /tmp
RUN gradle dependencies --no-daemon
COPY src /tmp/src/
RUN gradle build -x test --no-daemon

FROM openjdk:23-jdk-slim AS app
WORKDIR /app
COPY --from=builder /tmp/build/libs/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]