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

## Abhängigkeiten

Die `depends_on`-Funktionen sind so konfiguriert, dass die Services in der richtigen Reihenfolge gestartet werden. Die Datenbank wird zuerst gestartet, gefolgt von den Backend Services, NGINX und dem Frontend.
Die Services sind so konfiguriert, dass sie warten, bis die Datenbank bereit ist und die Dienste mit den jeweils verwendeten gRPC-Servers erfolgreich ihre healtchecks durchgeführt haben.
Dadurch verzögert sich der gesamte Startvorgang, allerdings wird dadurch sichergestellt, dass alle Services korrekt gestartet sind und es zu keinen Fehlern kommt.

## Ressourcen

Ebenso sind exemplarisch Ressourcenlimits und Reservierungen für die Services konfiguriert. Diese können in der `docker-compose.yml` entnommen und angepasst werden.

```yaml
deploy:
  resources:
    limits:
      cpus: "0.5"
      memory: "512M"
    reservations:
      cpus: "0.25"
      memory: "256M"
```

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

## CI/CD

Das dritte Docker Compose File `docker-compose.slim.yml` war für das Deployment auf einem leistungsschwachen System gedacht. Es verwendet die vorgebauten Container und stellt keine Mindestanforderungen an die Ressourcen, auch auf time-outs wurde verzichtet.
Ziel war es die Anwendung auf https://smartorder.eggl.dev zu deployen, allerdings war die Performance des Servers trotz des angepassten Docker Compose Files zu schwach, um alle Container zu starten.
