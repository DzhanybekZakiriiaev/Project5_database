# Base Database Access Library

This project has been repurposed as a base database access library that provides common components for database operations in Spring Boot applications.

## Components Included

### Base Entity Classes
- **BaseEntity**: Abstract base class with ID field and basic equals/hashCode implementation
- **AuditedEntity**: Extends BaseEntity with created/updated timestamp auditing
- **RemovalEntity**: Extends AuditedEntity with soft delete functionality

### Base Repository
- **BaseRepository**: Generic repository interface extending JpaRepository with Long ID type

### Base Service Layer
- **BaseService**: Generic service interface with CRUD operations
- **DefaultBaseService**: Default implementation of BaseService with transaction support

### Exception Handling
- **NotFoundException**: Custom exception for when entities are not found

## Usage

1. Extend the base entity classes for your domain entities
2. Create repository interfaces extending BaseRepository
3. Create service interfaces extending BaseService
4. Implement services extending DefaultBaseService

## Dependencies

- Spring Boot Data JPA
- Spring Boot JDBC
- Spring Boot Validation
- MySQL Connector
- Flyway for database migrations
- Lombok for reducing boilerplate code

## Configuration

The application is configured to use MySQL database with the following default settings:
- Database: `base_database`
- Port: 8080
- JPA auditing is enabled

Update `application.properties` to match your database configuration.
