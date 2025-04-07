#!/bin/bash

# Set max number of users
# Get max users from command line argument, default to 25 if not provided
MAX_USERS=${1:-25}

for i in $(seq 1 $MAX_USERS); do
    NS="devspaces-user${i}"
    USER="user${i}"

    # Create namespace
    oc create namespace $NS 2>/dev/null || continue

    # Add labels
    oc label namespace $NS \
        app.kubernetes.io/component=workspaces-namespace \
        app.kubernetes.io/part-of=che.eclipse.org
        
    # Add annotations  
    oc annotate namespace $NS \
        che.eclipse.org/username=$USER
        
    # Add admin role to user
    oc adm policy add-role-to-user admin $USER -n $NS
done