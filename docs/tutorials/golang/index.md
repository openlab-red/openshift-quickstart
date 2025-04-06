# Golang REST API with PostgreSQL

This tutorial guides you through building and running a simple REST API using Golang and PostgreSQL.

## Tutorial

> The code examples and instructions in this tutorial are located under the `openshift-quickstart` project in the `tutorials/golang` directory.  
> Ensure you are in this directory before executing the commands.

1. Navigate to the Tutorial Directory
```bash
cd openshift-quickstart/tutorials/golang
```

2. Or open a New Terminal

---

## 🚀 Features

### Backend (Golang REST API)
- Provides RESTful endpoints.
- Secure database interactions using prepared statements (PostgreSQL).
- Implements API health check (ping functionality).

---

## 🛠️ Initial Setup

### Prerequisite: Spin Up PostgreSQL Container on Laptop

To set up a PostgreSQL container locally, follow these steps:

1. **Run PostgreSQL Container:**

Start a new PostgreSQL container with the following command:

```bash
podman run -d -v $(pwd):/projects -e POSTGRESQL_USER=user -e POSTGRESQL_PASSWORD=pass -e POSTGRESQL_ROOT_PASSWORD=root -e POSTGRESQL_DATABASE=db -p 5432:5432 registry.redhat.io/rhel9/postgresql-16:latest
```

2. **Verify Container is Running:**

Check that your PostgreSQL container is running:

```bash
podman ps
```

### Database Configuration (First-time setup)

1. **Open Terminal on PostgreSQL container:**

2. **Access PostgreSQL Container:**

To access the running PostgreSQL container, execute the following command, replacing `<containerId>` with the actual container ID obtained from the `podman ps` command:

```bash
podman exec -it <containerId> bash
```

This command will open an interactive terminal session inside the PostgreSQL container, allowing you to run database commands directly.

3. **Initialize Database Schema:**

Execute the following command in the terminal (password: `pass`):

```bash
cd /projects
psql -d db -U user -W -f tutorials/golang/db/schema.sql
```

**Expected Output:**
```bash
Password: 
CREATE TABLE
```

---

## 🌐 Backend Development (Golang)

### Install Dependencies

Navigate to the backend directory and install dependencies:

```bash
cd app
go mod tidy
```

### Start Backend Server (Live Coding)

Run the backend server with live reload using `air` (or similar tool):

```bash
air
```

> **Note:** (DevSpaces only) Click "Yes" if prompted to expose the application outside the workspace.

### Explore API Endpoints

Access the API endpoints directly via your browser or API testing tools like Postman or curl.

---

## ✅ Testing the Application

1. **Verify Functionality:**
   - Check message retrieval and display.
   - Test adding new messages.
   - Use ping functionality to verify API health.

Example curl commands:

- Health check:
```bash
curl http://localhost:8080/api/ping
```

- Retrieve messages:
```bash
curl http://localhost:8080/api
```

- Add a new message:
```bash
curl -X POST -H "Content-Type: application/json" http://localhost:8080/api/add/Hello
```

---

🎉 **Congratulations!** You've successfully set up and tested your Golang REST API with PostgreSQL.

---

## 🚀 Deploying Backend on OpenShift using Helm

### Steps to Deploy

1. **Navigate to the Backend Helm Chart Directory:**

Change to the directory containing the Helm chart for the backend:

```bash
cd openshift-quickstart-manifest/golang/helm
```

2. **Deploy the Backend using Helm:**

Use the following command to deploy the backend application on OpenShift:

```bash
helm dependency build
helm install golang-backend .
```

This command will deploy the backend application using the Helm chart located in the current directory.

3. **Verify Deployment:**

Check the status of the deployed pods to ensure everything is running smoothly:

```bash
oc get pods -lapp.kubernetes.io/instance=golang-backend
```

You should see the backend and postgres pods up and running.

4. **Access the Backend Service:**

Once deployed, you can access the backend service using the route created by OpenShift. Retrieve the route with:

```bash
oc get routes golang-backend
```

Use the URL provided to interact with your backend API.


## 🚀 Bonus: Switch the Angular Frontend to Use the Golang Backend
