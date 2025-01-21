# URL-Validierung

<!-- Content for Preisüberwachungs -->

Der URL-Validierungs-Service ist ein Service zur Validierung von URLs.

- **Autor**: Leonie Rößler
- **Architektur**: -
- **Technologie**: Quarkus (Java)

## Architektur Beschreibung

Der URL-Validierungs-Service nimmt eine URL über gRPC entgegen prüft diese auf Validität. 
Die Antwort auf die gRPC-Anfrage beinhaltet Informationen über die Validität und Erreichbarkeit der URL sowie eine Statusmeldung. 
