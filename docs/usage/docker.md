# Docker

## Docker Compose

### Lokales bauen und starten

Dem Projekt liegt eine `docker-compose.yml` bei, die alle Services des Projekts baut und startet.

```sh
docker-compose up
```

### Vorgebaute Container verwenden

Alternativ können die vorgebauten Container aus der GitHub Container Registry verwendet werden. Diese werden automatisch beim Pushes des jeweiligen Service Ordners gebaut und veröffentlicht. Die selben Zugriffsrechte wie für das Repository sind notwendig.

```sh
docker login ghcr.io
docker-compose -f docker-compose-cloud.yml up
```

::: tip Hinweis
Zur Authentifizierung wird ein GitHub Personal Access Token (classic) benötigt. Dieser wird beim `docker login` abgefragt und muss statt des Passworts eingegeben werden.
:::
