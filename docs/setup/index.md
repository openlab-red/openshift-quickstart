# Workshop Cluster Setup Guide

This guide will walk you through the process of setting up the workshop cluster using Kustomize and OpenShift CLI (oc).

## Prerequisites

Before you begin, ensure you have the following tools installed:
- OpenShift CLI (`oc`)
- Kustomize
- Access to an OpenShift cluster with sufficient permissions
- OpenShift version 4.18.x or higher

## Setup Steps

### 1. Deploy Base Cluster Configuration

First, deploy the base cluster configuration using Kustomize:

```bash
kustomize build cluster/base | oc apply -f -
```

This command will:
- Build the Kubernetes manifests from the base configuration
- Apply them directly to your OpenShift cluster

### 2. Deploy Tekton Tasks

Next, deploy the required Tekton tasks for the workshop:

```bash
kustomize build cluster/tekton-tasks | oc apply -f -
```

This step sets up the CI/CD pipeline components needed for the workshop environment.

### 3. Deploy DevSpaces Configuration

Finally, deploy the DevSpaces configuration with nested container support:

```bash
kustomize build devspaces/overlays/nested-container | oc apply -f -
```

## Verification

After running all commands, verify your setup by:
1. Checking that all resources are in the `Running` or `Completed` state
2. Ensuring no errors are present in the cluster events
3. Verifying that DevSpaces is accessible

## Troubleshooting

If you encounter any issues:
1. Check the logs of the deployed pods
2. Verify your cluster permissions
3. Ensure all prerequisites are properly installed
4. Check the OpenShift cluster events for any error messages

## Additional Notes

- Make sure to run these commands in the order specified
- Wait for each step to complete before proceeding to the next
- Monitor the cluster events during deployment for any potential issues
```