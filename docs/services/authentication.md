
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
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
```

:::

1. Build-Stage: Erstellt das JAR-File mit Gradle

- Verwendet das Template `gradle:8.11.1-jdk23` als Basisimage.
- Legt das Arbeitsverzeichnis auf `/tmp`.
- Kopiert die `build.gradle` und `settings.gradle` Dateien in das Image.
- Führt den Befehl `gradle dependencies` aus:
  - Installiert und cached die Projektabhängigkeiten.
  - Nutzt die Optionen `--no-daemon` und `--parallel`, um den Build zu beschleunigen.
- Kopiert den `src` Ordner in das Image.
- Führt den Gradle-Build-Prozess aus:
  - Erstellt das JAR-File.
  - Nutzt ebenfalls die Optionen `--no-daemon` und `--parallel`, um Ressourcen optimal zu nutzen.

2. Run-Stage: Verpackt das JAR-File in einem schlanken Image

- Verwendet das Template `eclipse-temurin:23-jre-alpine` als Basisimage.
- Installiert zusätzliche Tools für den Healthendpunkt `bash` und `curl` über `apk --no-cache add`.
- Setzt das Arbeitsverzeichnis auf `/app`.
- Kopiert das gebaute JAR-File aus dem `builder`-Stage nach `/app` und benennt es als `app.jar`.
- Öffnet den Port `8081`, um die Anwendung verfügbar zu machen.
- Legt den Entrypoint fest, um die Anwendung mit dem Befehl `java -jar app.jar` zu starten.

::: info Wofür ist das andere Dockerfile?
Das `Dockerfile.gh` dient dazu, das Multi-Arch Image effizienter mit GitHub Action zu bauen. Nähere Infos lassen sich [hier](/usage/ci) entnehmen.
:::

