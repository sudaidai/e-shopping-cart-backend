# Shopping Cart Backend

This project serves as the backend for a shopping cart application. It provides APIs for managing users, products, and shopping carts using GraphQL and Restful.

## Features

- User authentication and authorization using Spring Security and JWT tokens.
- Shopping cart management including adding/removing items and placing orders.
- GraphQL API for flexible data querying.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

Make sure you have the following software installed on your system:

- Java Development Kit (JDK) version 17 or higher
- Gradle
- Docker

### Create MySQL Container

```
docker build -t mysql
docker run -d -p 3306:3306 -v /docker/init.sql:/docker-entrypoint-initdb.d/init.sql mysql
```

### Running the Application
After successfully configuring the project, you can run the application using the following command:

```bash
./gradlew bootRun
```

The application will start and be accessible at http://localhost:8000.

### Built With

* Kotlin - Programming language
* Spring Boot - Framework for building Spring applications
* Spring Security - Authentication and access control framework
* JWT - JSON Web Tokens for authentication
* GraphQL - Query language for APIs
* Restful - Restful endpoints
* Gradle - Build automation tool
* Spring Data JPA - Data access framework for Spring applications



