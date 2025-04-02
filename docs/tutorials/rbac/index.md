# RBAC (Role-Based Access Control)

## Tutorials

> The code examples and instructions in this tutorial are located in the `tutorials/rbac` directory. Ensure you are in this directory before executing the commands.

This guide demonstrates how to set up and test role-based access control in OpenShift.

### Steps

1. **Create the Pod Reader Role**
   As an admin user, create a role that allows reading pod information:
   ```bash
   oc create -f pod-reader.yaml -n pod-play
   ```

2. **Bind the Role to a User**
   Create a RoleBinding to assign the pod-reader role to user1:
   ```bash
   oc create -f pod-reader-binding.yaml -n pod-play
   ```

3. **Test Limited Access**
   Login as user1 and verify that you can:
   - View pods (`oc get pods`)
   - Cannot access other resources (services, routes, etc.)

4. **Grant Additional Permissions**
   Add the standard view role to allow broader access:
   ```bash
   oc adm policy add-role-to-user view user1 -n pod-play
   ```

5. **Verify Extended Access**
   Login as user1 again and confirm that you can now:
   - View all basic resources in the namespace
   - View pods, services, routes, configmaps, etc.
   - Cannot modify any resources (read-only access)

## Exercises

### Exercise 1: Create a Custom Role

**Objective:** Learn how to create a custom role with specific permissions.

1. **Define a Custom Role:**
   - Create a YAML file named `custom-role.yaml` with the following content to define a role that allows listing and watching pods:
     ```yaml
     kind: Role
     apiVersion: rbac.authorization.k8s.io/v1
     metadata:
       namespace: pod-play
       name: custom-pod-watcher
     rules:
     - apiGroups: [""]
       resources: ["pods"]
       verbs: ["list", "watch"]
     ```

2. **Create the Role:**
   - Apply the role using the following command:
     ```bash
     oc create -f custom-role.yaml -n pod-play
     ```

3. **Verify the Role:**
   - Check that the role has been created:
     ```bash
     oc get roles -n pod-play
     ```

### Exercise 2: Modify an Existing Role

**Objective:** Learn how to modify an existing role to add or remove permissions.

1. **Edit the Pod Reader Role:**
   - Open the `pod-reader.yaml` file and add permissions to list services:
     ```yaml
     kind: Role
     apiVersion: rbac.authorization.k8s.io/v1
     metadata:
       namespace: pod-play
       name: pod-reader
     rules:
     - apiGroups: [""]
       resources: ["pods"]
       verbs: ["get", "list", "watch"]
     - apiGroups: [""]
       resources: ["services"]
       verbs: ["list"]
     ```

2. **Update the Role:**
   - Apply the changes to update the role:
     ```bash
     oc apply -f pod-reader.yaml -n pod-play
     ```

3. **Verify the Changes:**
   - Check the updated role to ensure the changes were applied:
     ```bash
     oc get role pod-reader -o yaml -n pod-play
     ```

### Exercise 3: Create a RoleBinding for a Group

**Objective:** Learn how to bind a role to a group of users.

1. **Create a Group:**
   - Add users to a group named `dev-team`:
     ```bash
     oc adm groups new dev-team
     oc adm groups add-users dev-team user2 user3
     ```

2. **Create a RoleBinding:**
   - Create a YAML file named `group-rolebinding.yaml` to bind the `view` role to the `dev-team` group:
     ```yaml
     kind: RoleBinding
     apiVersion: rbac.authorization.k8s.io/v1
     metadata:
       name: view-binding
       namespace: pod-play
     subjects:
     - kind: Group
       name: dev-team
       apiGroup: rbac.authorization.k8s.io
     roleRef:
       kind: Role
       name: view
       apiGroup: rbac.authorization.k8s.io
     ```

3. **Apply the RoleBinding:**
   - Use the following command to create the role binding:
     ```bash
     oc create -f group-rolebinding.yaml -n pod-play
     ```

4. **Verify the RoleBinding:**
   - Check that the role binding has been created:
     ```bash
     oc get rolebindings -n pod-play
     ```
