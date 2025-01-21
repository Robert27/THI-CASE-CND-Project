# Dokumenation

Diese Dokumentation beschreibt die Services und Komponenten entsprechend ihrer Architektur und Funktionalität. Dabei fokussiert sie sich auf die wesentlichen in der Aufgabenstellung geforderten Aspekte mit dem Anspruch eines schönens und übersichtlichen Designs.

- **Technologie**: VitePress (Vue.js)

## Funktionsweise

Alle Seiten sind in Markdown geschrieben und werden von VitePress in HTML gerendert. Die Navigation wird automatisch generiert und basiert auf der Ordnerstruktur. Die Seiten können in der `/docs` Ordnerstruktur gefunden werden.

## Starten ohne Docker

Die Dokumentation kann ohne Docker gestartet werden. Dazu muss die Anwendung lokal gebaut und gestartet werden. Dazu wird die Verwendung des bereitgestellten Docker Compose Setups empfohlen.

Gehen Sie wie folgt vor:

```sh
cd docs
pnpm install
pnpm run docs:dev
```
