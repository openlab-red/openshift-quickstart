# Instructions

1. Build

    `podman build -t simple:latest .`

2. Run

    `podman run simple:latest`

3. Check if it is running

    `podman ps`

4. Interaction

    `podman run -it --entrypoint /bin/bash simple:latest`

5. View logs

    ```bash
    podman ps
    podman logs -f <containerID>
    ```

6. Edit Dockerfile and launch again (without build)