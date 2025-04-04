# OpenShift Quickstart

Welcome to the OpenShift Quickstart workshop! 
This workshop is designed to provide you with a seamless development experience, whether you choose to work locally on your laptop or leverage the OpenShift DevSpaces Cloud IDE. 
Both environments are equipped to help you efficiently set up and manage your development projects with ease. Follow the instructions below to get started with your preferred setup.


## Laptop

### Prerequisite: Install Podman Desktop

Before proceeding, make sure you have Podman Desktop installed on your laptop. 
You can download and install it from the official website: (https://podman-desktop.io/)[Podman Desktop].

![Podman](assets/images/podman.png)

### Git Clone

1. Open your terminal.
2. Choose your root workspace project:
      - Decide on the directory where you want your root workspace to reside. This will be the main directory for your project files and configurations.
3. Navigate to the directory where you want to clone the project (e.g. `/projects`).
4. Run the following command to clone the Git repository:
   ```bash
   cd /projects/
   git clone https://github.com/openlab-red/openshift-quickstart.git
   git clone https://github.com/openlab-red/openshift-quickstart-manifest.git
   ```

5. Open Visual Studio Code or your favourite IDEs:
   ```bash
   code .
   ```

1. Log into your OpenShift cluster at [OpenShift Console](https://console-openshift-console.{{ config.extra.base_url }}/).
   ![OpenShift](assets/images/openshift.png)

2. Navigate to the DevSpaces dashboard.
   ![DevSpaces](assets/images/devspaces.png)

   > Note: Even if working locally, having a DevSpaces namespace allows to have all necessary configuratin in place for the tutorial

## DevSpaces

### Using DevSpaces with Devfile

OpenShift DevSpaces provides a cloud-based development environment using the configuration specified in the `devfile.yaml`. 

Follow these steps to get started:

1. Log into your OpenShift cluster at [OpenShift Console](https://console-openshift-console.{{ config.extra.base_url }}/).
   ![OpenShift](assets/images/openshift.png)

2. Navigate to the DevSpaces dashboard.
   ![DevSpaces](assets/images/devspaces.png)

3. Create a new workspace by importing this repository URL:
   ```bash
   https://github.com/openlab-red/openshift-quickstart.git
   ```
   ![DevSpaces](assets/images/create-workspace.png)
   DevSpaces will automatically detect and use the `devfile.yaml` at the root of the project.

4. The devfile configures:
      - A developer container with ZSH shell
      - Default workspace pointing to the project's VS Code workspace
      - Required environment variables and configurations

5. Once the workspace starts:
      - You'll have a fully configured development environment
      - All necessary tools and dependencies will be pre-installed
   ![DevSpaces](assets/images/workspace.png)

> Note: The devfile inherits configurations from a parent devfile that contains base developer tooling and settings.
