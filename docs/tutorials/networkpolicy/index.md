# Network Policy

## Tutorial

> The code examples and instructions in this tutorial are located under `openshift-quickstart` project in the
> `tutorials/networkpolicy` directory. 
>
> Ensure you are in this directory before executing the commands.
>

1. Navigate to the Tutorial Directory
    ```bash
    cd openshift-quickstart/tutorials/networkpolicy
    ```

2. Or open a New Terminal

---

### Steps

> Note: At this point, you should have at least one Python pods up and running in your namespace.

1. Deploy a new Python pod with a different name and labels to test network policies.

    Use the following YAML configuration:
    ```yaml
    ---
    apiVersion: v1
    kind: Pod
    metadata:
      name: python-server-second
      labels:
        app: python-server-second
    spec:
      securityContext:
        runAsNonRoot: true
        seccompProfile:
          type: RuntimeDefault
      containers:
      - name: python-server-container
        image: registry.redhat.io/ubi9/python-312
        ports:
        - containerPort: 8000
        command: ["python3", "-m", "http.server"]
        resources:
          limits:
            cpu: "0.5"
            memory: "512Mi"
          requests:
            cpu: "0.2"
            memory: "256Mi"
        securityContext:
          allowPrivilegeEscalation: false
          capabilities:
            drop: ["ALL"]
          readOnlyRootFilesystem: true
        livenessProbe:
          httpGet:
            path: /
            port: 8000
          initialDelaySeconds: 3
          periodSeconds: 3
    ```
2. Expose the `python-server-second` pod with a service to enable network communication.

    Use the following YAML configuration:
    ```yaml
    ---
    apiVersion: v1
    kind: Service
    metadata:
      name: python-server-second-service
    spec:
      selector:
        app: python-server-second
      ports:
        - protocol: TCP
          port: 8000
          targetPort: 8000
    ```

3. Verify that all communication is allowed within the namespace.

    Execute the following command to test connectivity to the `python-server-second-service` service.
    ```bash
    oc rsh python-server-pod
    curl -I http://python-server-second-service:8000
    exit
    ```
    Use this command to test connectivity to the `python-server-service` on port 8000.
    ```bash
    oc rsh python-server-second
    curl -I http://python-server-service:8000
    ```

4.  Restrict the traffic to your `python-server-service` service by applying a deny policy.
    ```bash
    oc create -f deny.yaml
    ```

5. Check the connectivity from the second Python pod to ensure the deny policy is in effect.
    ```bash
    curl -I http://python-server-service:8000 -v
    ```

6. Re-enable specific traffic to the `devspaces-userX` namespace by applying an allow policy.
    ```bash
    oc create -f allow.yaml
    ```

7. Verify that the allow policy is functioning correctly by testing connectivity again.

    Execute:
    ```bash
    curl -I http://python-server-service:8000
    ```

---

## Exercises

### Exercise 1: A third pod

1. Deploy a new Python pod named `python-server-third` to test network policies.

    Use the following YAML configuration:
    ```yaml
    ---
    apiVersion: v1
    kind: Pod
    metadata:
      name: python-server-third
      labels:
        app: python-server-third
    spec:
      securityContext:
        runAsNonRoot: true
        seccompProfile:
          type: RuntimeDefault
      containers:
      - name: python-server-container
        image: registry.redhat.io/ubi9/python-312
        ports:
        - containerPort: 8000
        command: ["python3", "-m", "http.server"]
        resources:
          limits:
            cpu: "0.5"
            memory: "512Mi"
          requests:
            cpu: "0.2"
            memory: "256Mi"
        securityContext:
          allowPrivilegeEscalation: false
          capabilities:
            drop: ["ALL"]
          readOnlyRootFilesystem: true
        livenessProbe:
          httpGet:
            path: /
            port: 8000
          initialDelaySeconds: 3
          periodSeconds: 3
    ```

2. Test the connectivity from the `python-server-third` pod to ensure the deny policy is in effect.
    ```bash
    oc rsh python-server-third
    curl -I http://python-server-service:8000 -v
    exit
    ```

3. Allow communication to the `python-server-third` pod by applying a new allow policy.

    Use the following YAML configuration:
    ```yaml
    apiVersion: networking.k8s.io/v1
    kind: NetworkPolicy
    metadata:
      name: allow-third-pod-communication
    spec:
      podSelector:
        matchLabels:
          app: python-server
      policyTypes:
      - Ingress
      ingress:
      - from:
        - podSelector:
            matchLabels:
              app: python-server-third
    ```

9. Enable the new allow policy to permit traffic from the `python-server-third` pod.
    ```bash
    oc create -f allow-third-pod.yaml
    ```

10. Test connectivity again to ensure the allow policy is functioning correctly.

    ```bash
    oc rsh python-server-third
    curl -I http://python-server-service:8000
    exit
    ```

---

### Exercise 2: Multi-tier Application Network Policies

