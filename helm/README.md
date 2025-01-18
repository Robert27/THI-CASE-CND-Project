# Kubernetes with Helm

This project demonstrates how to deploy a simple application to Kubernetes using Helm.

## Setup registry

1. Create a secret for the registry:

```bash
kubectl create secret docker-registry ghcr-secret \
  --docker-server=ghcr.io \
  --docker-username=YOUR_USERNAME \
  --docker-password=YOUR_ACCESS_TOKEN \
  --docker-email=YOUR_EMAIL
```

Kubernetes should automatically assign this secret to the default service account in the default namespace.

## Starting the Cluster

1. Install dependencies first:

```bash
helm dependency update
```

2. Install the Helm chart:

```bash
helm install app .
```

3. Verify the deployment:

```bash
kubectl get pods
```

4. To access the application:

- Wait for all pods to be in "Running" state
- Open a browser and navigate to `http://localhost`

5. To uninstall the release:

```bash
helm uninstall app
```

6. To upgrade the release:

```bash
helm upgrade app .
```
