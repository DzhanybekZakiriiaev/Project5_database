# Project5 Database Module

Welcome to the API & schema docs for the Database Team.

## What is this?
- Versioned PostgreSQL schema managed by Flyway (V1–V3).
- Integrated **Facilities** (facilities schema) and **Inventory Management** (inventory schema).

## Quick Links
- Executive Summary (landing): `../index.html`
- GitHub Repository: https://github.com/DzhanybekZakiriiaev/Project5_database

## Schemas
- **Facilities**: `Machines`, `Parts`, `Logs`, `Reports`
- **inventory**: `Items`, `Stock_Levels`, `Stock_Ledger`

## Team Notes
- Run `BaseDatabaseAccessApplication` to apply migrations.
- Migrations live in `src/main/resources/db/migration`.
