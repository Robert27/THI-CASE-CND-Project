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
