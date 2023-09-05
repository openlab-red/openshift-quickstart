# OpenShift Quickstart

## Laptop

### Git Clone

1. Open your terminal.
2. Navigate to the directory where you want to clone the project.
3. Run the following command to clone the Git repository:
   ```
   git clone https://github.com/openlab-red/openshift-quickstart.git
   ```
   Replace `https://github.com/openlab-red/openshift-quickstart.git` with the actual URL of the Git repository you want to clone.

### Open the Project with Visual Studio Code
1. Navigate to the project directory:
   ```
   cd openshift-quickstart
   ```
   Replace `openshift-quickstart` with the name of the directory where you cloned the Git repository.
2. Open Visual Studio Code:
   ```
   code .
   ```

### Build the Java Application and Launch quarkus:dev Mode
1. In Visual Studio Code, open the integrated terminal.
2. Navigate to the root directory of your Java application project.
3. Build the application (assuming you have the necessary build tools):
   ```
   ./mvnw clean package
   ```
4. Start the application in Quarkus development mode:
   ```
   ./mvnw quarkus:dev
   ```

### Check if the App is Running on localhost:8080
1. Open a web browser.
2. Navigate to `http://localhost:8080` in the address bar.
3. Verify that your Java application is running successfully.

### Deploy with the Quarkus Kubernetes Plugin to OpenShift Platform
1. To trigger building and deploying a container image you need to enable the `quarkus.kubernetes.deploy` flag (the flag is disabled by default - furthermore it has no effect during test runs or dev mode). This can be easily done with the command line:
    ```
    ./mvnw clean package -Dquarkus.kubernetes.deploy=true
    ```
### Deploy with Tilt to OpenShift Platform
1. Ensure you have Tilt installed and properly configured.
2. Create a Tiltfile (usually named `Tiltfile`) in your project directory to define your deployment configuration.
3. Inside the Tiltfile, specify the configuration for deploying your application to OpenShift using Tilt.

4. In your terminal, navigate to the project directory.

    Run Tilt to start the deployment:
    ```
    tilt up
    ```

    Tilt will automatically deploy your application to OpenShift based on your configuration.

    Monitor the deployment progress and logs in the Tilt UI or terminal.

5. Access Your Deployed Application. Once the deployment is successful, access your deployed application through the provided route.
