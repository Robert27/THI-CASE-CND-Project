# Docker Compose

Neben den einzelnen Backend Services, dem Frontend und dieser Dokumentation enthält das `docker-compose.yml` Setup auch folgende Services:

- PostgreSQL Datenbank
- NGINX als Reverse Proxy
  Beim Startup werden alle Services gebaut / gepulled und gestartet.

Die Datenbank Migration wird durch ein `schema.sql` Script initialisiert, das beim Starten der Datenbank ausgeführt wird. Dadurch entfällt der Bedarf an einem zusätzlichen Migrationsservice oder einer Migration je Service.

NGINX leitet die Anfragen an die entsprechenden Services weiter, sodass alle Backend Services und das Frontend über den gemeinsamen `Port 4000` erreichbar sind, die einzelnen Pfade sind der jeweiligen Service Dokumentation zu entnehmen.

- Frontend: `http://localhost:4000`
- Docs: `http://localhost:4000/docs`
- APIs: `http://localhost:4000/rest/<service_name>/<path>`

## Umgebungsvariablen

Die vertraulichen Umgebungsvariablen sind in einer `.env` Datei im Root des Projekts abgelegt. Diese Datei wird von Docker Compose beim Starten der Services eingelesen. Es wird empfohlen, für die Produktion eigene Umgebungsvariablen zu verwenden.
Nähere Informationen dazu finden Sie in der [Konfigurationsdokumentation](/usage/configuration).

## Vorgebaute Container verwenden

Die vorgebauten Container aus der GitHub Container Registry verwendet werden. Diese werden automatisch beim Pushes des jeweiligen Service Ordners gebaut und veröffentlicht. Die selben Zugriffsrechte wie für das Repository sind notwendig.
Mehr Informationen dazu, siehe [Bauautomarisierung](/usage/ci).

```sh
docker login ghcr.io
docker compose up
```

::: tip Hinweis
Zur Authentifizierung wird ein GitHub Personal Access Token (classic) benötigt. Dieser wird beim `docker login` abgefragt und muss statt des Passworts eingegeben werden.
:::

## Lokales bauen und starten

Dem Projekt liegt eine `docker-compose.dev.yml` bei, die alle Services des Projekts lokal baut und startet. Für besseres Debugging exposed sie alle Ports der Services, daher ist es nicht empfohlen, diese in Produktivumgebungen zu verwenden. Für alle Funktionen, wie Health-Checks, sollte die normale `docker-compose.yml` verwendet werden.

```sh
docker compose -f docker-compose.dev.yml up --build
```
