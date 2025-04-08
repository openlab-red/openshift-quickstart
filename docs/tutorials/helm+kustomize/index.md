# Helm and Kustomize

This tutorial introduces Helm and Kustomize, two powerful Kubernetes configuration management tools. 
You'll learn the basics of each tool through practical exercises and see how combining them can simplify and enhance your Kubernetes deployments.

> The code examples and instructions in this tutorial are located under `openshift-quickstart` project in the
> `tutorials/helm+kustomize` directory. 
>
> Ensure you are in this directory before executing the commands.
>

1. Navigate to the Tutorial Directory
    ```bash
    cd openshift-quickstart/tutorials/helm+kustomize
    ```

2. Or open a New Terminal

---

## Exercises

### Exercise 1: Helm Basics

Deploy a simple application using Helm.

1. Create a Helm Chart
   ```bash
   helm create myapp
   ```
2. Edit/Update `values.yaml` to customize your deployment:
     ```yaml
      image:
        repository: registry.redhat.io/ubi9/python-312
        tag: latest

      service:
        type: ClusterIP
        port: 8000
     ```
3. Edit `templates/deployment.yaml` to add the python3 startup command:
   ```yaml
   spec:
     template:
       spec:
         containers:
         - name: {% raw %}{{ Chart.Name }}{% endraw %}
           command: ["python3"]
           args: ["-m", "http.server"]
   ```

3. Deploy the Chart
   ```bash
   helm install myapp-release ./myapp
   ```
4. Verify Deployment
   ```bash
   oc get pods
   ```

---

### Exercise 2: Kustomize Basics

Deploy the same application using Kustomize.

1. Create a directory structure:
     ```
     myapp/
     ├── base/
     │   ├── deployment.yaml
     │   ├── service.yaml
     │   └── kustomization.yaml
     └── overlays/
         └── dev/
             └── kustomization.yaml
     ```
2. Populate `deployment.yaml` and `service.yaml` with basic Kubernetes manifests.
   - Create `deployment.yaml`:
     ```yaml
      apiVersion: apps/v1
      kind: Deployment
      metadata:
        name: myapp-k
      spec:
        replicas: 1
        selector:
          matchLabels:
            app: myapp-k
        template:
          metadata:
            labels:
              app: myapp-k
          spec:
            containers:
            - name: nginx
              image: registry.redhat.io/ubi9/nginx-122:latest
              command: ["nginx"]
              args: ["-g", "daemon off;"]
              ports:
              - containerPort: 8080
              resources:
                limits:
                  cpu: "500m"
                  memory: "256Mi"
                requests:
                  cpu: "200m" 
                  memory: "128Mi"
     ```

   - Create `service.yaml`:
     ```yaml
     apiVersion: v1
     kind: Service
     metadata:
       name: myapp-k
     spec:
       selector:
         app: myapp-k
       ports:
       - port: 8080
         targetPort: 8080
       type: ClusterIP
     ```

3. In `overlays/dev/kustomization.yaml`, reference the base and apply customizations:
     ```yaml
     apiVersion: kustomize.config.k8s.io/v1beta1
     kind: Kustomization
     resources:
       - ../../base
     patches:
       - path: deployment-patch.yaml
     ```

4. Create `deployment-patch.yaml`:
     ```yaml
     apiVersion: apps/v1
     kind: Deployment
     metadata:
       name: myapp-k
     spec:
       replicas: 2
     ```

5. Deploy with Kustomize**
   ```bash
   # The -k flag tells oc/kubectl to process the directory as a kustomization
   oc apply -k overlays/dev
   ```

6. Verify Deployment**
   ```bash
   oc get pods
   ```

---

### Exercise 3: Using Kustomize with Helm Charts (`--enable-helm`)

Leverage Kustomize's built-in Helm integration to directly manage Helm charts without manually rendering templates.

1. Create Directory Structure
   ```bash
   mkdir -p helm-k/overlays/prod
   ```

2. Create a Sample Helm Chart
   ```bash
   # Create charts directory
   mkdir -p helm-k/overlays/prod/charts
   cd helm-k/overlays/prod/charts
   
   # Create a sample Helm chart
   helm create myapp
   
   # Return to the base directory
   cd ../../..
   ```

   This will create a basic Helm chart structure with default templates and values.

3. Define Kustomization with Helm Chart
   Create a `kustomization.yaml` file in `helm-k/overlays/prod`:
   ```yaml
    apiVersion: kustomize.config.k8s.io/v1beta1
    kind: Kustomization

    helmGlobals:
      chartHome: ./charts
    helmCharts:
      - name: myapp
        releaseName: myapp-prod
        namespace: prod
        valuesInline:
          replicaCount: 2
   ```

3. Build Using Kustomize with Helm Enabled and check the replicas
   ```bash
   kustomize build --enable-helm helm-k/overlays/prod |grep replicas
   ```

### Explanation

- The `--enable-helm` flag allows Kustomize to directly process Helm charts, simplifying the workflow by eliminating the need to manually run `helm template`.
- Inline values (`valuesInline`) provide a convenient way to customize Helm chart parameters directly within the Kustomize configuration.

