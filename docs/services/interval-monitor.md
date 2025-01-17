# Intervall Monitoring

Der Intervall Monitoring Service dient der Überwachung des Bestellzyklus.
Der Service wird in einem festen Intervall ausgeführt und prüft, ob eine Bestellung an dem aktuellen Tag fällig ist. Ist dies der Fall, wird der Bestellservice darüber informiert.

- **Autor**: Robert Eggl
- **Architektur**: n/a
- **Technologie**: Quarkus (Java)

## Architekturbeschreibung

Der **Intervall-Monitoring-Service** ist ein Dienst zur Überwachung des Bestellzyklus.

### Funktionsweise

Der Service arbeitet in folgenden Schritten:

1. Abruf aller Nutzer-IDs vom **Nutzerverwaltungsservice**.
2. Verarbeitung der Nutzer in 5er-Batches zur Lastverteilung.
3. Für jeden Batch: Abruf der zugehörigen Objekt-IDs vom **Objekt-Management-Service**. Dabei wird direkt der aktuelle Tag übergeben, um nur die Objekte zu erhalten, die an diesem Tag fällig sind.
4. Prüfung jedes Nutzers auf fehlende Objekte durch Abgleich mit der Log-Tabelle.
5. Bei fehlenden Objekten: Benachrichtigung des Bestellservices und Aktualisierung der Log-Tabelle

### Die Rolle der Log-Tabelle

Die Log-Tabelle erfüllt mehrere wichtige Funktionen:

1. **Idempotenz**: Auch wenn der Bestellservice doppelte Bestellungen ablehnt, stellt die Log-Tabelle eine zusätzliche Sicherheitsschicht dar
2. **Performance**: Schnelle lokale Prüfung ohne Netzwerkanfragen an den Bestellservice
3. **Audit-Trail**: Historische Nachverfolgung aller übergebenen Bestellungen
4. **Fehleranalyse**: Ermöglicht die Identifikation von Problemen im Bestellprozess

Diese Architektur gewährleistet Zuverlässigkeit und Effizienz bei der Verarbeitung der Bestellungen.

### REST API für Mock-Modus

Der Service bietet Endpunkte für einen Mock-Modus, der für Demonstrations- und Testzwecke verwendet werden kann.
Im Mock-Modus wird ein benutzerdefiniertes Datum für die Intervallprüfung verwendet und die Prüfung erfolgt alle 15 Sekunden statt alle 4 Stunden.

#### Endpunkte

| Method | Path          | Description                                           | Content-Type     |
| ------ | ------------- | ----------------------------------------------------- | ---------------- |
| GET    | /mock/enabled | Prüft, ob der Mock-Modus aktiviert ist                | application/json |
| GET    | /mock         | Ruft das aktuell konfigurierte Mock-Datum ab          | application/json |
| POST   | /mock/date    | Setzt ein neues Datum für den Mock-Modus (YYYY-MM-DD) | text/plain       |

> **Hinweis**: Der Mock-Modus ist nur für Entwicklungs- und Testzwecke gedacht und sollte in Produktivumgebungen deaktiviert sein. Es findet keine Überprüfung der Authentifizierung statt.

## Start ohne Docker

Der Service kann ohne Docker gestartet werden. Dazu muss die Anwendung lokal gebaut und gestartet werden. Allerdings erfordert dieser Dienst eine hohe Abhängigkeit von anderen Services und der Datenbank. Die Schritte der anderen Services müssen daher ebenfalls ausgeführt werden, andernfalls wird der Service nicht ordnungsgemäß funktionieren.

Gehen Sie wie folgt vor:

1. **Voraussetzungen**: Stellen Sie sicher, dass Maven und eine Java 21 JDK installiert sind.

2. **Projekt klonen**: Klonen Sie das Repository auf Ihren lokalen Rechner.

   ```sh
   git clone https://github.com/roberteggl/THI-CASE-CND-Projekt.git
   cd THI-CASE-CND-Projekt/interval-monitor
   ```

3. **Maven Build**: Führen Sie den Maven-Build aus, um die Anwendung zu erstellen.

   ```sh
   mvn package
   ```

4. **Anwendung starten**: Starten Sie die Anwendung mit dem folgenden Befehl:

   ```sh
   java -Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager -jar target/quarkus-app/quarkus-run.jar
   ```

5. **Zugriff auf die Anwendung**: Die Anwendung ist nun unter `http://localhost:8080` erreichbar.
