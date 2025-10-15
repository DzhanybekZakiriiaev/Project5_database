# Facilities and Inventory Management System

A comprehensive REST API-based system with role-based access control for managing facilities and inventory operations across different teams.

## Features

- **REST API** with comprehensive CRUD operations
- **Role-based Authentication** using JWT tokens
- **Team-based Access Control** with separate admin roles
- **Database Schema Management** with Flyway migrations
- **Swagger Documentation** for REST endpoints
- **Cross-team Access Control** with proper isolation

## Architecture

### Teams and Roles

#### Facilities Team
- **USER**: General access to CRUD operations on facilities data
- **FACILITIES_ADMIN**: Full access including database altering operations

#### Inventory Team  
- **USER**: General access to CRUD operations on inventory data
- **INVENTORY_ADMIN**: Full access including database altering operations

### Database Schemas

#### Facilities Schema (Public)
- `Machines`: Equipment and machinery
- `Parts`: Machine components
- `Logs`: Maintenance logs
- `Reports`: Maintenance reports

#### Inventory Schema
- `Items`: Inventory items with SKU tracking
- `Stock_Levels`: Current stock quantities
- `Stock_Ledger`: Stock movement history

## Getting Started

### Prerequisites
- Java 19
- PostgreSQL database
- Maven 3.6+

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd software-design-techniques
   ```

2. **Configure Database**
   Update `src/main/resources/application.properties` with your PostgreSQL connection details:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

3. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the Application**
   - **REST API Base**: http://localhost:8080/api
   - **Swagger UI**: http://localhost:8080/swagger-ui.html
   - **API Docs**: http://localhost:8080/api/docs

## Authentication

### Getting a JWT Token

**Sign Up** (Create new user):
```http
POST /api/auth/signup
Content-Type: application/json

{
  "username": "newuser",
  "email": "user@example.com", 
  "password": "password123",
  "role": ["USER"]
}
```

**Sign In** (Get JWT token):
```http
POST /api/auth/signin
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

Response:
```json
{
  "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
  "tokenType": "Bearer",
  "id": 1,
  "username": "admin",
  "email": "admin@example.com",
  "roles": ["ROLE_USER"]
}
```

### Using JWT Token

Include the token in REST API requests:
```http
GET /api/facilities/machines
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

## REST API Usage

### Facilities Team Operations

#### Machine Operations
```http
# Get all machines
GET /api/facilities/machines
Authorization: Bearer <jwt-token>

# Get specific machine
GET /api/facilities/machines/1
Authorization: Bearer <jwt-token>

# Search machines by name
GET /api/facilities/machines/search?name=conveyor
Authorization: Bearer <jwt-token>

# Create new machine
POST /api/facilities/machines
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "name": "Conveyor Belt A"
}

# Update machine
PUT /api/facilities/machines/1
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "name": "Conveyor Belt A - Updated"
}

# Delete machine
DELETE /api/facilities/machines/1
Authorization: Bearer <jwt-token>
```

#### Admin Operations (FACILITIES_ADMIN Role)
```http
# Recreate machine table (admin only)
POST /api/facilities/machines/admin/recreate-table
Authorization: Bearer <jwt-token>

# Backup data (admin only)
POST /api/facilities/machines/admin/backup-data
Authorization: Bearer <jwt-token>
```

### Inventory Team Operations

#### Item Operations
```http
# Get all items
GET /api/inventory/items
Authorization: Bearer <jwt-token>

# Get specific item
GET /api/inventory/items/{uuid}
Authorization: Bearer <jwt-token>

# Get item by SKU
GET /api/inventory/items/sku/SCR-001
Authorization: Bearer <jwt-token>

# Search items by name
GET /api/inventory/items/search?name=screw
Authorization: Bearer <jwt-token>

# Create new item
POST /api/inventory/items
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "sku": "SCR-001",
  "name": "Steel Screw 1/4 inch",
  "reorderPoint": 100
}
```

#### Stock Level Operations
```http
# Get stock level for item
GET /api/inventory/stock-levels/{item-uuid}
Authorization: Bearer <jwt-token>

# Update stock level
PUT /api/inventory/stock-levels/{item-uuid}
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "level": 150
}
```

#### Admin Operations (INVENTORY_ADMIN Role)
```http
# Truncate item table (admin only)
POST /api/inventory/items/admin/truncate-table
Authorization: Bearer <jwt-token>

# Backup inventory data (admin only)
POST /api/inventory/items/admin/backup-data
Authorization: Bearer <jwt-token>
```

## Security Model

### Role Permissions

| Operation | USER | FACILITIES_ADMIN | INVENTORY_ADMIN |
|-----------|------|------------------|-----------------|
| Facilities CRUD | ✅ | ✅ | ❌ |
| Inventory CRUD | ✅ | ❌ | ✅ |
| Facilities Admin Ops | ❌ | ✅ | ❌ |
| Inventory Admin Ops | ❌ | ❌ | ✅ |
| Cross-team Access | ✅ | ❌ | ❌ |

### Authentication Flow

1. **Login**: User authenticates with username/password
2. **Token Generation**: JWT token issued with user roles
3. **Request Authorization**: Token validated on each request
4. **Role-based Access**: Operations restricted based on user roles

## Database Schema

### User Authentication Tables
- `users`: User accounts and credentials
- `roles`: Available system roles
- `user_roles`: User-role assignments

### Facilities Tables (Public Schema)
- `Machines`: Equipment registry
- `Parts`: Machine components
- `Logs`: Maintenance logs
- `Reports`: Maintenance reports with relationships

### Inventory Tables (Inventory Schema)
- `Items`: Inventory catalog with SKU tracking
- `Stock_Levels`: Current stock quantities
- `Stock_Ledger`: Complete audit trail of stock movements

## Development

### Adding New Teams

1. **Create Domain Entities** in appropriate package
2. **Create Repositories** extending JpaRepository
3. **Create Services** with CRUD and admin operations
4. **Create GraphQL Resolvers** for queries and mutations
5. **Update Schema** with new types and operations
6. **Add Role Configuration** for access control

### Testing

```bash
# Run tests
mvn test

# Run with coverage
mvn test jacoco:report
```

## API Documentation

- **Swagger UI**: Interactive REST API documentation at `/swagger-ui.html`
- **OpenAPI Spec**: JSON spec at `/api/docs`
- **REST Endpoints**: All endpoints follow RESTful conventions with proper HTTP methods

## Default Credentials

The system creates a default admin user:
- **Username**: admin
- **Password**: admin123
- **Role**: USER 

⚠️ **Important**: Change default credentials in production!

## Troubleshooting

### Common Issues

1. **Database Connection**: Verify PostgreSQL is running and credentials are correct
2. **JWT Token Expired**: Re-authenticate to get new token
3. **Permission Denied**: Check user roles and operation permissions
4. **API Errors**: Use Swagger UI to test endpoints and view error responses

### Logs

Application logs are available in console output. Enable debug logging by adding:
```properties
logging.level.com.example.softwaredesigntechniques=DEBUG
```

## Contributing

1. Fork the repository
2. Create feature branch
3. Commit changes
4. Push to branch
5. Create Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.