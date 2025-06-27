# Library Management

This module is a Spring Boot application that provides a simple online library system. It demonstrates user registration, login and a basic catalog of books.

## Running the application

Ensure you have Java 17 and Maven installed. From this directory run:

```bash
mvn spring-boot:run
```

By default the application connects to a PostgreSQL database `librarydb` on `localhost`. Adjust the connection settings in `src/main/resources/application.yml` if needed.

## Database schema

The schema is initialized automatically from `src/main/resources/schema.sql` when the application starts. This script creates the necessary tables and inserts a few example books.

## Demo credentials

- **Admin:** `admin` / `admin123`
- **User:** `john_doe` / `user123`
