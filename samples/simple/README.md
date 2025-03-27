# Simple Container Instructions

This guide walks through basic container operations using Podman.

1. Build the Container Image
    ```bash
    # Build an image tagged as 'simple:latest' from the Dockerfile in current directory
    podman build -t simple:latest .
    ```

2. Run the Container
    ```bash
    # Run the container in detached mode
    podman run -d simple:latest
    ```

3. View Running Containers
    ```bash
    # List all running containers
    podman ps -a
    ```

4. Interactive Shell Access
    ```bash
    # Start a new container with an interactive bash shell
    podman run -it --entrypoint /bin/bash simple:latest
    ```

5. View Container Logs, split the terminal
    ```bash
    # First get the container ID
    podman ps
    
    # Stream the logs from the container (-f follows the log output)
    podman logs -f <containerID>
    ```
    
    >
    > Note: Replace `<containerID>` with the actual container ID from `podman ps` output or `$(podman ps -q)`.
    >
    
6. Development Workflow
    ```bash
    # After editing the Dockerfile, rebuild the image:
    podman build -t simple:latest .
    
    # Then run a new container with the updated image:
    podman run -d simple:latest
    ```
