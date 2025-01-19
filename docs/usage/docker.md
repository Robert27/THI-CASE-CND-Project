# Docker

## Docker Compose

Neben den einzelnen Backend Services und dem Frontend enthält das Docker Compose Setup auch eine PostgreSQL Datenbank und NGINX als Reverse Proxy. Beim Startup werden alle Services gebaut und gestartet. Mit einem Skirpt wird die Datenbank initialisiert und die Migrationen ausgeführt.

NGINX leitet die Anfragen an die entsprechenden Services weiter, sodass alle Backend Services über den gemeinsamen `Port 4000` erreichbar sind, die einzelnen Pfade sind der jeweiligen Service Dokumentation zu entnehmen. Das Frontend ist über `Port 3000` erreichbar.

### Umgebungsvariablen

Die vertraulichen Umgebungsvariablen sind in einer `.env` Datei im Root des Projekts abgelegt. Diese Datei wird von Docker Compose beim Starten der Services eingelesen. Es wird empfohlen, für die Produktion eigene Umgebungsvariablen zu verwenden.

````sh
### Lokales bauen und starten

Dem Projekt liegt eine `docker-compose.dev.yml` bei, die alle Services des Projekts baut und startet.

```sh
docker compose -f docker-compose.dev.yml up --build
````

### Vorgebaute Container verwenden

Alternativ können die vorgebauten Container aus der GitHub Container Registry verwendet werden. Diese werden automatisch beim Pushes des jeweiligen Service Ordners gebaut und veröffentlicht. Die selben Zugriffsrechte wie für das Repository sind notwendig.

```sh
docker login ghcr.io
docker compose up
```

::: tip Hinweis
Zur Authentifizierung wird ein GitHub Personal Access Token (classic) benötigt. Dieser wird beim `docker login` abgefragt und muss statt des Passworts eingegeben werden.
:::
