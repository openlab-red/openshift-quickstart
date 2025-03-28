# Instructions

1. With the admin user create a role `pod-reader.yaml`

    `oc create -f pod-reader.yaml -n pod-play`

2. Create the binding for the `user1`

    `oc create -f pod-reader-binding.yaml -n pod-play`

3. Show that can only see the pods nothing else.

4. Add the standard view binding.

    `oc adm policy add-role-to-user view user1 -n pod-play`

5. Show that can view everything on the namespace.