# Preisüberwachungs

<!-- Content for Preisüberwachungs -->

Der Preisüberwachungs-Service ist ein Service zum Abfragen von Objektpreisen.
Er bietet die Möglichkeit den aktuellen Preis sowie die Verfügbarkeit eines Objektes abzufragen und in der Datenbank zu speichern.

- **Autor**: Leonie Rößler
- **Architektur**: Hexagonal
- **Technologie**: Quarkus (Java)

## Architektur Beschreibung

Der Preischeck-Service ist ein gRPC-basierter Microservice, der es ermöglicht, Preise und Verfügbarkeiten für eine Liste von Artikeln zu überprüfen. Der Service empfängt Anfragen mit einer Liste von Artikel-IDs und gibt entsprechende Ergebnisse zurück, einschließlich Preis, Verfügbarkeit und Status.
Der Service erwartet eine gRPC-Anfrage (PriceCheckRequest) mit einer Liste von Artikel-IDs (itemIds).
Kontaktiert Object Management Service um URLs zu erhalten 
Kontaktiert URL Validation Service um zu prüfen ob URLs valide sind
REST Aufruf der URL, entnimmt Preis, Verfügbarkeit
Der Service gibt eine Antwort (PriceCheckReply) mit einer Liste von Ergebnissen (PriceResult) zurück. Jedes Ergebnis enthält Details zu einem Artikel.


Adapter:
Inbound
gRPC: checkPrices 
(REST: checkPriceByUrl, checkPriceById, getLogById)

Outbound
gRPC: ObjectManagement, UrlValidation
jpa: savePriceLog
REST: checkPrice

Ports:
ObjectManagementPort
PriceCheckPort
PriceLogRepository
URLValidationPort

Domain:
ItemPriceService
Model:
PriceLog
BulkCheckResult




