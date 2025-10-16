# Database Migrations Guide

## Overview

This application uses **Flyway** for database version control and schema management. All migrations are automatically executed when the Spring Boot application starts.

## Migration Files

Migrations are located in: `src/main/resources/db/migration/`

### V1__Create_base_tables.sql
**Purpose**: Trial migration file - creates sample users and products tables
**Status**: Superseded by V4 (creates conflicting users table)
**Tables Created**:
- `users` - User records with name and audit fields
- `products` - Product inventory with pricing
**Indexes**: Created for username, email, and product queries
**Sample Data**: Inserts 3 sample users and 3 products

### V2__Force_recreate_tables.sql
**Purpose**: Forces recreation of base tables (drops and recreates)
**Status**: Superseded by V4 (creates conflicting users table)
**Tables Recreated**:
- `users` - User records (recreated)
- `products` - Product inventory (recreated)
**Sample Data**: Inserts 3 sample users and 3 products

### V3__Facilities_and_Inventory_Mgmt_base_schema.sql
**Purpose**: Core schema for Facilities and Inventory management
**Status**: Active - defines the main schema structure
**Databases**:
- `pgcrypto` extension - For UUID generation

#### Public Schema (Facilities Domain)
**Tables**:
- `logs` - Daily operation and maintenance logs
  - Fields: `id` (PK), `date`
  
- `machines` - Facility machines inventory
  - Fields: `id` (PK), `name` (UNIQUE)
  
- `parts` - Machine components
  - Fields: `id` (PK), `name`, `machine_id` (FK - nullable)
  
- `reports` - Maintenance/operation reports
  - Fields: `id` (PK), `report_text`, `log_id` (FK), `part_id` (FK - nullable), `machine_id` (FK - nullable), `needs_repair`

#### Inventory Schema
**Tables**:
- `items` - Master inventory items
  - Fields: `id` (UUID PK), `sku` (UNIQUE), `name`, `reorder_point`, `created_at`, `updated_at`
  
- `stock_levels` - Current stock levels (1:1 with items)
  - Fields: `item_id` (UUID PK, FK), `level`, `updated_at`
  
- `stock_ledger` - Immutable transaction history
  - Fields: `id` (UUID PK), `item_id` (FK), `delta`, `reason`, `ref_id` (UNIQUE), `created_at`

**Indexes**: Created for performance on foreign keys and unique fields

### V4__Create_user_authentication_tables.sql
**Purpose**: Authentication and role-based access control (RBAC)
**Status**: Active - defines security schema
**Tables**:
- `users` - Authentication users (replaces V1/V2 users table)
  - Fields: `id` (BIGSERIAL PK), `username` (UNIQUE), `password`, `email` (UNIQUE), `enabled`, `created_at`, `updated_at`
  
- `roles` - Application roles
  - Fields: `id` (BIGSERIAL PK), `name` (UNIQUE), `description`, `created_at`
  
- `user_roles` - User-role assignments (junction table)
  - Fields: `user_id` (FK), `role_id` (FK), `created_at`

**Default Roles**:
- `USER` - General user with CRUD access to all schemas
- `FACILITIES_ADMIN` - Facilities team administrator with database altering permissions
- `INVENTORY_ADMIN` - Inventory team administrator with database altering permissions

**Default Users**:
- `admin` user with `USER` role (password: `$2a$10$8.Un...` - bcrypt encoded `admin123`)

**Foreign Keys**: User-cascade deletes on users/roles

### V5__Populate_sample_data.sql
**Purpose**: Populates realistic sample data for testing and demonstration
**Status**: Active - provides test data

#### Facilities Sample Data
**Logs** (4 records):
- Logs from the past 5 days to current date

**Machines** (5 records):
- Industrial Conveyor Belt A
- Hydraulic Press Unit 1
- CNC Milling Machine 01
- Assembly Robot Arm 02
- Packaging Machine Unit B

**Parts** (6 records):
- Motor Bearing, Hydraulic Pump, Spindle Assembly, Control Board, Drive Belt, Electrical Connector
- Most parts linked to specific machines

**Reports** (4 records):
- Reports of maintenance, repairs, and operations linked to logs and machines

#### Inventory Sample Data
**Items** (8 records):
- Motor Oil 5L (SKU-001) - Qty: 35
- Industrial Bearings (SKU-002) - Qty: 8
- Hydraulic Fluid 10L (SKU-003) - Qty: 15
- Electrical Connectors Box (SKU-004) - Qty: 45
- Drive Belts Assortment (SKU-005) - Qty: 18
- Control Board PCB (SKU-006) - Qty: 4
- Lubricating Grease 500g (SKU-007) - Qty: 60
- Replacement Spindle Assembly (SKU-008) - Qty: 1

**Stock Levels**: Initialized with starting quantities

**Stock Ledger**: 
- Initial stock entries for all items
- Additional movements tracking maintenance usage, repairs, and lubrication

## How Migrations Work

1. **Automatic Execution**: When the Spring Boot application starts, Flyway automatically:
   - Checks the `flyway_schema_history` table
   - Identifies missing or pending migrations
   - Executes them in version order (V1, V2, V3, etc.)

2. **Version Naming**: `V{number}__{description}.sql`
   - Must start with `V`
   - Followed by version number (1, 2, 3, etc.)
   - Double underscore separator
   - SQL extension

3. **Idempotency**: 
   - Each migration uses `IF NOT EXISTS` or `ON CONFLICT` clauses
   - Safe to run multiple times
   - Only executes once per database instance

4. **Configuration**: Set in `application.properties`:
   ```properties
   spring.flyway.locations=classpath:db/migration
   spring.flyway.enabled=true
   ```

## Database Population Status

✅ **V3** - Facilities and Inventory tables created
✅ **V4** - Authentication and RBAC tables created with default roles
✅ **V5** - Sample data populated for testing and demonstration

### What Gets Populated:
- ✅ 4 facility logs
- ✅ 5 machines with maintenance reports
- ✅ 6 machine parts
- ✅ 4 maintenance/operation reports
- ✅ 8 inventory items
- ✅ Stock levels for all items
- ✅ Stock transaction history
- ✅ 3 default roles (USER, FACILITIES_ADMIN, INVENTORY_ADMIN)
- ✅ 1 default admin user

## Running Migrations

### Automatic (Recommended)
```bash
./mvnw clean spring-boot:run
```
Migrations run automatically on application startup.

### Manual with Maven
```bash
./mvnw flyway:migrate
```
Requires database configuration in `application.properties`

## Database Connection

For migrations to work, ensure your `application.properties` has valid database credentials:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQL9Dialect
```

## Troubleshooting

**Issue**: Migrations not running
- Check `application.properties` database URL and credentials
- Verify PostgreSQL is running on the configured host/port
- Check Flyway is enabled: `spring.flyway.enabled=true`

**Issue**: "Table already exists"
- This is normal - migrations use `IF NOT EXISTS`
- No action needed, migration will skip

**Issue**: Foreign key constraint violations
- Check V3 tables are created before V4 migrations run
- Verify dependent data is inserted in correct order

## Next Steps

After migrations run successfully:
1. Verify tables exist: Check PostgreSQL database
2. Test APIs: Use Swagger UI at http://localhost:8080/swagger-ui/index.html
3. Query sample data: Use provided REST endpoints
4. Review roles/users: Check authentication setup with default admin user
