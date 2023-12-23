
```markdown
# Spring Boot Application with PostgreSQL and Redis Caching

## Overview

This is a sample Spring Boot application that uses PostgreSQL as the database and Redis for caching. The application demonstrates how to set up a Spring Boot project, integrate it with PostgreSQL, implement Redis caching, and use Docker Compose for managing PostgreSQL and Redis in Docker containers. Additionally, the application utilizes Testcontainers for integration testing.

## Prerequisites

- [Docker](https://www.docker.com/get-started)
- [Docker Compose](https://docs.docker.com/compose/install/)
- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-downloads.html)
- [Maven](https://maven.apache.org/download.cgi)

## Running the Application

1. Clone the repository:

   ```bash
   git clone https://github.com/MoamenZakariaMohamed/redis.git
   ```

2. Navigate to the project directory:

   ```bash
   cd spring-boot-postgres-redis
   ```

3. Build the application:

   ```bash
   mvn clean install
   ```

4. Start the application with Docker Compose:

   ```bash
   docker-compose up
   ```

5. The Spring Boot application should be accessible at [http://localhost:8080](http://localhost:8080).

## Testing

The project includes integration tests using Testcontainers. To run the tests:

```bash
mvn test
```

## Application Structure

- `src/main/java/com/study/redis`: Contains the main application code.
- `ssrc/test/java/com/study/redis`: Includes integration tests using Testcontainers.

## Configuration

- PostgreSQL configuration is in `src/main/resources/application.yml`.
- Redis configuration is in `src/main/resources/application.yml`.

## Docker Compose

- `docker-compose.yml` includes services for PostgreSQL and Redis.
- Use `docker-compose up` to start the Docker services.

