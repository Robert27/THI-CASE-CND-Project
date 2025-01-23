
# Authentifizierung

- **Autor**: Andreas Ziegltrum
- **Architektur**: Hexagonal
- **Technologie**: Spring (Java)

## Architektur Beschreibung

Der Microsrvice Authentifizierung ist verandwortlich für den Login von Usern und damit zur vergabe von JWT zur Authentifizierung von späteren Anfragen.

## Rest API

| Method | Path                            | Description                                                          |
| ------ | ------------------------------- | -------------------------------------------------------------------- |
| POST   | [/login](#postlogin)            | Anmelden mit bereits bestehenden Nutzer um JWT zu erhalten           |
| GET    | [/actuator/health](#gethealth)  | Health Endpunkt                                                      |


## Dockerfile

Das Dockerfile für den Authentifizierungs Service ist in `.\Dockerfile`.

Es verwendet Multi-Stage Builds, um das JAR-File zu erstellen und anschließend in einem schlanken Image zu verpacken.

::: code-group

```dockerfile [Build-Stage]
FROM gradle:8.11.1-jdk23 AS builder

WORKDIR /tmp
COPY build.gradle /tmp
COPY settings.gradle /tmp
RUN gradle dependencies --no-daemon --parallel
COPY src /tmp/src/
RUN gradle build --no-daemon --parallel
```

```dockerfile [Run-Stage]
FROM eclipse-temurin:23-jre-alpine

RUN apk --no-cache add bash curl
WORKDIR /app
COPY --from=builder /tmp/build/libs/*.jar /app/app.jar
EXPOSE 8080
EXPOSE 9091
ENTRYPOINT ["java", "-jar", "app.jar"]
```

:::