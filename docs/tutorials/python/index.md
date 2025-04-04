# Deploying and Testing a Python server

## Tutorial

> The code examples and instructions in this tutorial are located under `openshift-quickstart` project in the
> `tutorials/python` directory. 
> Ensure you are in this directory before executing the commands.
>

1. Navigate to the Tutorial Directory
    ```bash
    # Change to the tutorials/simple directory
    cd openshift-quickstart/tutorials/python
    ```

2. Or open a New Terminal

### Building the Container Image

1. **Build the Container Image:**
   Use the following command to build the container image for the Python server:
   ```bash
   podman build -t python-server:latest .
   ```

### Running the Container

2. **Run the Container:**
   Start the container using the image you just built:
   ```bash
   podman run python-server:latest
   ```

3. **Verify the Container is Running:**
   Check the status of the running container with:
   ```bash
   podman ps
   ```

### Testing the Server

1. **Test the Server:**
   Ensure the server is responding by executing:
   ```bash
   curl http://localhost:8000
   ```
   > Note: This command will fail at this point since we haven't mapped the container's port to the host yet. We'll fix this in the next step.

2. **Relaunch the Container with Port Mapping:**
   To map the container's port to the host, use:
   ```bash
   podman run -p8000:8000 python-server:latest
   ```

3. **Test the Server Again:**
   Verify the server is accessible with:
   ```bash
   curl http://localhost:8000
   ```

### Deploying with Podman

1. **Create a Pod:**
   Deploy the application as a pod with port publishing:
   ```bash
   podman play kube --publish 8000:8000 pod.yaml
   ```

2. **Verify Pod is Running:**
   Check the status of the pod and its containers:
   ```bash
   podman pod ps
   podman ps
   ```

3. **Stop and Remove the Pod:**
   To stop and remove the pod, use:
   ```bash
   podman pod stop <podId>
   podman pod rm <podId>
   ```

### Deploying to OpenShift

1. **Create Resources in OpenShift:**
    Switch to the desired project and create resources:
    ```bash
    # Note: If you are using DevSpaces, you are already logged in your namespace due to token injection
    # For non-DevSpaces users:
    # oc login --token=<your-token> --server=<your-server-url>
    # oc project devspaces-userX

    oc create -f pod.yaml
    ```

2. **Add Service and Route:**
    Expose the application by creating a service and route:
    ```bash
    oc create -f service.yaml
    oc create -f route.yaml
    ```
3. **Access the Application:**
    Get the route URL and access your application:
    ```bash
    # Get the route URL
    oc get route python-server-route -o jsonpath='{.spec.host}'
    
    # Access the application using the route URL
    curl http://$(oc get route python-server-route -o jsonpath='{.spec.host}')
    ```
    You can also open the route URL in your web browser to view the application.
    You can find the route URL under "Networking" → "Routes" and click it to access your application.

4. **Note on Pod Deletion:**
    Be aware that deleting the pod will result in the loss of the running instance.

## Exercises

### Exercise 1: Create a Deployment in OpenShift

**Objective:** Learn how to create a deployment in OpenShift to manage your application.

1. **Create a Deployment:**
   Use the following command to create a deployment for your application:
   ```bash
   oc create deployment python-server --image=registry.redhat.io/ubi9/python-312 -- python3 -m http.server
   ```

2. **Verify the Deployment:**
   Check the status of the deployment to ensure it was created successfully:
   ```bash
   oc get deployments
   ```

3. **Check the Pods:**
   Verify that pods are running as part of the deployment:
   ```bash
   # Note: The pod name will have a random suffix appended to it, like: python-server-5b7f8d9c7b-xyz12
   oc get pods
   ```

### Exercise 2: Scale the Deployment

**Objective:** Understand how to scale a deployment to handle more traffic.

1. **Scale the Deployment:**
   Increase the number of replicas to handle more traffic:
   ```bash
   oc scale --replicas=3 deploy/python-server
   ```

2. **Verify Scaling:**
   Check the status of the pods to ensure scaling was successful:
   ```bash
   oc get pods
   ```

3. **Test the Application:**
   Ensure the application is still accessible and functioning correctly:
   ```bash
   curl http://$(oc get route python-server-route -o jsonpath='{.spec.host}')
   ```

### Exercise 3: Update the Deployment with a New Image

**Objective:** Learn how to update a deployment with a new version of your application.

1. **Build a New Image:**
   Make changes to your application and build a new image:
   ```bash
   podman build -t python-server:v2 .
   ```

2. **Import the Local Image to OpenShift:**

   **For Laptop Users:**
   >
   > Note: The OpenShift internal registry has been exposed just for the workshop.
   >

   Use the following command to log in to the OpenShift internal registry:
   ```bash
   podman login -u $(oc whoami) -p $(oc whoami -t) https://default-route-openshift-image-registry.{{ config.extra.base_url }}
   ```
   Tag your local image with the OpenShift registry namespace:
   ```bash
   podman tag python-server:v2 default-route-openshift-image-registry.{{ config.extra.base_url }}/devspaces-user1/python-server:v2
   ```
   Push the tagged image to the OpenShift internal registry:
   ```bash
   podman push default-route-openshift-image-registry.{{ config.extra.base_url }}/devspaces-user1/python-server:v2
   ```

   **For DevSpaces Users:**

   Use the following command to log in to the OpenShift internal registry:
   ```bash
   podman login -u $(oc whoami) -p $(oc whoami -t) image-registry.openshift-image-registry.svc:5000
   ```
   Tag your local image with the OpenShift registry namespace:
   ```bash
   podman tag python-server:v2 image-registry.openshift-image-registry.svc:5000/devspaces-user1/python-server:v2
   ```
   Push the tagged image to the OpenShift internal registry:
   ```bash
   podman push image-registry.openshift-image-registry.svc:5000/devspaces-user1/python-server:v2
   ```

3. **Update the Deployment:**
   - Update the deployment to use the new image:
     ```bash
     oc set image deployment/python-server python-312=python-server:v2 --source=imagestreamtag
     ```
     > Note: An imagestream tag in OpenShift is a reference to a specific version of an image within an imagestream. It allows you to manage and track different versions of container images, facilitating seamless updates and rollbacks.

4. **Verify the Update:**
   - Check the status of the deployment to ensure it updated successfully:
     ```bash
     oc rollout status deployment/python-server
     ```

5. **Test the Updated Application:**
   - Verify that the updated application is running correctly:
     ```bash
     curl http://$(oc get route python-server-route -o jsonpath='{.spec.host}')
     ```
