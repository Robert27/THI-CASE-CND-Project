# URL-Validierung

Der URL-Validierungs-Service ist ein Service zur Validierung von URLs.

- **Autorin**: Leonie Rößler
- **Architektur**: -
- **Technologie**: Quarkus (Java)

---

## Beschreibung

Der URL-Validierungs-Service nimmt eine URL über gRPC entgegen prüft diese auf Validität.
Die Antwort auf die gRPC-Anfrage beinhaltet Informationen über die Validität und Erreichbarkeit der URL sowie eine Statusmeldung.

---

## Installation und Start in einer VM

### Voraussetzungen:

- **Betriebssystem:** Linux-basierte Distribution (z. B. Ubuntu, CentOS)
- **Vorinstallierte Software:**
  - Java 21 JDK (z. B. OpenJDK)
  - Maven 3.9.9 oder höher
- **Zugriff auf Internet:** Für Maven-Repositories

### Installation:

1. Klonen Sie das Repository und wechseln Sie in den Projektordner
   ```bash
   git clone https://github.com/roberteggl/THI-CASE-CND-Projekt.git
   cd THI-CASE-CND-Projekt/services/url-validator
   ```
2. Installieren Sie die Abhängigkeiten und bauen Sie das Projekt

   ```bash
   mvn package
   ```

   Nach erfolgreichem Build wird die ausführbare Datei im Verzeichnis `target` erstellt.

3. Starten Sie den Dienst mit:
   ```bash
   java -Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager -jar target/quarkus-app/quarkus-run.jar
   ```
   Parameter-Erklärung:

- `-Dquarkus.http.host=0.0.0.0`: Setzt die Bind-Adresse des HTTP-Servers auf `0.0.0.0`. Ermöglicht den Zugriff auf den Dienst von allen Netzwerkadressen, nicht nur localhost.
- `-Djava.util.logging.manager=org.jboss.logmanager.LogManager`: Setzt den Log-Manager auf `org.jboss.logmanager`, der von Quarkus verwendet wird.
- `-jar target/quarkus-app/quarkus-run.jar`: Startet die Anwendung aus der Quarkus-Build-Struktur.

Anschließend ist die Anwendung über `http://localhost:8085` erreichbar.

---

## Dockerfile

Das Dockerfile des Order-List-Service umfasst die folgenden Schritte:

1.  Das Maven Image als Build-Stage Image "builder" definieren:
    ```dockerfile
    FROM maven:3.9.9-eclipse-temurin-21 AS builder
    ```
2. "/app" als Arbeitsverzeichnes innerhalb des Container-Filesystems definieren:

    ```dockerfile
    WORKDIR /app
    ```
3. Kopieren der Projektdateien in den Container:
   ```dockerfile
    COPY pom.xml ./
    COPY src ./src
    ```
4. Installation von Abhängigkeiten:
   ```dockerfile
    RUN mvn package
    ```
5. Festlegen des Basis Image für die Laufzeitumgebung:
   ```dockerfile
    FROM registry.access.redhat.com/ubi8/openjdk-21:1.20
    ```
6.  Sprache des Containers auf Englisch setzen
    ```dockerfile
    ENV LANGUAGE='en_US:en'
    ```
7.  Dateien und Verzeichnisse aus dem Build-Stage "builder" in das aktuelle Image kopieren:
    ```dockerfile
    COPY --from=builder /app/target/quarkus-app/lib/ /deployments/lib/
    COPY --from=builder /app/target/quarkus-app/*.jar /deployments/
    COPY --from=builder /app/target/quarkus-app/app/ /deployments/app/
    COPY --from=builder /app/target/quarkus-app/quarkus/ /deployments/quarkus/
    ```
7.  Dokumentieren, dass der Container auf Port 8085 lauscht und Benutzer, unter dem der Container ausgeführt wird, auf die Benutzer-ID 185 (nicht root) setzen:
    ```dockerfile
    EXPOSE 8085
    USER 185
    ```
8.  Konfigurieren von Parametern und Dateiverzeichnis zum Starten der Java-Anwendung:
    ```dockerfile
    ENV JAVA_OPTS_APPEND="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
    ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"
    ```
9.  Starten der Anwendung mit einem Startskript:
    ```dockerfile
    ENTRYPOINT [ "/opt/jboss/container/java/run/run-java.sh" ]
    ```

---