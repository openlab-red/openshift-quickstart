# Network Policy

1. Test communication, everything is allowed inside a namespace.

    From the python pod

    ```bash
    curl -I http://openshift-quickstart
    ```

    From the java pod

    ```bash
    curl -I http://python-server-service:8000
    ```

2. Apply the deny policy to openshift-quickstart

    `oc create -f deny.yaml`


3. Test from the python pod.

    `curl -I http://openshift-quickstart -v`

4. Create the allow policy to openshift-quickstart`

    `oc create -f allow.yaml`


5. Test Again

    `curl -I http://openshift-quickstart`

6. Create a new python pod changing name and labels

    ```yaml
    ---
    apiVersion: v1
    kind: Pod
    metadata:
      name: python-server-second
      labels:
        app: python-server-second
    spec:
      containers:
      - name: python-server-container
        image: registry.access.redhat.com/ubi9/python-311
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
        livenessProbe:
          httpGet:
            path: /
            port: 8000
          initialDelaySeconds: 3
          periodSeconds: 3
    ```

7. Test from this new pod

    `curl -I http://openshift-quickstart -v`