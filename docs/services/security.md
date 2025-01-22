# Sicherheit

Zur sicheren Verwendung werden JWT-Token verwendet. Diese werden beim Login erstellt und bei jeder Anfrage an die API mitgeschickt. Die sind mit einem privaten Schlüssel signiert und können nur mit dem öffentlichen Schlüssel verifiziert werden. Die Gültigkeit der Token beträgt 10 Tage, danach muss sich der Nutzer erneut anmelden.

Im Frontend wird NextAuth verwendet, um die Authentifizierung auf serverseitigem Rendering zu ermöglichen. Dabei wird der bei der Anmeldung erhaltene JWT-Token ausgelesen und im Cookie gespeichert und kann so bei jeder Anfrage an den Server im Authorization-Header mitgeschickt werden.

Im Token ist neben dem Nutzernamen auch die User-ID enthalten, um die Identifikation des Nutzers zu ermöglichen. Der Nutzer daher kann nur auf seine eigenen Objekte zugreifen und bearbeiten.

::: warning Hinweis
Folglich wird drigend empfohlen den privaten und öffentlichen Schlüssel, welcher als Demo im Repository liegt, zu ersetzen. Die Schlüssel können in der `.env` bzw. bei Helm in `jwt-secrets.yaml` (bei Verwendung von Kubernetes) angepasst werden.
:::
