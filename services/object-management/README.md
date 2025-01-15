# Object Management Service

This project uses Quarkus, the Supersonic Subatomic Java Framework, to deliver a high-performance, lightweight Java application designed for modern cloud-native architectures.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Service Overview

This application implements an **Object Management Service** that handles the organization and tracking of household objects. The service supports:

- **Category Management**: Organize objects into categories.
- **Object Storage**: Maintain records of stored objects, including their properties and descriptions.
- **Reordering Integration**: Store URLs for quick reordering of objects.
- **Custom Cycles**: Track reorder intervals for recurring purchases.

### Architecture
The service is built using a **hexagonal architecture** (also known as ports-and-adapters) to ensure modularity and scalability. Key components include:

1. **Domain Layer** - Encapsulates business logic through domain models like `Category` and `StorageObject`.
2. **Application Layer** - Provides services like `ListCategoryService` and `ListStorageObjectService` to handle use cases.
3. **Adapter Layer** - Includes JPA-based persistence repositories and RESTful controllers for external interaction.
4. **Infrastructure Layer** - Uses PostgreSQL for data storage and Quarkus for dependency injection and runtime optimizations.

### Technologies Used
- **Quarkus** - Lightweight and cloud-native Java framework.
- **PostgreSQL** - Relational database backend.
- **Hibernate ORM with Panache** - Simplifies data access with JPA.
- **Jackson** - For TOML serialization and deserialization.

## Running the application in dev mode

You can run your application in dev mode, which enables live coding, using:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_** Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using:
```shell script
java -jar target/quarkus-app/quarkus-run.jar
```

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using:
```shell script
java -jar target/*-runner.jar
```

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with:
```shell script
./target/code-with-quarkus-1.0.0-SNAPSHOT-runner
```

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

