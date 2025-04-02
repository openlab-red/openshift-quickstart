# Instructions for Deploying and Testing the Python Server

## Building the Container Image

1. **Build the Container Image:**
   Use the following command to build the container image for the Python server:
   ```bash
   podman build -t python-server:latest .
   ```

## Running the Container

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

## Testing the Server

4. **Test the Server:**
   Ensure the server is responding by executing:
   ```bash
   curl http://localhost:8000
   ```

5. **Relaunch the Container with Port Mapping:**
   To map the container's port to the host, use:
   ```bash
   podman run -p8000:8000 python-server:latest
   ```

6. **Test the Server Again:**
   Verify the server is accessible with:
   ```bash
   curl http://localhost:8000
   ```

## Deploying with Podman and OpenShift

7. **Create a Pod:**
   Deploy the application as a pod with port publishing:
   ```bash
   podman play kube --publish 8000:8000 pod.yaml
   ```

8. **Verify Pod is Running:**
   Check the status of the pod and its containers:
   ```bash
   podman pod ps
   podman ps
   ```

9. **Stop and Remove the Pod:**
   To stop and remove the pod, use:
   ```bash
   podman pod stop <podId>
   podman pod rm <podId>
   ```

## Deploying to OpenShift

10. **Create Resources in OpenShift:**
    Switch to the desired project and create resources:
    ```bash
    oc new-project pod-play
    oc create -f pod.yaml -n pod-play
    ```

11. **Add Service and Route:**
    Expose the application by creating a service and route:
    ```bash
    oc create -f service.yaml -n pod-play
    oc create -f route.yaml -n pod-play
    ```

12. **Note on Pod Deletion:**
    Be aware that deleting the pod will result in the loss of the running instance.


### Exercise 1: Create a Deployment in OpenShift

**Objective:** Learn how to create a deployment in OpenShift to manage your application.

1. **Create a Deployment:**
   - Use the following command to create a deployment for your application:
     ```bash
     oc create deployment python-server --image=registry.redhat.io/ubi9/python-312 -n pod-play
     ```

2. **Verify the Deployment:**
   - Check the status of the deployment to ensure it was created successfully:
     ```bash
     oc get deployments -n pod-play
     ```

3. **Check the Pods:**
   - Verify that pods are running as part of the deployment:
     ```bash
     oc get pods -n pod-play
     ```

### Exercise 2: Scale the Deployment

**Objective:** Understand how to scale a deployment to handle more traffic.

1. **Scale the Deployment:**
   - Increase the number of replicas to handle more traffic:
     ```bash
     oc scale deployment/python-server --replicas=3 -n pod-play
     ```

2. **Verify Scaling:**
   - Check the status of the pods to ensure scaling was successful:
     ```bash
     oc get pods -n pod-play
     ```

3. **Test the Application:**
   - Ensure the application is still accessible and functioning correctly:
     ```bash
     curl http://<route-url>
     ```

### Exercise 3: Update the Deployment with a New Image

**Objective:** Learn how to update a deployment with a new version of your application.

1. **Build a New Image:**
   - Make changes to your application and build a new image:
     ```bash
     podman build -t python-server:v2 .
     ```

2. **Update the Deployment:**
   - Update the deployment to use the new image:
     ```bash
     oc set image deployment/python-server python-server=python-server:v2 -n pod-play
     ```

3. **Verify the Update:**
   - Check the status of the deployment to ensure it updated successfully:
     ```bash
     oc rollout status deployment/python-server -n pod-play
     ```

4. **Test the Updated Application:**
   - Verify that the updated application is running correctly:
     ```bash
     curl http://<route-url>
     ```
