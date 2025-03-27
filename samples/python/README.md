# Instructions

1. Build

    `podman build -t python-server:latest .`

2. Run

    `podman run python-server:latest`

3. Check if it is running

    `podman ps`

4. Test

    `curl http://localhost:8000`

5. Relaunch the container

    `podman run -p8000:8000 python-server:latest`

6. Test again

    `curl http://localhost:8000`


7. Create a pod

    `podman play kube --publish 8000:8000 pod.yaml`

8. Verify if it is running

    ```bash
        podman pod ps
        podman ps
    ```

9. Stop and remove

    ```bash
        podman pod stop <podId>
        podman pod rm <podId>
    ```

10. Create in OpenShift.

    ```bash
    oc project bit-play

    oc create -f pod.yaml

    ```

11. Add service and route.

    ```bash
    oc create -f service.yaml
    oc create -f route.yaml
    ```

12. Show that if you delete the pod will be lost.