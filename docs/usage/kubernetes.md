# Kubernetes mit Helm

Ebenso ist es möglich, die Anwendung in einem Kubernetes-Cluster zu deployen. Hierfür wird Helm verwendet, um die Anwendung zu verwalten und zu konfigurieren. Es basiert auf den in der GitHub Container Registry veröffentlichten Images, mehr dazu siehe [Bauautomarisierung](/usage/ci).

## Voraussetzungen

- Kubernetes-Cluster (lokal oder remote)
- Helm
- kubectl CLI-Tool

## Container Registry Einrichtung

### Registry-Zugriff konfigurieren

1. Erstellen Sie einen neuen Namespace für Ihre Anwendung:

```bash
kubectl create namespace smartorder
```

2. Konfigurieren Sie den Zugriff auf die GitHub Container Registry:
   - Erstellen Sie ein Personal Access Token in GitHub
   - Fügen Sie die Registry-Zugangsdaten als Kubernetes-Secret hinzu:

```bash
kubectl create secret docker-registry ghcr-secret \
  --docker-server=ghcr.io \
  --docker-username=IHR_GITHUB_BENUTZERNAME \
  --docker-password=IHR_GITHUB_TOKEN \
  --docker-email=IHRE_EMAIL \
  --namespace smartorder
```

3. Weisen Sie das Secret dem Standard-Service-Account zu:

```bash
kubectl patch serviceaccount default -p '{"imagePullSecrets": [{"name": "ghcr-secret"}]}' -n smartorder
```

## Konfiguration

Die Konfiguration erfolgt über die `values.yaml`-Datei im Helm-Chart-Verzeichnis. Hier können die Umgebungsvariablen für die Anwendung angepasst werden. Dazu zählen beispielsweise die Datenbankverbindung, die Ports oder der Mock-Modus.

Die besonders sensiblen Variablen wie die der private und public keys sollten in einem eigenen Secret hinterlegt werden, ähnlich wie bei der Registry-Konfiguration. Exemplarisch ist dies in der `jwt-secrets.yaml`-Datei zu sehen.
Nähere Details zur Konfiguration finden Sie in der [Konfigurationsdokumentation](/usage/configuration).

::: danger Achtung
Die aktuelle `jwt-secrets.yaml` Datei enthält nur Beispieldaten und sollte nicht in der Produktion verwendet werden und nicht auf GitHub veröffentlicht werden, sobald sensible Daten eingetragen sind.
:::

## Deployment der Anwendung

### 1. Abhängigkeiten aktualisieren

Vor dem ersten Deployment müssen die Helm-Chart-Abhängigkeiten aktualisiert werden:

```bash
helm dependency update
```

### 2. Installation des Helm Charts

Führen Sie die Installation mit folgendem Befehl durch:

```bash
helm install app . -n smartorder
```

### 3. Überprüfung des Deployments

#### Status der Pods überprüfen:

```bash
kubectl get pods -n smartorder
```

#### Logs einsehen (falls erforderlich):

```bash
kubectl logs [pod-name]
```

### 4. Zugriff auf die Anwendung

- Warten Sie, bis alle Pods den Status "Running" erreicht haben
- Die Anwendung ist dann unter `http://localhost` erreichbar (Port 80)
- Bei Problemen prüfen Sie:
  - Pod-Status (`kubectl get pods `)
  - Services (`kubectl get services `)
  - Ingress (`kubectl get ingress `)
