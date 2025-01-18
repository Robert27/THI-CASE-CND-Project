# Kubernetes mit Helm

Ebenso ist es möglich, die Anwendung in einem Kubernetes-Cluster zu deployen. Hierfür wird Helm verwendet, um die Anwendung zu verwalten und zu konfigurieren. Es basiert auf den in der GitHub Container Registry veröffentlichten Images.

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
- Die Anwendung ist dann unter `http://localhost` erreichbar
- Bei Problemen prüfen Sie:
  - Pod-Status (`kubectl get pods `)
  - Services (`kubectl get services `)
  - Ingress (`kubectl get ingress `)
