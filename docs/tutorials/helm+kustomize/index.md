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
> **Security Note**: The default Helm chart uses nginx which has some security considerations:
> - Runs as root by default
> - Binds to privileged port 80
> - Requires specific filesystem permissions
>
> OpenShift enforces security by running containers with random UIDs. To make the nginx image work properly in OpenShift, you'll need to:
> - Configure the container to use non-root user
> - Use non-privileged ports (>1024)
> - Ensure proper filesystem permissions
>

2. Create a ConfigMap for nginx configuration:
   ```yaml
   # Create nginx.conf in the chart directory
   cat > myapp/nginx.conf << 'EOF'
   worker_processes  auto;
   
   error_log  /var/log/nginx/error.log warn;
   pid        /var/run/nginx.pid;
   
   events {
       worker_connections  1024;
   }
   
   http {
       include       /etc/nginx/mime.types;
       default_type  application/octet-stream;
       
       log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                        '$status $body_bytes_sent "$http_referer" '
                        '"$http_user_agent" "$http_x_forwarded_for"';
       
       access_log  /var/log/nginx/access.log  main;
       
       sendfile        on;
       keepalive_timeout  65;
       
       server {
          listen       8080 default_server;
          listen       [::]:8080 default_server;
          server_name  _;
           
           location / {
               root   /usr/share/nginx/html;
               index  index.html index.htm;
           }
       }
   }
   EOF

   # Create ConfigMap template
   cat > myapp/templates/configmap.yaml << 'EOF'
   apiVersion: v1
   kind: ConfigMap
   metadata:
     name: {% raw %}{{ include "myapp.fullname" . }}{% endraw %}-nginx-config
     labels:
       {% raw %}{{- include "myapp.labels" . | nindent 4 }}{% endraw %}
   data:
     nginx.conf: |
   {% raw %}{{ .Files.Get "nginx.conf" | indent 4 }}{% endraw %}
   EOF
   ```

2. Edit/Update `values.yaml` to customize your deployment:
     ```yaml
      volumes:
      - name: cache
        emptyDir: {}
      - name: pid
        emptyDir: {}      
      - name: nginx-config
        configMap:
          # The name will contain the release name, e.g. myapp-release-nginx-config
          name: myapp-release-nginx-config

      volumeMounts:
      - name: cache
        mountPath: "/var/cache/nginx"
      - name: pid
        mountPath: "/var/run"  
      - name: nginx-config
        mountPath: "/etc/nginx/nginx.conf"
        subPath: nginx.conf
     ```

3. Deploy the Chart
   ```bash
   helm install myapp-release ./myapp
   ```
4. Verify Deployment
   ```bash
   oc get pods
   ```


Alternatively, you can customize the nginx image to run as non-root by creating your own Containerfile:

1. Create a Containerfile:
   ```Dockerfile
   FROM nginx:1.16.0
   
   # Create required directories and set permissions
   RUN mkdir -p /var/cache/nginx /var/run && \
       chown 0:0 /var/cache/nginx /var/run && \
       chmod -R g=u /var/cache/nginx /var/run
   
   # Copy nginx.conf
   COPY --chown=0:0 nginx.conf /etc/nginx/nginx.conf

   # Use arbitrary user ID as per OpenShift guidelines
   USER 1001
   ```

2. Build and push the custom image:
   ```bash
   podman build -t custom-nginx .
   podman push custom-nginx quay.io/quickstart/nginx:1.16.0
   ```

3. Update the image and the port values.yaml:
   ```yaml
   image:
     repository: quay.io/quickstart/nginx:1.16.0
     tag: latest
   
   service:
     port: 8080
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

