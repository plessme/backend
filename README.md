# PlessMe backend implementation in Java Quarkus

PlessMe is a software system for managing documents in different scenarios.

In this repository the backend implmentation gets tracked.

## Getting Started

These instructions will get you a copy of the project up and running locally and on any remote Kubernetes cluster for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites local development

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

Additionally the following tools must be installed and confiured:

* JDK (recommended GraalVM for native executables) 20.0.0 or higher
* Gradle 6.0 or higher
* Docker 19.03.8 or higher
* Docker-Compose  1.23.1 or higher
* Git 2.17.1 or higher
* Postman 7.21.2 or higher
* VS Code or IntelliJ IDE with Java extensions and remote debugger

We highly recommend to install all SDKs (Java, Gradle etc.) with [SDKMAN](https://sdkman.io/) for easy switching of local development environment.

You need to setup all dependencies with Docker(-Compose) for local development:

* MongoDB
* MongoExpress (MongoDB GUI)
* Keycloak
* Postgres

Start you dependencies for the backend by executing:

```bash
docker-compose -f src/main/docker/docker-compose.yaml up
```

### Prerequisites for remote Kubernetes development

* Kubectl 1.15.4 or higher
* Skaffold 1.7.0 or higher

You need a Kubernetes cluster with following configured:

<!-- TODO example to configure Kubernetes for remote development -->

### Running the application in dev mode locally

You can run the application with Quarkus in dev mode that enables live coding using:

```bash
./gradlew quarkusDev
```

**Use always the Gradle wrapper during development.**

### Running the application in dev mode remote on Kubernetes

You can run the application with Skaffold and Quarkus in dev mode that enables live coding on a remote Kubnerets cluster.

```bash
skaffold dev --tail
```

### Debug the application

While developing in dev mode (quarkus & skaffold) the application listens to port 5005 for remote debugging.
In order to provide remote debugging during Kubernetes remote developing there is an additional Kubernetes service
placed at `src/main/kubernetes/backend` to map the debug port of the application within a node port to port 30000
of any Kubernetes worker node.

#### VS Code remote debugging

If developing with Visual Studio Code you can simply use the build in debugger by placing a `launch.json` file in the `.vscode/`
folder in the root of this project with the following content:

```json
{
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "Debug Remote (Attach)",
            "projectName": "plessme-backend",
            "request": "attach",
            "hostName": "<k8s-worker-ip>",
            "port": 30000
        }
    ]
}
```

### Packaging and running the application

The application is packageable using `./gradlew quarkusBuild`.
<!---freshmark SECTION
output = "It produces the executable `plessme-backend-{{version}}-runner.jar` file in `build` directory.";
-->
It produces the executable `plessme-backend-0.1.0-SNAPSHOT-runner.jar` file in `build` directory.
<!---freshmark /SECTION -->
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `build/lib` directory.
<!---freshmark SECTION
output = "The application is now runnable using `java -jar build/plessme-backend-{{version}}-runner.jar`.";
-->
The application is now runnable using `java -jar build/plessme-backend-0.1.0-SNAPSHOT-runner.jar`.
<!---freshmark /SECTION -->
If you want to build an _über-jar_, just add the `--uber-jar` option to the command line:

```bash
./gradlew quarkusBuild --uber-jar
```

### Creating a native executable

You can create a native executable using: `./gradlew buildNative`.

Or you can use Docker to build the native executable using: `./gradlew buildNative --docker-build=true`.
<!---freshmark SECTION
output = "You can then execute your binary: `./build/plessme-backend-{{version}}-runner`";
-->
You can then execute your binary: `./build/plessme-backend-0.1.0-SNAPSHOT-runner`
<!---freshmark /SECTION -->
If you want to learn more about building native executables, please consult <https://quarkus.io/guides/gradle-tooling#building-a-native-executable> .

### Running the tests

The PlessMe backend is tested in this repository within two kinds of test levels: Unit tests and Full-API tests.

#### Unit tests

Unit test can be executed locally and within any CI/CD pipeline.

```bash
./gradlew test
```

#### Full-API tests

In order to run Full-API tests the backend must run with all dependencies configured and running proper.
These tests are created with Postman and can be executed within Postman on your local machine or with Newman (Postman as Docker image for CI/CD) execution.

<!-- TODO add description how to handle Postman test collections -->

### Code formatting

Code formatting is an important quality aspect of good source code. Therefore a code formatter is added to this project called **spotless**.

Spotless is a powerfull and generic tool and supports various of standard code formats. For this projects spotless is added as Gradle plugin
and configured for [Google's Java Format](https://github.com/google/google-java-format).
To format the code before pushing to a remote branch execute:

```bash
./gradlew spotlessApply
```

To check if your code already corresponds to the desired format execute:

```bash
./gradlew spotlessCheck
```

This is also a good possibility to integrate code formatting into the CI/CD pipeline.

## Versioning

This project

## Continuous Integration/Deployment

This project is also prepared for CI/CD with:

* Jenkins CI-Server
* Jfrog-Container-Registry (JCR)
* Github as SCM

The pipeline configuration is described in the `Jenkinsfile`.

<!-- Add more information about CI/CD process -->

### Git-Branching modell

### Versioning automation

### Docker image creation with Kaniko

## Contributing

Contributors are welcome! Please give us a little bit time to prepare a CONTRIBUTING.md for you!

<!-- Add more information about contributing with an own CONTRIBUTING.md file and a code of conduct -->

## Authors

* Ken Brucksch ([bongladesch](https://github.com/bongladesch))
* Onur Sahin ([OdinValholl](https://github.com/OdinValholl))

## License

Not licensed yet. All rights reserved for the authors.

<!-- Add licensing with a license file and headers to all source files -->
