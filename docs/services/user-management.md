# Nutzerverwaltung

Dieser Nutzerverwalungs Service verwaltet die Nutzeraccounts von Smartorder.
Er bietet die Möglichkeit Nutzeraccounts zu erstellen, verändern und Löschen.

Die Endpunkte dieses Services werden hauptsächlich von anderen Services verwendet.

- **Autor**: Andreas Ziegltrum
- **Architektur**: Hexagonal
- **Technologie**: Spring (Java)

## Architektur Beschreibung


## Sequenzdiagramm

### REST API

Die REST API bietet die Möglichkeit, Objekte zu erstellen, bearbeiten und löschen. Die API ist durch die JWT Authentifizierung geschützt.

#### Endpunkte

| Method | Path                                       | Description                                                          |JWT|
| ------ | ------------------------------------------ | -------------------------------------------------------------------- | - |
| GET    | [/user/{id}](#getuser)                     | Übergibt alle erlaubten Nutzerdaten (nichtPassowrd)                  | J |
| GET    | [/user/username/{username}](#getuser)      | Alle Objekte für einen authentifizierten Nutzer abrufen              | J |
| POST   | [/user](#postnewuser)                      | Erstellen eines neuen Nutzeraccounts                                 | J |
| POST   | [/user/{id}/changepw}](#putpasswordch)     | Ändern des Passworts eienes Nutzeraccounts                           | J |
| DELETE | [/user/{id}](#deleteuser)                  | Löschen eines Nutzeraccounts                                         | J |
| Post   | [/user/checkpassword](#postcheckpassword)  | Endpunkt für Authservice für die Überprufung von Passworten          | J |
| GET    | [/user/userids](#getuserids)               | Übergibt Liste aller vorhanden userids                               | N |
| GET    | [/acctuator/health](#gethealth)            | Healt Endpunkt                                                       | N |


### gRPC

gRPC dient zur Kommunikation zwischen den Services und ist daher nicht von außen erreichbar.
Der Nutzerverwaltungs Service bietet als Server die folgenden Services an:

