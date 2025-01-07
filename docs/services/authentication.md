
# Authentifizierung

- **Autor**: Andreas Ziegltrum
- **Architektur**: Hexagonal
- **Technologie**: Spring (Java)

## Architektur Beschreibung

Der Microsrvice Authservice ist verandwortlich für den Login von Usern und damit zur vergabe von JWT zur Authentifizierung von Späteren Anfragen

## API

| Method | Path                            | Description                                                          |
| ------ | ------------------------------- | -------------------------------------------------------------------- |
| POST   | [/login](#postlogin)            | Anmelden mit bereits bestehenden Nutzer um JWT zu erhalten           |
