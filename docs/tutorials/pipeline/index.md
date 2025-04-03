# Pipeline Setup

This tutorial will guide you through the process of setting up and executing a pipeline using the provided Helm chart. We will cover creating a pipeline and PVC, launching a PipelineRun, and setting up event listeners and triggers.

## Steps

### 1. Install the Pipeline using Helm

First, we need to install the pipeline using the Helm chart. The release name should be the type of the pipeline (e.g., `java`, `golang`, `nodejs`), and the target namespace should be your user namespace (`user1`) or `devspaces-user1` if using DevSpaces.


### 1.5. Link the Registry Credentials to the Pipeline Service Account

Before launching the pipeline, ensure that the pipeline service account has the necessary credentials to pull images from the registry. Use the following command to link the `registry-credentials` secret to the `pipeline` service account:

```bash
oc secrets link --for=mount pipeline registry-credentials
```

1. **Install the Helm Chart:**

   Use the following command to install the Helm chart. Replace `<pipeline-type>` with `java`, `golang`, or `nodejs`, and `<namespace>` with `user1` or `devspaces-user1`.

   ```bash
   cd gitops/pipeline

   helm install <pipeline-type> helm/pipeline --namespace <namespace>
   ```

### 2. Launch the PipelineRun

To execute the pipeline, we need to create a PipelineRun using the OpenShift UI.

1. **Create the PipelineRun:**

   - Navigate to the OpenShift Console and log in with your credentials.
   - Go to the "Pipelines" section in your project.
   - Click on the target pipeline from the list.
   - Click Actions and "Start" button.
 
2. **Select Workspaces:**

   Before starting the PipelineRun, ensure that you have selected the appropriate workspaces and PVCs. This is crucial for the pipeline to access necessary resources and credentials.

   - **Select Workspaces:**
     - Ensure the following workspaces are selected:
       - `<pipeline-type>-ws`
       - `<pipeline-type>-resource-ws`
       - `<pipeline-type>-dockerconfig`
       - `<pipeline-type>-git-credentials`

   - **Select PVCs:**
     - Ensure the following PVCs are selected:
       - `<pipeline-type>-ws` (replace with actual PVC name)
       - `<pipeline-type>-resource-ws` (replace with actual PVC name)

   - **Link Docker and GitHub Credentials:**
     - Ensure that the Docker and GitHub credentials are linked to the pipeline service account:
       - `registry-credentials` for Docker
       - `git-credentials` for GitHub

   After selecting the workspaces, PVCs, and linking the credentials, proceed to the next step.

3. **Click "Start":**

   Once all selections are made, click the "Start" button to initiate the PipelineRun.
