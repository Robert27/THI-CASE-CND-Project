# Objekt Management Service

Der Objekt Management Service ist ein Service zur Verwaltung von Objekten.
Er bietet die Möglichkeit je Nutzer Objekte zu erstellen, bearbeiten und löschen.

- **Autor**: Robert Eggl
- **Architektur**: Hexagonal
- **Technologie**: Quarkus (Java)

## Architektur Beschreibung

Der Objekt Management Service ist ein Service zur Verwaltung von Objekten.

## API

| Method | Path | Description |
| --- | --- | --- |
| GET | [/category](#getcategory) | Find all available categories |
| GET | [/object](#getobject) | Find all objects of an authenticated user |
| POST | [/object](#postobject) | Create a new object for an authenticated user |
| PUT | [/object/{id}](#putobjectid) | Update an object for an authenticated user |

