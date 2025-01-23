# Frontend

Das Dashboard ist die Benutzeroberfläche für die Verwaltung der Objekte und Bestellungen. Es bietet die Möglichkeit, Objekte zu erstellen, bearbeiten und löschen. Außerdem können aktive Bestellungen eingesehen und bearbeitet werden.
Natürlich bietet es auch die Möglichkeit, sich als Nutzer zu registrieren, einzuloggen und das Passwort zu ändern.

- **Technologie**: Next.js (React)
- **Autor**: Robert Eggl

## Authentifizierung

Das Frontend verwendet NextAuth.js für die Authentifizierung. Es bietet die Möglichkeit, sich mit einer User-Email und einem Passwort einzuloggen, wobei der erhaltene JWT-Token im Cookie gespeichert wird. Dieser wird für die Authentifizierung bei den API-Requests verwendet.

Die Umgebungsvariablen `DEPLOYMENT_TYPE` stellt sicher, dass bei verschiedenen Deployments der richtige Authentifizierungs-Cookie verwendet wird.
Dadurch wird sichergestellt, dass wenn `localhost` sowohl für die Dev-Umgebung, aber auch das Docker Compose Setup verwendet wird, der richtige Cookie verwendet wird und nicht noch der Nutzer angemeldet ist, obwohl sich Backend geändert hat.

## Verwenden ohne Docker

Zum entwickeln und testen des Frontends ohne Docker, können Sie die folgenden Schritte befolgen. Allerdings wird empfohlen, die API über das Docker Compose Setup zu verwenden.

### Umgebungsvariablen

Kopieren Sie die `.env.local.example` Datei und benennen Sie sie in `.env.local` um. Passen Sie die Werte der Umgebungsvariablen an.
Dies ist besonders wichtig, um die API-URL zu setzen, damit das Frontend die API erreichen kann.

### Lokales bauen und starten

- Node.js v20.0.0 oder höher

```sh
corepack enable
pnpm install
pnpm run dev
```

Das Frontend sollte nun unter `http://localhost:3000` erreichbar sein.
