
# Cluster Setup with Kustomize and OpenShift

This guide provides instructions on how to set up your cluster using Kustomize and apply the configuration to an OpenShift cluster.

## Prerequisites

- Ensure you have `kubectl` and `oc` (OpenShift CLI) installed and configured on your machine.
- Make sure you have access to an OpenShift cluster and are logged in using the `oc` command.

## Overview

The configuration for the cluster is managed using Kustomize, which allows you to customize Kubernetes YAML configurations. The `kustomization.yaml` file in the `cluster/base` directory specifies the components and resources to be applied to the OpenShift cluster.

## Steps to Apply the Configuration

1. **Navigate to the Base Directory:**

   Open your terminal and navigate to the `cluster/base` directory where the `kustomization.yaml` file is located.

   ```bash
   cd cluster/base
   ```

2. **Build the Kustomize Configuration:**

   Use the `kustomize build` command to generate the final configuration YAML from the `kustomization.yaml` file.

   ```bash
   kustomize build .
   ```

3. **Apply the Configuration to OpenShift:**

   Pipe the output of the `kustomize build` command to `oc apply` to apply the configuration to your OpenShift cluster.

   ```bash
   kustomize build . | oc apply -f -
   ```

   This command will apply all the components and resources defined in the `kustomization.yaml` to the OpenShift cluster.

## Components Included

The `kustomization.yaml` file includes the following components:

- **Feature Gate**: Manages feature gates for the cluster.
- **Container Runtime**: Configures the container runtime settings.
- **Operators**: Includes various operators such as DevSpaces, DevWorkspaces, and Kubernetes Image Puller.
- **Security Context Constraints (SCC)**: Configures nested container security settings.

## Conclusion

By following these steps, you can successfully apply the cluster configuration using Kustomize and OpenShift CLI. Ensure that you have the necessary permissions and access to the OpenShift cluster before applying the configurations.