1. Deploy a three-tier application setup with the following components:

    a. Frontend Pod (Web Server):
    ```yaml
    apiVersion: v1
    kind: Pod
    metadata:
      name: frontend-pod
      labels:
        tier: frontend
        app: multi-tier-app
    spec:
      securityContext:
        runAsNonRoot: true
        seccompProfile:
          type: RuntimeDefault
      containers:
      - name: frontend-container
        image: registry.redhat.io/ubi9/python-312
        ports:
        - containerPort: 8080
        command: ["python3", "-m", "http.server", "8080"]
        resources:
          limits:
            cpu: "0.5"
            memory: "512Mi"
          requests:
            cpu: "0.2"
            memory: "256Mi"
        securityContext:
          allowPrivilegeEscalation: false
          capabilities:
            drop: ["ALL"]
    ```

      b. API Pod (Application Layer):
      ```yaml
      apiVersion: v1
      kind: Pod
      metadata:
        name: api-pod
        labels:
          tier: api
          app: multi-tier-app
      spec:
        securityContext:
          runAsNonRoot: true
          seccompProfile:
            type: RuntimeDefault
        containers:
        - name: api-container
          image: registry.redhat.io/ubi9/python-312
          ports:
          - containerPort: 5000
            name: api
          command: ["python3", "-m", "http.server", "5000"]
          resources:
            limits:
              cpu: "0.5"
              memory: "512Mi"
            requests:
              cpu: "0.2"
              memory: "256Mi"
          securityContext:
            allowPrivilegeEscalation: false
            capabilities:
              drop: ["ALL"]
      ```

    c. Database Pod:
    ```yaml
    ---
    apiVersion: v1
    kind: ConfigMap
    metadata:
      name: db-config
      labels:
        tier: database
        app: multi-tier-app
    data:
      POSTGRESQL_USER: user
      POSTGRESQL_PASSWORD: pass
      POSTGRESQL_ROOT_PASSWORD: root
      POSTGRESQL_DATABASE: db
    ---
    apiVersion: v1
    kind: Pod
    metadata:
      name: db-pod
      labels:
        tier: database
        app: multi-tier-app
    spec:
      securityContext:
        runAsNonRoot: true
        seccompProfile:
          type: RuntimeDefault
      containers:
      - name: db-container
        image: registry.redhat.io/rhel9/postgresql-16:latest
        ports:
        - containerPort: 5432
        envFrom:
          - configMapRef:
              name: db-config
        volumeMounts:
          - mountPath: /var/lib/pgsql/data
            name: postgresdata
        resources:
          limits:
            cpu: "0.5"
            memory: "512Mi"
          requests:
            cpu: "0.2"
            memory: "256Mi"
        securityContext:
          allowPrivilegeEscalation: false
          capabilities:
            drop: ["ALL"]
      volumes:
        - name: postgresdata
          emptyDir: {}
    ```

2. Create corresponding services for each pod:

    ```yaml
    apiVersion: v1
    kind: Service
    metadata:
      name: frontend-service
    spec:
      selector:
        tier: frontend
      ports:
        - protocol: TCP
          port: 8080
          targetPort: 8080
    ---
    apiVersion: v1
    kind: Service
    metadata:
      name: api-service
      labels:
        tier: api
    spec:
      selector:
        tier: api
      ports:
        - protocol: TCP
          port: 5000
          targetPort: 5000
    ---
    apiVersion: v1
    kind: Service
    metadata:
      name: db-service
    spec:
      selector:
        tier: database
      ports:
        - protocol: TCP
          port: 5432
          targetPort: 5432
    ```

3. Tasks:

    a. Create a network policy that:

      - Allows frontend to communicate only with the API layer
      - Allows API layer to communicate only with the database
      - Denies all other traffic between pods
      - Allows external traffic only to the frontend

    b. Test the connectivity between different layers:

      - Test frontend to API communication
      - Test frontend to database communication (should fail)
      - Test API to database communication
      - Test external access to frontend

4. First, let's create a default deny policy to ensure all traffic is blocked by default:

    ```yaml
    apiVersion: networking.k8s.io/v1
    kind: NetworkPolicy
    metadata:
      name: default-deny-all
    spec:
      podSelector:
        matchLabels:
          app: multi-tier-app
      policyTypes:
      - Ingress
      - Egress
    ```

5. Frontend Network Policy (allows incoming external traffic and outgoing to API):

      ```yaml
      apiVersion: networking.k8s.io/v1
      kind: NetworkPolicy
      metadata:
        name: frontend-policy
      spec:
        podSelector:
          matchLabels:
            tier: frontend
        policyTypes:
        - Ingress
        - Egress
        ingress:
        - {}  # Allows all incoming traffic
        egress:
        - {}
      ```

6. API Layer Network Policy (allows frontend traffic and connection to database):

    ```yaml
    apiVersion: networking.k8s.io/v1
    kind: NetworkPolicy
    metadata:
      name: api-policy
    spec:
      podSelector:
        matchLabels:
          tier: api
      policyTypes:
      - Ingress
      - Egress
      ingress:
      - from:
        - podSelector:
            matchLabels:
              tier: frontend
        ports:
        - protocol: TCP
          port: 5000
      egress: 
      - {}
    ```

7. Database Network Policy (only allows API layer access):

    ```yaml
    apiVersion: networking.k8s.io/v1
    kind: NetworkPolicy
    metadata:
      name: database-policy
    spec:
      podSelector:
        matchLabels:
          tier: database
      policyTypes:
      - Ingress
      ingress:
      - from:
        - podSelector:
            matchLabels:
              tier: api
        ports:
        - protocol: TCP
          port: 5432
    ```

8. Testing Commands:

```bash
# Test frontend to API communication (should succeed)
oc rsh frontend-pod
curl -I http://api-service:5000
exit

# Test frontend to database communication (should fail)
oc rsh frontend-pod
curl -I http://db-service:5432
exit

# Test API to database communication (should succeed)
oc rsh api-pod
curl -I http://db-service:5432
exit

```

9. Expected Results:

    - Frontend pod should only be able to communicate with the API service
    - API pod should only be able to communicate with the database service
    - Database pod should only accept connections from the API service
    - Monitoring pod should be able to access all services
    - Any other communication attempts should fail
