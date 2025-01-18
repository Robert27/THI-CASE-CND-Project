FROM gradle:8.11.1-jdk23 AS builder

WORKDIR /tmp
COPY build.gradle /tmp
COPY settings.gradle /tmp
RUN gradle dependencies --no-daemon
COPY src /tmp/src/
RUN gradle build -x test --no-daemon

FROM eclipse-temurin:23-jre-alpine AS app
RUN apt install -y curl && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY --from=builder /tmp/build/libs/*.jar /app/app.jar
EXPOSE 8080
EXPOSE 9091
ENTRYPOINT ["java", "-jar", "app.jar"]