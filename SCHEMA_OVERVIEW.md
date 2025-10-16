# Database Schema Overview

## Schema Distribution

The migrations use **two main schemas**:

### 1. **public** (Default PostgreSQL Schema)
Used for: Facilities Management Domain

#### Tables in `public` schema:
- `logs` - Operation logs
- `machines` - Facility equipment
- `parts` - Machine components
- `reports` - Maintenance reports
- `users` - Authentication users (created in V4)
- `roles` - User roles (created in V4)
- `user_roles` - User-role mappings (created in V4)
- `products` - *(Legacy from V1/V2, may conflict)*

#### Migrations affecting `public`:
- **V1** - Creates users, products tables
- **V2** - Drops and recreates users, products tables
- **V3** - Creates logs, machines, parts, reports tables
- **V4** - Drops old users table, creates new auth users, roles, user_roles
- **V5** - Populates logs, machines, parts, reports with sample data

### 2. **inventory** (Custom Schema)
Used for: Inventory Management Domain

#### Tables in `inventory` schema:
- `inventory.items` - Inventory items master data
- `inventory.stock_levels` - Current stock quantities (1:1 with items)
- `inventory.stock_ledger` - Stock transaction history

#### Migrations affecting `inventory`:
- **V3** - Creates the inventory schema and all its tables
- **V5** - Populates items, stock_levels, stock_ledger with sample data

## Migration Sequence Diagram

```
┌─────────────────────────────────────────────────────────────────────────┐
│ Application Startup → Flyway Detects & Executes Migrations             │
└──────────────────────────────────────┬──────────────────────────────────┘
                                      │
                    ┌─────────────────┼─────────────────┐
                    │                 │                 │
               PUBLIC SCHEMA      INVENTORY SCHEMA   AUTH SCHEMA
                    │                 │                 │
        ┌───────────┴───────────┐     │       ┌─────────┴──────────┐
        │                       │     │       │                    │
        V                       V     V       V                    V
       V1/V2              V3 (logs,   V3     V4 (users,
    (users,           machines,     creates  roles,
    products)         parts,      inventory   user_roles)
                      reports)      schema
        │                │          │          │
        └────────────────┼──────────┴──────────┘
                         │
                         V
                   V5 (POPULATE)
                  Sample Data
                  ├─ Logs
                  ├─ Machines
                  ├─ Parts
                  ├─ Reports
                  ├─ Items
                  ├─ Stock Levels
                  ├─ Stock Ledger
                  ├─ Roles
                  └─ Users
```

## Data Flow

### Facilities Domain (public schema)
```
V3: CREATE
├─ logs table
├─ machines table
├─ parts table (FK → machines)
└─ reports table (FK → logs, parts, machines)

↓

V5: POPULATE
├─ 4 log records
├─ 5 machine records
├─ 6 part records
└─ 4 report records
```

### Inventory Domain (inventory schema)
```
V3: CREATE inventory SCHEMA
├─ inventory.items table (UUID PK)
├─ inventory.stock_levels table (1:1 FK → items)
└─ inventory.stock_ledger table (FK → items)

↓

V5: POPULATE
├─ 8 item records with SKUs
├─ Stock levels for each item
└─ Stock transaction history
```

### Authentication Domain (public schema)
```
V4: CREATE
├─ users table (replaces V1/V2 users)
├─ roles table
└─ user_roles junction table

↓

V4: AUTO-POPULATE
├─ 3 default roles
└─ 1 default admin user

(V5 doesn't modify auth tables)
```

## Schema Usage Summary

| Component | Schema | Migration | Tables | Sample Data |
|-----------|--------|-----------|--------|-------------|
| Facilities | public | V3 | logs, machines, parts, reports | V5 |
| Inventory | inventory | V3 | items, stock_levels, stock_ledger | V5 |
| Authentication | public | V4 | users, roles, user_roles | V4 |

## Key Points

1. **Two Active Schemas**:
   - `public` - Default schema (PostgreSQL default)
   - `inventory` - Custom schema created explicitly in V3

2. **Explicit Schema References**:
   - Facilities tables: `logs`, `machines`, `parts`, `reports` (NO prefix = public schema)
   - Inventory tables: `inventory.items`, `inventory.stock_levels`, `inventory.stock_ledger` (explicit `inventory.` prefix)
   - Authentication tables: `users`, `roles`, `user_roles` (NO prefix = public schema)

3. **Schema Separation Benefits**:
   - Clear logical separation of concerns
   - Easy to manage inventory independently
   - Can apply different permissions per schema
   - Allows multiple teams to work independently

## SQL Examples

### Query Facilities Data (public schema)
```sql
SELECT * FROM public.logs;
SELECT * FROM public.machines;
SELECT * FROM public.parts;
SELECT * FROM public.reports;
```

### Query Inventory Data (inventory schema)
```sql
SELECT * FROM inventory.items;
SELECT * FROM inventory.stock_levels;
SELECT * FROM inventory.stock_ledger;
```

### Query Authentication Data (public schema)
```sql
SELECT * FROM public.users;
SELECT * FROM public.roles;
SELECT * FROM public.user_roles;
```

### Cross-Schema Query Example
```sql
-- Get items low on stock
SELECT i.sku, i.name, sl.level 
FROM inventory.items i
JOIN inventory.stock_levels sl ON i.id = sl.item_id
WHERE sl.level < i.reorder_point;

-- Get items used in recent reports
SELECT DISTINCT i.sku, i.name 
FROM inventory.items i
WHERE i.id IN (
    -- This would need a bridge table in a real scenario
    -- as reports reference machines/parts, not items directly
);
```
