# Frontend

Das Dashboard ist die Benutzeroberfläche für die Verwaltung der Objekte und Bestellungen. Es bietet die Möglichkeit, Objekte zu erstellen, bearbeiten und löschen. Außerdem können aktive Bestellungen eingesehen und bearbeitet werden.
Natürlich bietet es auch die Möglichkeit, sich als Nutzer zu registrieren, einzuloggen und das Passwort zu ändern.

- **Technologie**: Next.js (React)

## Verwenden ohne Docker

### Umgebungsvariablen

Kopieren Sie die `.env.local.example` Datei und benennen Sie sie in `.env.local` um. Passen Sie die Werte der Umgebungsvariablen an.

### Lokales bauen und starten

- Node.js v20.0.0 oder höher

```sh
corepack enable
pnpm install
pnpm run dev
```
