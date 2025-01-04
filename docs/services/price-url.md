# Preisüberwachungs

<!-- Content for Preisüberwachungs -->

Der Preisüberwachungs-Service ist ein Service zum Abfragen von Objektpreisen.
Er bietet die Möglichkeit den aktuellen Preis sowie die Verfügbarkeit eines Objektes abzufragen und in der Datenbank zu speichern.

- **Autor**: Leonie Rößler
- **Architektur**: Hexagonal
- **Technologie**: Quarkus (Java)

## Architektur Beschreibung

Viele Adapter. Ports. Wundervolle Dinge. 
Aufruf des Url Validierungs Service. Wow! 

## API

| Method | Path | Description |
| --- | --- | --- |
| POST | [item/check-price] | Save Price and ... of Object |
| GET | [/something] | check url of Object?  |
| POST | [/item/bulk-check] | check-price for JSON List of Item Ids |

