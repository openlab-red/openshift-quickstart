# OpenShift Quickstart

## Laptop

### Prerequisite: Install Podman Desktop

Before proceeding, make sure you have Podman Desktop installed on your laptop. 
You can download and install it from the official website: (https://podman-desktop.io/)[Podman Desktop].

![Podman](assets/images/podman.png)

### Git Clone

1. Open your terminal.
2. Navigate to the directory where you want to clone the project.
3. Run the following command to clone the Git repository:
   ```
   git clone https://github.com/openlab-red/openshift-quickstart.git
   ```
   Replace `https://github.com/openlab-red/openshift-quickstart.git` with the actual URL of the Git repository you want to clone.

### Open the Project with Visual Studio Code
1. Navigate to the project directory:
   ```
   cd openshift-quickstart
   ```
   Replace `openshift-quickstart` with the name of the directory where you cloned the Git repository.
2. Open Visual Studio Code:
   ```
   code .
   ```

## DevSpaces

### Using DevSpaces with Devfile

OpenShift DevSpaces provides a cloud-based development environment using the configuration specified in the `devfile.yaml`. Follow these steps to get started:

1. Log into your OpenShift cluster and navigate to the DevSpaces dashboard.

2. Create a new workspace by importing this repository URL:
   ```
   https://github.com/openlab-red/openshift-quickstart.git
   ```
   DevSpaces will automatically detect and use the `devfile.yaml` at the root of the project.

3. The devfile configures:
   - A developer container with ZSH shell
   - Default workspace pointing to the project's VS Code workspace
   - Required environment variables and configurations

4. Once the workspace starts:
   - You'll have a fully configured development environment
   - VS Code server will be available in your browser
   - All necessary tools and dependencies will be pre-installed

5. Start developing:
   ```bash
   # Your workspace is ready with:
   - ZSH shell configured
   - VS Code extensions
   - Project workspace loaded
   ```

> Note: The devfile inherits configurations from a parent devfile that contains base developer tooling and settings.
