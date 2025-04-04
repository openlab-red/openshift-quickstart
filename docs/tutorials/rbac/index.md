# RBAC (Role-Based Access Control)

## Tutorials

This demonstrates how to set up and test role-based access control in OpenShift.

> The code examples and instructions in this tutorial are located under `openshift-quickstart` project in the
> `tutorials/rbac` directory. 
> Ensure you are in this directory before executing the commands.
>

1. Navigate to the Tutorial Directory
    ```bash
    # Change to the tutorials/simple directory
    cd openshift-quickstart/tutorials/rbac
    ```

2. Or open a New Terminal

### Steps

1. As an admin user of your namespaces, create a role that allows reading pod information:
   ```bash
   oc create -f pod-reader.yaml
   ```

2. Create a RoleBinding to assign the pod-reader role to your user friend userX:
   ```bash
   # Replace userX with the target user and namespaceX with your namespace in the pod-reader-binding.
   sed -i 's/namespaceX/<your-namespace>/g' pod-reader-binding.yaml
   sed -i 's/userX/user2/g' pod-reader-binding.yaml
   oc create -f pod-reader-binding.yaml
   ```

3. Ask your friend as userX and verify that you can:
   - View pods (`oc get pods`)
   - Cannot access other resources (services, routes, etc.)

4. Add the standard view role to allow broader access:
   ```bash
   oc adm policy add-role-to-user view userX -n <your-namespace>
   ```

5. Login as userX again and confirm that you can now:
   - View all basic resources in the namespace
   - View pods, services, routes, configmaps, etc (no secrets).
   - Cannot modify any resources (read-only access)

## Exercises

### Exercise 1: Modify an Existing Role

**Objective:** Learn how to modify an existing role to add or remove permissions.

1. Open the `pod-reader.yaml` file and add permissions to list services:
     ```yaml
     kind: Role
     apiVersion: rbac.authorization.k8s.io/v1
     metadata:
       namespace: namespaceX
       name: pod-reader
     rules:
     - apiGroups: [""]
       resources: ["pods"]
       verbs: ["get", "list", "watch"]
     - apiGroups: [""]
       resources: ["services"]
       verbs: ["list"]
     ```

2. Apply the changes to update the role:
   ```bash
     oc apply -f pod-reader.yaml
   ```

3. Check the updated role to ensure the changes were applied:
   ```bash
   oc get role pod-reader -o yaml
   ```

4. Verify that you user friend can now list services in addition to viewing pods:
    ```bash
    oc get services
    oc get pods
    ```
   Ensure that you cannot modify any resources:
   ```bash
   # Attempting to delete a pod should fail
   oc delete pod <pod-name>
   ```
   Confirm that you still cannot access other resources like secrets:
   ```bash
   oc get secrets
   ```

### Exercise 3: Create a Service Account

**Objective:** Learn how to create a service account and bind a role to it within your own namespace.

> Note: A service account in Kubernetes is an identity that processes within a pod can use to interact with the Kubernetes API. It provides a way to control access to resources within a namespace, allowing for more granular permission management compared to using user accounts. Service accounts are particularly useful for applications running within the cluster that need to interact with the Kubernetes API.


1. Use the following command to create a service account named `pod-viewer` in your namespace:
    ```bash
    oc create serviceaccount pod-viewer -n pod-play
    ```

2. Create a YAML file named `sa-rolebinding.yaml` to bind the `view` role to the `pod-viewer` service account:
    > Note: Ensure to replace `namespaceX` with your actual namespace where you want the service account and role binding to be applied.

    ```yaml
    kind: RoleBinding
    apiVersion: rbac.authorization.k8s.io/v1
    metadata:
      name: sa-view-binding
      namespace: namespaceX
    subjects:
    - kind: ServiceAccount
      name: pod-viewer
      namespace: namespaceX
    roleRef:
      kind: Role
      name: view
      apiGroup: rbac.authorization.k8s.io
    ```

3. Use the following command to create the role binding for the service account:
     ```bash
     oc create -f sa-rolebinding.yaml
     ```

4. Check that the service account and role binding have been created:
     ```bash
     oc get serviceaccounts
     oc get rolebindings
     ```

5. Use the service account to verify that it can view and list pods in the `namespaceX` namespace:
     ```bash
     oc auth can-i get pods --as=system:serviceaccount:namespaceX:pod-viewer -n namespaceX
     ```
   Ensure that the service account cannot modify resources:
     ```bash
     # Attempting to delete a pod should fail
     oc auth can-i delete pod --as=system:serviceaccount:namespaceX:pod-viewer -n namespaceX
     ```
