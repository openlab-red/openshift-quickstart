# Argo CD Setup Tutorial

This tutorial guides you through setting up Argo CD on OpenShift and deploying applications using Argo CD Application manifests. We'll cover installing Argo CD, configuring it, and deploying example applications (`go-app`, `angular-app`, and `java-app`).

> **Note:** All YAML files referenced in this tutorial are located under the `openshift-quickstart/gitops/argocd` directory. Ensure you navigate to this directory before executing commands.

---

## 1. Install Argo CD on OpenShift

First, we need to install Argo CD using the provided YAML manifest (`argocd.yaml`). This manifest configures Argo CD with OpenShift OAuth integration, resource limits, and exclusions.

### Navigate to the Argo CD Directory

```bash
cd openshift-quickstart/gitops/argocd
```

### Adjust Namespace in Argo CD Manifest

Before applying the manifest, edit the `argocd.yaml` file to replace the default namespace (`devspaces-admin`) with your specific user namespace (`user1`, `devspaces-user1`, etc.):

```yaml
# argocd.yaml
metadata:
  name: argocd
  namespace: <your-user-namespace> # Replace with your namespace
```

### Apply the Argo CD Manifest

Deploy Argo CD using the following command:

```bash
oc apply -f argocd.yaml
```

This command will:

- Deploy Argo CD in your specified user namespace.
- Enable OpenShift OAuth integration for Single Sign-On (SSO).
- Configure resource limits and requests for Argo CD components.
- Exclude Tekton resources (`TaskRun`, `PipelineRun`) from Argo CD management.

---

## 2. Access the Argo CD UI

After installation, access the Argo CD UI through the OpenShift route:

1. Log in to your OpenShift Console.
2. Navigate to **Networking → Routes** in your specific user namespace.
3. Click on the Argo CD route URL to open the Argo CD UI.
4. Log in using your OpenShift credentials (OAuth integration is enabled).

---

## 3. Deploy Applications Using Argo CD

We will deploy three example applications (`go-app`, `angular-app`, and `java-app`) using Argo CD Application manifests.

### Adjust Namespace in Application Manifests

Before deploying, edit each application manifest (`go-app.yaml`, `angular.yaml`, `java-app.yaml`) to replace the default namespace (`devspaces-admin`) with your specific user namespace:

```yaml
# Example adjustment in go-app.yaml, angular.yaml, java-app.yaml
spec:
  destination:
    namespace: <your-user-namespace> # Replace with your namespace
    server: 'https://kubernetes.default.svc'
```

---

### Deploying the Applications

Deploy each application by applying its respective YAML manifest:

#### Deploy Go Application (`go-app`):

```bash
oc apply -f application/go-app.yaml
```

#### Deploy Angular Application (`angular-app`):

```bash
oc apply -f application/angular.yaml
```

#### Deploy Java Application (`java-app`):

```bash
oc apply -f application/java-app.yaml
```

---

## 4. Synchronize Applications in Argo CD UI

After applying the manifests, synchronize the applications through the Argo CD UI:

1. Open the Argo CD UI.
2. Navigate to the **Applications** section.
3. You should see the newly created applications (`go-app`, `angular-app`, `java-app`) listed.
4. Click on each application and then click **Sync** to deploy the application resources to your cluster.

---

## 5. Verify Application Deployment

Verify that your applications are successfully deployed:

- Navigate to the OpenShift Console.
- Go to **Workloads → Pods** in your specific user namespace.
- Confirm that pods for each application (`go-app`, `angular-app`, `java-app`) are running and healthy.

---
