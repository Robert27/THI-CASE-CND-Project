# Konfiguration

Vor Verwendung sollten einige Konfigurationen vorgenommen werden. Diese können je nach Deployment-Umgebung variieren. Etwa müssen bei der Verwendung von Docker die Variablen in der `docker-compose.yml` bzw. `.env` angepasst werden, während bei der Verwendung von Kubernetes die `values.yaml`-Datei angepasst werden muss.

## Encryption Key

Zur Authentifizierung und Verschlüsselung von Daten wird ein öffentlicher und privater Schlüssel benötigt.
Aus sicherheitstechnischen Gründen sollten nicht die Beispiel-Schlüssel verwendet werden.

Neue Schlüssel können mit folgendem Befehl generiert werden:

```bash
openssl genrsa -out private.pem 2048
openssl rsa -in private.pem -outform PEM -pubout -out public.pem
```

Anschließend müssen die Schlüssel in den jeweiligen Umgebungen hinterlegt werden. Dies ist bei Docker in der `.env`-Datei und bei Helm in der `jwt-secrets.yaml`-Datei möglich.

::: danger Achtung
Falls die Beispieldatein angepasst werden, muss darauf geachtet werden, dass diese nicht in die Versionsverwaltung gelangen. Besonders die privaten Schlüssel sollte geheim gehalten werden.
:::

## Mock Mode

Zum Testen des Interval-Monitor Services ist es möglich, diesen im Mock-Modus zu starten. Hierbei wird ein unauthentifizierter REST Endpunkt bereitgestellt, der es erlaubt einen beliebigen Tag zu setzen.
Dadurch können anstehende Bestellungen simuliert werden ohne dass mehrere Tage gewartet werden muss.

Um den Mock-Modus zu aktivieren, muss die Umgebungsvariable `MOCK_ENABLED` auf `true` gesetzt werden.
Anschließend kann bequem über das Dashboard ein Tag gesetzt werden.
