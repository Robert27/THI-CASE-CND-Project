# Preisüberwachungs

<!-- Content for Preisüberwachungs -->

Der Preisüberwachungs-Service ist ein Service zum Abfragen von Objektpreisen.
Er bietet die Möglichkeit den aktuellen Preis sowie die Verfügbarkeit eines Objektes abzufragen und in der Datenbank zu speichern.

- **Autor**: Leonie Rößler
- **Architektur**: Hexagonal
- **Technologie**: Quarkus (Java)

## Architektur Beschreibung

Der Preischeck-Service ist ein gRPC-basierter Microservice, der es ermöglicht, Preise und Verfügbarkeiten für eine Liste von Artikeln zu überprüfen.  
Der Service empfängt Anfragen mit einer Liste von Artikel-IDs und gibt entsprechende Ergebnisse zurück, einschließlich Preis, Verfügbarkeit und Status.

### Ablauf:

1. Der Service erwartet eine gRPC-Anfrage (`PriceCheckRequest`) mit einer Liste von Artikel-IDs (`itemIds`).
2. Um die Artikel-URLs zu erhalten, wird der **Object Management Service** mit einer entsprechenden Anfrage kontaktiert.
3. Die URLs werden anschließend über den **Validation Service** validiert.
4. Bei positiver Rückmeldung wird die URL (Mock-Server) kontaktiert, um Preis und Verfügbarkeit zu erhalten.
5. Der Service gibt eine Liste von Ergebnissen zurück. Jedes Ergebnis enthält Details zu einem Artikel.


## Adapter

### Inbound:
- **gRPC**: `checkPrices`
- **REST**:
    - `checkPriceByUrl`
    - `checkPriceById`
    - `getLogById`

### Outbound:
- **gRPC**:
    - `ObjectManagement`
    - `UrlValidation`
- **JPA**: `savePriceLog`
- **REST**: `checkPrice`

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

