# Preisüberwachung

Der Preisüberwachungs-Service ist ein Service zum Abfragen von Objektpreisen.
Er bietet die Möglichkeit den aktuellen Preis sowie die Verfügbarkeit eines Objektes abzufragen und in der Datenbank zu speichern.

- **Autorin**: Leonie Rößler
- **Architektur**: Hexagonal
- **Technologie**: Quarkus (Java)

## Architektur Beschreibung

Der Preischeck-Service ist ein gRPC-basierter Microservice, der es ermöglicht, Preise und Verfügbarkeiten für eine Liste von Artikeln zu überprüfen.  
Der Service empfängt Anfragen mit einer Liste von Artikel-IDs und gibt entsprechende Ergebnisse zurück, einschließlich Preis, Verfügbarkeit und Status.

### Ablauf:

1. Der Service erwartet eine gRPC-Anfrage (`PriceCheckRequest`) mit einer Liste von Artikel-IDs (`itemIds`).
2. Um die Artikel-URLs zu erhalten, wird der **Object-Management-Service** mit einer entsprechenden Anfrage kontaktiert.
3. Die URLs werden anschließend über den **URL-Validation-Service** validiert.
4. Bei positiver Rückmeldung wird die URL (Mock-Server) kontaktiert, um Preis und Verfügbarkeit zu erhalten.
5. Der Service gibt eine Liste von Ergebnissen zurück. Jedes Ergebnis enthält Details zu einem Artikel.


---

## Inbound Adapter

#### REST-Endpunkte

**Hinweis:** Einige der definierten Endpunkte existieren, werden jedoch derzeit nicht genutzt.


| HTTP-Methode | Pfad               | Beschreibung                        | Status      |
|--------------|--------------------|-------------------------------------|-------------|
| `POST`       | `/check-price/url` | Überprüft den Preis eines Artikels anhand einer URL | **Nicht genutzt** |
| `POST`       | `/check-price/id`  | Überprüft den Preis eines Artikels anhand seiner ID  | **Nicht genutzt** |
| `GET`        | `/get-log/{logId}` | Ruft ein Preisprotokoll anhand der Log-ID ab       | **Nicht genutzt** |
- **REST Endpunkte (`GetItemResource`):**
    - Aktuell definiert, jedoch nicht in der Anwendung aktiv genutzt. Können in zukünftigen Erweiterungen genutzt werden (z.B. für das Abrufen älterer Preislogs für einen Preisverlauf).



#### gRPC-Endpunkte
| RPC-Methode    | Beschreibung                                     | Status          |
|----------------|--------------------------------------------------|-----------------|
| `checkPrices`  | Überprüft die Preise mehrerer Artikel anhand ihrer IDs | **Aktiv genutzt** |
- **gRPC Service (`PriceCheckGrpcService`):**
    - Aktiv genutzt zur gleichzeitigen Überprüfung der Preise mehrerer Artikel anhand ihrer Ids. 
## Outbound Adapter

| Adapter                      | Typ          | Methode              | Beschreibung                                                  | Status   |
|------------------------------|--------------|----------------------|---------------------------------------------------------------|----------|
| `PriceCheckAdapter`          | HTTP Adapter | `checkPrice`         | Führt einen HTTP GET-Request zur Preisüberprüfung aus        | **Aktiv genutzt**    |
| `URLValidationGrpcClient`    | gRPC Client  | `validateUrl`        | Validiert URLs über einen externen gRPC-Service               | **Aktiv genutzt** |
| `ObjectManagementGrpcClient` | gRPC Client  | `getReorderUrlsByIds` | Ruft Reorder-URLs für Artikel über einen externen gRPC-Service ab | **Aktiv genutzt** |

- **HTTP Adapter (`PriceCheckAdapter`):**
    - Dient zur externen Preisüberprüfung und stellt sicher, dass die Preis- und Verfügbarkeitsdaten aktuell sind.

- **gRPC Clients (`URLValidationGrpcClient`, `ObjectManagementGrpcClient`):**
    - Kommuniziert mit anderen Diensten zur URL-Validierung und zur Verwaltung von Reorder-URLs.


### Datenbankzugriffe

| Adapter              | Typ         | Methode        | Beschreibung                                      | Status  |
|----------------------|-------------|----------------|---------------------------------------------------|---------|
| `PriceLogRepository` | JPA/Panache | `savePriceLog` | Speichert ein `PriceLog` in der Datenbank         | **Aktiv genutzt**   |
| `PriceLogRepository` | JPA/Panache  | `findById`     | Findet ein `PriceLog` anhand der Log-ID            | **Nicht genutzt**   |
- **`savePriceLog`:** Wird verwendet, um neue Preisprotokolle in der Datenbank zu speichern, wodurch eine Historie der Preisüberprüfungen erstellt wird.
- **`findById`:** Ermöglicht das Abrufen spezifischer Preisprotokolle anhand ihrer ID, was für Nachverfolgung und Analysezwecke nützlich ist.


---
## Ports

- `ObjectManagementPort`
- `PriceCheckPort`
- `PriceLogRepository`
- `URLValidationPort`

---

## Domain

### Service:
- `ItemPriceService`

### Models:
- `PriceLog`
- `BulkCheckResult`





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
   cd THI-CASE-CND-Projekt/services/get-item-info
   ```
2. Installieren Sie die Abhängigkeiten und bauen Sie das Projekt
   ```bash
   mvn package
   ```
   Nach erfolgreichem Build wird die ausführbare Datei im Verzeichnis target erstellt.

3. Starten Sie den Dienst mit:
   ```bash
   java -Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager -jar target/quarkus-app/quarkus-run.jar
   ```
Parameter-Erklärung:
- `-Dquarkus.http.host=0.0.0.0`: Setzt die Bind-Adresse des HTTP-Servers auf `0.0.0.0`. Ermöglicht den Zugriff auf den Dienst von allen Netzwerkadressen, nicht nur localhost.
- `-Djava.util.logging.manager=org.jboss.logmanager.LogManager`: Setzt den Log-Manager auf `org.jboss.logmanager`, der von Quarkus verwendet wird.
- `-jar target/quarkus-app/quarkus-run.jar`: Startet die Anwendung aus der Quarkus-Build-Struktur.

Anschließend ist die Anwendung über `http://localhost:808` erreichbar. 