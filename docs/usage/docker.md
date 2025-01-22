# Docker Compose

Neben den einzelnen Backend Services und dem Frontend enthält das Docker Compose Setup auch eine PostgreSQL Datenbank und NGINX als Reverse Proxy. Beim Startup werden alle Services gebaut und gestartet. Mit einem Skirpt wird die Datenbank initialisiert und die Migrationen ausgeführt.

NGINX leitet die Anfragen an die entsprechenden Services weiter, sodass alle Backend Services und das Frontend über den gemeinsamen `Port 4000` erreichbar sind, die einzelnen Pfade sind der jeweiligen Service Dokumentation zu entnehmen.

- Frontend: `http://localhost:4000`
- Docs: `http://localhost:4000/docs`
- APIs: `http://localhost:4000/rest/<service>/<path>`

## Umgebungsvariablen

Die vertraulichen Umgebungsvariablen sind in einer `.env` Datei im Root des Projekts abgelegt. Diese Datei wird von Docker Compose beim Starten der Services eingelesen. Es wird empfohlen, für die Produktion eigene Umgebungsvariablen zu verwenden.

## Vorgebaute Container verwenden

Die vorgebauten Container aus der GitHub Container Registry verwendet werden. Diese werden automatisch beim Pushes des jeweiligen Service Ordners gebaut und veröffentlicht. Die selben Zugriffsrechte wie für das Repository sind notwendig.

```sh
docker login ghcr.io
docker compose up
```

::: tip Hinweis
Zur Authentifizierung wird ein GitHub Personal Access Token (classic) benötigt. Dieser wird beim `docker login` abgefragt und muss statt des Passworts eingegeben werden.
:::

## Lokales bauen und starten

Dem Projekt liegt eine `docker-compose.dev.yml` bei, die alle Services des Projekts lokal baut und startet. Für besseres Debugging exposed sie alle Ports der Services, daher ist es nicht empfohlen, diese in Produktivumgebungen zu verwenden.

```sh
docker compose -f docker-compose.dev.yml up --build
```
