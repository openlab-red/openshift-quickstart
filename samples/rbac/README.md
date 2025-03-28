# Instructions for RBAC (Role-Based Access Control) Configuration

This guide demonstrates how to set up and test role-based access control in OpenShift.

## Steps

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