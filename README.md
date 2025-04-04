# Employee Hardware Directory (EHD) - MVP Documentation

## Overview

The Employee Hardware Directory (EHD) is a web-based application designed to help IT Administrators track and manage hardware assets within an organization. This MVP (Minimum Viable Product) focuses on core functionality for hardware asset tracking, employee assignments, and basic inventory management.

## Table of Contents

1. [System Architecture](#system-architecture)
2. [Technology Stack](#technology-stack)
3. [Backend Documentation](#backend-documentation)
4. [Frontend Documentation](#frontend-documentation)
5. [Database Schema](#database-schema)
6. [API Documentation](#api-documentation)
7. [Security Implementation](#security-implementation)
8. [Development Setup](#development-setup)
9. [Deployment Guide](#deployment-guide)

## System Architecture

The EHD system follows a modern three-tier architecture:

```
+---------------------+        +-------------------------+        +----------------+
|   User's Browser    |        |   App Server (Backend)  |        |   Database     |
|---------------------|        |-------------------------|        |----------------|
|    Next.js App      |<------>|   Java Spring Boot      |<------>|  PostgreSQL DB |
| (React Components)  | HTTP/S |   - REST Controllers    |  JDBC  |                |
| (Client-Side Render)|  REST  |   - Services (Logic)    | (JPA)  |   Tables:      |
|                     |  API   |   - Repositories (Data) |        |   - Assets     |
|                     | (JSON) |   - Spring Security/JWT |        |   - Employees  |
|                     |        |                         |        |   - Users      |
|                     |        |                         |        |   - AssignHist |
|                     |        |                         |        |   - HW Types   |
+---------------------+        +-------------------------+        +----------------+
```

### Key Components

1. **Frontend (Next.js)**
   - React-based user interface
   - Client-side routing and state management
   - API integration with backend services
   - Responsive design using Tailwind CSS

2. **Backend (Spring Boot)**
   - RESTful API endpoints
   - Business logic and data processing
   - Database interaction through JPA
   - Security and authentication

3. **Database (PostgreSQL)**
   - Relational data storage
   - Optimized for asset tracking and assignment history
   - ACID compliance for data integrity

## Technology Stack

### Backend
- Java 17+
- Spring Boot 3.x
  - Spring Web
  - Spring Data JPA
  - Spring Security
  - Spring Validation
- PostgreSQL 14+
- JWT for authentication
- Maven/Gradle for build management

### Frontend
- Next.js 13+
- React 18+
- TypeScript
- Tailwind CSS
- TanStack Query (React Query)
- Axios for API calls

## Backend Documentation

### Core Entities

1. **HardwareAsset**
   - Represents physical hardware items
   - Key fields: assetTag, serialNumber, type, make, model, status
   - Relationships: HardwareType, Employee

2. **Employee**
   - Represents company employees
   - Key fields: employeeId, fullName, email, status
   - Used for hardware assignments

3. **HardwareType**
   - Lookup table for hardware categories
   - Key fields: typeId, typeName

4. **AppUser**
   - System users (IT Administrators)
   - Key fields: username, email, passwordHash, role

5. **AssignmentHistory**
   - Tracks hardware assignments over time
   - Key fields: assignmentDate, returnDate, notes
   - Relationships: HardwareAsset, Employee, AppUser

### Service Layer

The backend implements a service-oriented architecture with the following key services:

1. **HardwareAssetService**
   - Manages hardware asset lifecycle
   - Handles CRUD operations
   - Implements assignment and return logic

2. **EmployeeService**
   - Manages employee records
   - Handles CRUD operations
   - Supports employee lookup for assignments

3. **AuthService**
   - Handles user authentication
   - Manages JWT token generation and validation
   - Implements password hashing

### Security Implementation

- JWT-based authentication
- Role-based access control (RBAC)
- Password hashing using BCrypt
- CSRF protection
- CORS configuration
- Secure session management

## Frontend Documentation

### Key Components

1. **Layout Components**
   - AppLayout: Main application structure
   - ProtectedRoute: Authentication wrapper
   - Navigation: Sidebar and header

2. **Asset Management**
   - AssetTable: List view of hardware assets
   - AssetForm: Create/Edit asset details
   - AssignAssetModal: Asset assignment interface
   - ReturnAssetModal: Asset return interface

3. **Employee Management**
   - EmployeeTable: List view of employees
   - EmployeeForm: Create/Edit employee details
   - EmployeeAssets: View assigned assets

### State Management

- React Context for authentication state
- TanStack Query for server state
- Local state for form management
- Axios interceptors for API calls

## Database Schema

The database schema consists of five main tables:

1. **hardware_assets**
   - Primary table for hardware inventory
   - Tracks current status and assignments

2. **employees**
   - Stores employee information
   - Used for hardware assignments

3. **hardware_types**
   - Lookup table for hardware categories
   - Maintains consistent hardware classification

4. **app_users**
   - System user accounts
   - Stores authentication information

5. **assignment_history**
   - Tracks all hardware assignments
   - Maintains historical record of asset usage

## API Documentation

The API follows RESTful principles with the following endpoints:

### Authentication
- POST /api/v1/auth/login
- GET /api/v1/auth/me

### Hardware Assets
- GET /api/v1/assets
- POST /api/v1/assets
- GET /api/v1/assets/{id}
- PUT /api/v1/assets/{id}
- POST /api/v1/assets/{id}/assign
- POST /api/v1/assets/{id}/return

### Employees
- GET /api/v1/employees
- POST /api/v1/employees
- GET /api/v1/employees/{id}
- PUT /api/v1/employees/{id}
- GET /api/v1/employees/{id}/assets

### Hardware Types
- GET /api/v1/hardware-types

## Development Setup

### Prerequisites
- Java 17+
- Node.js 16+
- PostgreSQL 14+
- Maven/Gradle
- npm/yarn

### Backend Setup
1. Clone the repository
2. Configure database connection in application.properties
3. Run `mvn spring-boot:run` or `gradle bootRun`

### Frontend Setup
1. Navigate to frontend directory
2. Install dependencies: `npm install`
3. Start development server: `npm run dev`

## Deployment Guide

### Backend Deployment
1. Build the application: `mvn clean package`
2. Deploy the JAR file to your server
3. Configure environment variables
4. Start the application

### Frontend Deployment
1. Build the application: `npm run build`
2. Deploy the build output to your web server
3. Configure environment variables
4. Start the server

## Contributing

Please read CONTRIBUTING.md for details on our code of conduct and the process for submitting pull requests.

## License

This project is licensed under the MIT License - see the LICENSE.md file for details. 