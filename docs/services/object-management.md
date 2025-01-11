# Objekt Management Service

Der Objekt Management Service ist ein Service zur Verwaltung von Objekten.
Er bietet die Möglichkeit je Nutzer Objekte zu erstellen, bearbeiten und löschen.

- **Autor**: Robert Eggl
- **Architektur**: Hexagonal
- **Technologie**: Quarkus (Java)

## Architektur Beschreibung

Der Objekt Management Service ist ein Service zur Verwaltung von Objekten.

### REST API

| Method | Path                            | Description                                                          |
| ------ | ------------------------------- | -------------------------------------------------------------------- |
| GET    | [/category](#getcategory)       | Alle verfügbaren Kategorien abrufen                                  |
| GET    | [/object](#getobject)           | Alle Objekte für einen authentifizierten Nutzer abrufen              |
| POST   | [/object](#postobject)          | Ein neues Objekt für einen authentifizierten Nutzer erstellen        |
| PATCH  | [/object/{id}](#putobjectid)    | Ein bestehendes Objekt für einen authentifizierten Nutzer bearbeiten |
| DELETE | [/object/{id}](#deleteobjectid) | Ein bestehendes Objekt für einen authentifizierten Nutzer löschen    |

### gRPC

#### ObjectService

| Method                 | Request Type            | Response Type       | Description                                   |
| ---------------------- | ----------------------- | ------------------- | --------------------------------------------- |
| GetStorageObjectsByIds | StorageObjectIdsRequest | StorageObjectsReply | Abrufen von Speicherobjekten anhand ihrer IDs |
| FindAllDayUsers        | DayUsersRequest         | DayUsersReply       | Finden aller Nutzer-Objekte eines Tages       |

## Architketur Skizze

tbd

## Start ohne Docker

Der Service kann ohne Docker gestartet werden. Dazu muss die Anwendung lokal gebaut und gestartet werden. Allerdings erfordert dieser Dienst die Abhängigkeit zum Nutzer-Management-Service, der URL Validierung und der Datenbank.
Die Schritte der anderen Services müssen daher ebenfalls ausgeführt werden, andernfalls wird der Service nicht ordnungsgemäß funktionieren.

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
