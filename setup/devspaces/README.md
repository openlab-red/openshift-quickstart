# Devspaces Deployment Guide

This guide provides instructions on how to deploy Devspaces using Kustomize and OpenShift.

## Prerequisites

- Ensure you have `kubectl` and `oc` (OpenShift CLI) installed and configured.
- Ensure you have access to an OpenShift cluster with the necessary permissions to deploy resources.

## Deployment Steps

1. **Navigate to the Overlay Directory**

   Change your working directory to the `nested-container` overlay directory:

   ```bash
   cd devspaces/overlays/nested-container
   ```

2. **Build the Kustomization**

   Use Kustomize to build the configuration files. This will generate the necessary Kubernetes manifests based on the overlay configuration:

   ```bash
   kustomize build .
   ```

3. **Apply the Configuration**

   Apply the generated configuration to your OpenShift cluster in the `openshift-devspaces` namespace:

   ```bash
   kustomize build . | oc apply -n openshift-devspaces -f -
   ```

   This command will deploy the Devspaces resources as defined in the `kustomization.yaml` file.

4. **Pre-provision User Namespaces**

   Run the pre-provision script to create and configure user namespaces:

   ```bash
   ./scripts/pre-provision.sh
   ```

   This script will:
   - Create namespaces for each user (up to 25 users by default)
   - Add required labels and annotations for DevSpaces
   - Configure appropriate RBAC permissions

## Additional Information

- The `kustomization.yaml` file in the `nested-container` overlay directory specifies the resources and patches to be applied.
- Ensure that the `openshift-devspaces` namespace exists or create it before applying the configuration.
- Review the `devspaces-checluster.yaml` and other related files for specific configurations and customizations.

For more detailed information on each configuration file, refer to the comments and documentation within the respective YAML files.
