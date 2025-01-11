# Objekt Management Service

Der Objekt Management Service ist ein Service zur Verwaltung von Objekten.
Er bietet die Möglichkeit je Nutzer Objekte zu erstellen, bearbeiten und löschen.

- **Autor**: Robert Eggl
- **Architektur**: Hexagonal
- **Technologie**: Quarkus (Java)

## Architektur Beschreibung

Der Objekt Management Service ist ein Service zur Verwaltung von Objekten.

## REST API

| Method | Path                            | Description                                                          |
| ------ | ------------------------------- | -------------------------------------------------------------------- | --- |
| GET    | [/category](#getcategory)       | Alle verfügbaren Kategorien abrufen                                  |
| GET    | [/object](#getobject)           | Alle Objekte für einen authentifizierten Nutzer abrufen              |     |
| POST   | [/object](#postobject)          | Ein neues Objekt für einen authentifizierten Nutzer erstellen        |
| PATCH  | [/object/{id}](#putobjectid)    | Ein bestehendes Objekt für einen authentifizierten Nutzer bearbeiten |
| DELETE | [/object/{id}](#deleteobjectid) | Ein bestehendes Objekt für einen authentifizierten Nutzer löschen    |

## gRPC

### ObjectService

| Method                 | Request Type            | Response Type       | Description                                   |
| ---------------------- | ----------------------- | ------------------- | --------------------------------------------- |
| GetStorageObjectsByIds | StorageObjectIdsRequest | StorageObjectsReply | Abrufen von Speicherobjekten anhand ihrer IDs |
| FindAllDayUsers        | DayUsersRequest         | DayUsersReply       | Finden aller Tagesnutzer                      |
