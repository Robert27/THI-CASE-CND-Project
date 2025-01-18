# Kubernetes with Helm

This project demonstrates how to deploy a simple application to Kubernetes using Helm.

## Setup

0. Crate namespace:

```bash
kubectl create namespace smartorder
```

1. Create a secret for the registry:

```bash
kubectl create secret docker-registry ghcr-secret \
  --docker-server=ghcr.io \
  --docker-username=YOUR_USERNAME \
  --docker-password=YOUR_ACCESS_TOKEN \
  --docker-email=YOUR_EMAIL \
  --namespace smartorder
```

2. Assign the secret to the default service account:

```bash
kubectl patch serviceaccount default -p '{"imagePullSecrets": [{"name": "ghcr-secret"}]}' -n smartorder
```

## Starting the Cluster

1. Install dependencies first:

```bash
helm dependency update
```

2. Install the Helm chart:

```bash
helm install app . -n smartorder
```

3. Verify the deployment:

```bash
kubectl get pods -n smartorder
```

4. To access the application:

- Wait for all pods to be in "Running" state
- Open a browser and navigate to `http://localhost`

5. To uninstall the release:

```bash
helm uninstall app -n smartorder
```

6. To upgrade the release:

```bash
helm upgrade app . -n smartorder
```
