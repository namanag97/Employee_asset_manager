# EHD Database Schema Documentation

## Overview

The EHD database uses PostgreSQL 14+ and follows a relational database design pattern. The schema consists of five main tables that handle hardware asset tracking, employee management, and assignment history.

## Tables

### 1. employees

Stores employee information for hardware assignments.

```sql
CREATE TABLE employees (
    employee_id VARCHAR(50) PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('Active', 'Inactive')),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);
```

#### Fields
- `employee_id`: Primary key, company-assigned employee identifier
- `full_name`: Employee's full name
- `email`: Unique email address
- `status`: Current employment status (Active/Inactive)
- `created_at`: Record creation timestamp
- `updated_at`: Last update timestamp

#### Indexes
```sql
CREATE INDEX idx_employee_email ON employees(email);
```

### 2. hardware_types

Lookup table for hardware categories.

```sql
CREATE TABLE hardware_types (
    type_id SERIAL PRIMARY KEY,
    type_name VARCHAR(255) NOT NULL UNIQUE
);
```

#### Fields
- `type_id`: Auto-incrementing primary key
- `type_name`: Unique hardware type name (e.g., Laptop, Monitor)

### 3. hardware_assets

Primary table for hardware inventory tracking.

```sql
CREATE TABLE hardware_assets (
    asset_id SERIAL PRIMARY KEY,
    asset_tag VARCHAR(100) UNIQUE NOT NULL,
    serial_number VARCHAR(255) UNIQUE NOT NULL,
    type_id INT NOT NULL REFERENCES hardware_types(type_id),
    make VARCHAR(100) NOT NULL,
    model VARCHAR(150) NOT NULL,
    specifications TEXT,
    status VARCHAR(50) NOT NULL CHECK (status IN ('Available', 'Assigned', 'In Repair', 'Damaged', 'Retired')),
    notes TEXT,
    current_employee_id VARCHAR(50) REFERENCES employees(employee_id),
    last_assignment_date TIMESTAMPTZ,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);
```

#### Fields
- `asset_id`: Auto-incrementing primary key
- `asset_tag`: Unique asset identifier
- `serial_number`: Unique serial number
- `type_id`: Foreign key to hardware_types
- `make`: Manufacturer name
- `model`: Model name/number
- `specifications`: Optional technical specifications
- `status`: Current asset status
- `notes`: Optional notes
- `current_employee_id`: Foreign key to employees (nullable)
- `last_assignment_date`: Date of last assignment
- `created_at`: Record creation timestamp
- `updated_at`: Last update timestamp

#### Indexes
```sql
CREATE INDEX idx_asset_status ON hardware_assets(status);
CREATE INDEX idx_asset_type ON hardware_assets(type_id);
CREATE INDEX idx_asset_employee ON hardware_assets(current_employee_id);
```

### 4. app_users

Stores system user accounts for authentication.

```sql
CREATE TABLE app_users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL CHECK (role IN ('ROLE_ADMIN')),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);
```

#### Fields
- `user_id`: Auto-incrementing primary key
- `username`: Unique username
- `email`: Unique email address
- `password_hash`: BCrypt-hashed password
- `role`: User role (currently only ROLE_ADMIN)
- `is_active`: Account status
- `created_at`: Record creation timestamp
- `updated_at`: Last update timestamp

#### Indexes
```sql
CREATE INDEX idx_user_email ON app_users(email);
```

### 5. assignment_history

Tracks all hardware assignments over time.

```sql
CREATE TABLE assignment_history (
    history_id SERIAL PRIMARY KEY,
    asset_id INT NOT NULL REFERENCES hardware_assets(asset_id),
    employee_id VARCHAR(50) NOT NULL REFERENCES employees(employee_id),
    assigned_by_user_id INT REFERENCES app_users(user_id),
    assignment_date TIMESTAMPTZ NOT NULL,
    return_date TIMESTAMPTZ,
    returned_by_user_id INT REFERENCES app_users(user_id),
    notes TEXT
);
```

#### Fields
- `history_id`: Auto-incrementing primary key
- `asset_id`: Foreign key to hardware_assets
- `employee_id`: Foreign key to employees
- `assigned_by_user_id`: Foreign key to app_users (who assigned)
- `assignment_date`: Date of assignment
- `return_date`: Date of return (nullable)
- `returned_by_user_id`: Foreign key to app_users (who processed return)
- `notes`: Optional notes about the assignment

#### Indexes
```sql
CREATE INDEX idx_assignment_asset ON assignment_history(asset_id);
CREATE INDEX idx_assignment_employee ON assignment_history(employee_id);
```

## Relationships

1. **Hardware Assets to Hardware Types**
   - Many-to-One relationship
   - Each hardware asset belongs to one hardware type
   - Hardware type can have many assets

2. **Hardware Assets to Employees**
   - Many-to-One relationship
   - Each hardware asset can be assigned to one employee at a time
   - Employee can have many assets assigned

3. **Assignment History to Hardware Assets**
   - Many-to-One relationship
   - Each assignment history record belongs to one hardware asset
   - Hardware asset can have many assignment history records

4. **Assignment History to Employees**
   - Many-to-One relationship
   - Each assignment history record belongs to one employee
   - Employee can have many assignment history records

5. **Assignment History to App Users**
   - Many-to-One relationship (for both assigned_by and returned_by)
   - Each assignment history record can reference two different app users
   - App user can have many assignment history records

## Constraints

1. **Primary Keys**
   - All tables have a primary key
   - Most use SERIAL (auto-incrementing)
   - employees table uses VARCHAR for employee_id

2. **Foreign Keys**
   - All foreign key relationships are enforced
   - ON DELETE behavior is default (RESTRICT)
   - ON UPDATE behavior is default (CASCADE)

3. **Unique Constraints**
   - employee_id in employees
   - email in employees
   - asset_tag in hardware_assets
   - serial_number in hardware_assets
   - type_name in hardware_types
   - username in app_users
   - email in app_users

4. **Check Constraints**
   - status in employees (Active/Inactive)
   - status in hardware_assets (Available/Assigned/In Repair/Damaged/Retired)
   - role in app_users (ROLE_ADMIN)

## Triggers

The following triggers are implemented to maintain data integrity:

1. **Updated At Timestamp**
   - Automatically updates the updated_at field when a record is modified
   - Applied to all tables with updated_at field

```sql
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_employees_updated_at
    BEFORE UPDATE ON employees
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Similar triggers for other tables
```

## Data Types

1. **Timestamps**
   - All timestamp fields use TIMESTAMPTZ (timestamp with time zone)
   - Ensures consistent time handling across time zones

2. **Text Fields**
   - VARCHAR for fixed-length strings
   - TEXT for variable-length strings
   - Appropriate length limits for each field

3. **Boolean**
   - BOOLEAN for true/false values
   - Used for is_active in app_users

4. **Integer**
   - SERIAL for auto-incrementing IDs
   - INT for foreign keys and other numeric values

## Best Practices

1. **Naming Conventions**
   - Table names are lowercase and plural
   - Column names are lowercase with underscores
   - Foreign key columns end with _id
   - Timestamp columns end with _at

2. **Indexing Strategy**
   - Primary keys are automatically indexed
   - Foreign keys are indexed for join performance
   - Additional indexes on frequently queried columns
   - No redundant indexes

3. **Data Integrity**
   - All required fields are marked NOT NULL
   - Appropriate default values where needed
   - Check constraints for enumerated values
   - Foreign key constraints for relationships

4. **Security**
   - Passwords are stored as BCrypt hashes
   - No sensitive data in plain text
   - Appropriate access controls at database level 