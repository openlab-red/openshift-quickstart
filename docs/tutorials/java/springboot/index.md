# Spring Boot Sample Application

This tutorial guides you through building and running a simple REST API using SpringBoot and PostgreSQL.

## Tutorial

> The code examples and instructions in this tutorial are located under the `openshift-quickstart` project in the `tutorials/java/springboot` directory.  
> Ensure you are in this directory before executing the commands.

1. Navigate to the Tutorial Directory
```bash
cd openshift-quickstart/tutorials/java/springboot
```

2. Or open a New Terminal

---

## 🚀 Features

### Backend (SpringBoot REST API)
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

The SpringBoot application will automatically initialize the database schema on first startup. The schema creation is handled by Hibernate ORM using the entity classes defined in the application.

You can verify the schema creation by checking the application logs during startup:

---

## Building the Application

To build the application, use the following Maven command:

```bash
cd springboot
mvn clean package
```

This command will compile the project and package it into a JAR file located in the `target/` directory.

## Running the Application

You can run the application like normal java app.

```bash
java -jar target/boot-0.0.1-SNAPSHOT.jar
```

Or you can run your application in development mode, which enables live coding, using:

```bash
mvn spring-boot:run
```

Expose the endpoint and access the application.

![listening](../assets/springboot/listening.png)

## Debugging with VSCode

>
> It is already defined in the project.
>

1. **Run in VSCode**:
   - Open the project in VSCode.
   - Navigate to the `BootApplication.java` file.
   - Click on the `Run` button above the `main` method or use the `Run` menu.

## Debugging the Application

1. **Debug in VSCode**:
   - Set breakpoints in your Java files.
   - Open the `Run and Debug` panel in VSCode (Ctrl+Shift+D).
   - Click on `Run and Debug` and select `Java` environment.
   - The application will start in debug mode, and execution will pause at your breakpoints.

## Building the Container

To build a container for your Quarkus application, follow these steps:

### Steps

1. **Ensure the Application is Packaged**:
   Before building the Docker image, make sure your application is packaged. You can do this by running:

   ```bash
   mvn package
   ```

2. **Build the Image**:
   Use the following command to build the image. This command uses the Dockerfile located at `src/main/docker/Dockerfile.jvm`:

   ```bash
   podman build -f src/main/docker/Dockerfile.jvm -t boot:latest .
   ```

3. **Run the Docker Container**:
   Once the image is built, you can run the container using:

   ```bash
   podman run boot:latest -p 8080:8080 
   ```

These steps will help you build and run your Spring Boot application in a container, allowing for easy deployment and testing.

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

🎉 **Congratulations!** You've successfully set up and tested your Quarkus REST API with PostgreSQL.

---

## 🚀 Deploying Backend on OpenShift using Helm

### Steps to Deploy

1. **Navigate to the Backend Helm Chart Directory:**

Change to the directory containing the Helm chart for the backend:

```bash
cd openshift-quickstart-manifest/java/helm
```

2. **Deploy the Backend using Helm:**

Use the following command to deploy the backend application on OpenShift:

```bash
helm dependency build
helm install springboot-backend . -f values-boot.yaml
```

This command will deploy the backend application using the Helm chart located in the current directory.

3. **Verify Deployment:**

Check the status of the deployed pods to ensure everything is running smoothly:

```bash
oc get pods -lapp.kubernetes.io/instance=springboot-backend
```

You should see the backend and postgres pods up and running.

4. **Access the Backend Service:**

Once deployed, you can access the backend service using the route created by OpenShift. Retrieve the route with:

```bash
oc get routes springboot-backend
```

Use the URL provided to interact with your backend API.


## 🚀 Bonus: Switch the Angular Frontend to Use the Spring Boot Backend
