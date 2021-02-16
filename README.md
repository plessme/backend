# PlessMe backend implementation in Java Quarkus

PlessMe is a software system for managing documents in different scenarios.

In this repository the backend implmentation gets tracked.

## Getting Started

These instructions will get you a copy of the project up and running locally for development and testing purposes.

### Prerequisites local development

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

Additionally the following tools must be installed and confiured:

* JDK (recommended GraalVM for native executables) 20.3.1 or higher
* Maven 3.6 or higher
* Docker 20.10.2 or higher
* Docker-Compose  1.27.4 or higher
* Git 2.24.3 or higher
* VS Code or IntelliJ IDE with Java extensions and remote debugger

We highly recommend to install all SDKs (Java, Maven etc.) with [SDKMAN](https://sdkman.io/) for easy switching of local development environment.

You need to setup all dependencies with Docker(-Compose) for local development:

* MongoDB
* MongoDB-Express
* Keycloak

Start you dependencies for the backend by executing:

```bash
docker-compose -f src/main/docker/docker-compose.yaml up -d
```

#### Quarkus Profile

Since quarkus supports profiles for different configuration you have the set the 'local' profile that the configuration fit's for local development.

Set the following environment variable:

```bash
export QUARKUS_PROFILE=local
```

### Running the application in dev mode locally

You can run the application with Quarkus in dev mode that enables live coding using:

```bash
./mvnw quarkus:dev
```

**Use always the Maven wrapper during development.**

### Packaging and running the application

The application is packageable using `./mvnw quarkus:build`.

### Creating a native executable

You can create a native executable using: `./mvnw quarkus:build -Pnative`.

The compiled executeable can be found at the `target/` folder.

Or you can use Docker to build the native executable using a Docker container: `./mvnw package -Dnative -Dquarkus.native.container-build=true`.

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling#building-a-native-executable>.

### Build Docker image

After creating a native executable you can build a Docker image by executing:

```bash
docker build -t <image-tag> .
```

### Running the tests

The PlessMe backend is tested in this repository within two kinds of test levels: Unit tests and Integration tests.

#### Unit tests

Unit test can be executed locally and within any CI/CD pipeline without any precondition.

```bash
./mvnw clean test
```

#### Integration tests

In order to run Integration tests the backend must run with all dependencies configured and running proper.
This can be done by starting e.g. the development docker-compose.yaml file with `docker-compose up`.

The integration tests can be executed with:

```bash
./mvnw clean integration-test
```

#### Code coverage

Code coverage will be collected during testing within the Maven Jacoco Plugin.
The collection will be automatically executed by testing unit-tests and integration-tests with a singel command:

```bash
./mvnw clean verify
```

This will generate a merged report of unit-tests and integration-tests in the  `target/` folder.
Test executions and coverage collections will generate also HTML based reports in the `target/site` folder.

### Code formatting

Code formatting is an important quality aspect of good source code. Therefore a code formatter is added to this project called **spotless**.

Spotless is a powerfull and generic tool and supports various of standard code formats. For this projects spotless is added as Gradle plugin
and configured for [Google's Java Format](https://github.com/google/google-java-format).
To format the code before pushing to a remote branch execute:

```bash
./mvnw spotless:apply
```

To check if your code already corresponds to the desired format execute:

```bash
./mvnw spotless:check
```

This is also a good possibility to integrate code formatting into the CI/CD pipeline.

## Contributing

Contributors are welcome! Please give us a little bit time to prepare a CONTRIBUTING.md for you!

<!-- Add more information about contributing with an own CONTRIBUTING.md file and a code of conduct -->

## Authors

* Ken Brucksch ([bongladesch](https://github.com/bongladesch))
* Onur Sahin ([OdinValholl](https://github.com/OdinValholl))

## License

Not licensed yet. All rights reserved for the authors.

<!-- Add licensing with a license file and headers to all source files -->
