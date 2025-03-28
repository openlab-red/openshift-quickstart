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