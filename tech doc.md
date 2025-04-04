tech doc 



Okay, let's refine the PRD and TDD into a focused MVP (Minimum Viable Product) specification tailored for a Java Spring Boot backend and a Next.js frontend.

The goal of the MVP is to deliver the absolute core value proposition – tracking hardware assignments – quickly and efficiently, establishing a foundation to build upon.

MVP Specification: Employee Hardware Directory (EHD)

1. Introduction

This document defines the Minimum Viable Product (MVP) for the Employee Hardware Directory (EHD). The MVP focuses on enabling IT Administrators to digitally track company hardware assets, assign them to employees, and view the current status and assignment history. This replaces basic manual methods (like spreadsheets) for core asset tracking.

Backend: Java Spring Boot

Frontend: Next.js (React Framework)

Database: PostgreSQL

2. MVP Goals

Core Asset Tracking: Provide a central place for IT Admins to create and manage hardware asset records.

Basic Assignment: Enable IT Admins to assign hardware to employees and mark it as returned.

Essential Visibility: Allow IT Admins to easily see which assets exist, their status, and who they are assigned to.

Foundation: Build a stable, secure base application using the chosen tech stack, ready for future enhancements.

3. Target Audience (MVP)

IT Administrators: Primary users responsible for managing the hardware inventory and assignments within the EHD system.

(Other roles like HR, Managers, Finance are out of scope for MVP functionality but the architecture should allow adding roles later).

4. Core Use Cases (MVP)

UC-MVP-01: As an IT Admin, I want to add new hardware items (e.g., Laptop, Monitor) to the inventory with essential details (Asset Tag, Serial, Type, Make, Model, Status), so they can be tracked.

UC-MVP-02: As an IT Admin, I want to manually add or update basic employee information (ID, Name, Email, Active Status), so hardware can be assigned to them.

UC-MVP-03: As an IT Admin, I want to assign an "Available" hardware item to a specific employee, recording the assignment date.

UC-MVP-04: As an IT Admin, I want to view a list of all hardware assets, with basic filtering (by Status, Type) and searching (by Asset Tag, Serial Number, Model).

UC-MVP-05: As an IT Admin, I want to view all hardware currently assigned to a specific employee.

UC-MVP-06: As an IT Admin, I want to mark hardware as returned from an employee, recording the return date, and update its status (e.g., back to "Available" or "Damaged").

UC-MVP-07: As an IT Admin, I want to update the status of a hardware item (e.g., "Available," "Assigned," "In Repair," "Retired").

UC-MVP-08: As an IT Admin, I want to view a simple history of assignments (Assign/Return actions) for a specific asset.

5. Functional Requirements (MVP)

FR-MVP-01: Hardware Asset CRUD:

Ability to Create, Read, Update hardware asset records.

Logical "Retire" status instead of hard delete.

FR-MVP-02: Essential Asset Fields:

Required: Asset Tag (Unique), Serial Number (Unique), Hardware Type (Select list: Laptop, Monitor, Keyboard, Mouse, Docking Station, Other - Managed via simple lookup), Make, Model, Status (Select list: Available, Assigned, In Repair, Damaged, Retired - Default: Available).

Optional for MVP (but include field): Specifications, Notes.

(Deferred Fields: Purchase Date, Warranty Expiry, Supplier, Cost, Location - can be added later)

FR-MVP-03: Employee Data (Manual Entry):

Ability to Create, Read, Update basic employee records.

Required: Employee ID (Unique), Full Name, Email (Unique), Status (Select list: Active, Inactive).

(Deferred Fields: Department)

(Deferred Functionality: HRIS/AD Integration)

FR-MVP-04: Assignment Management:

Associate one asset with one employee at a time.

Record assignmentDate on assignment. Update asset status to "Assigned". Link asset to employeeId.

Record returnDate on return. Clear employeeId link. Allow Admin to set new asset status.

Prevent assigning assets not in "Available" status.

Maintain a basic assignment history log (Asset ID, Employee ID, Assignment Date, Return Date).

FR-MVP-05: Search & Filter:

Search assets by Asset Tag, Serial Number, Model.

Filter asset list by Status, Hardware Type.

Search/lookup employees by Name or Employee ID.

FR-MVP-06: User Roles & Authentication:

Secure login mechanism (Username/Password).

Single administrative role (ROLE_ADMIN) with full access to all MVP features.

(Deferred: More granular roles like IT_Staff, ReadOnly).

FR-MVP-07: Basic Audit Trail:

The AssignmentHistory table tracks assign/return actions.

Optionally, simple logging of asset creation/update/status change events linked to the performing user and timestamp (can be a simplified AuditLog table or JPA Auditing).

6. Non-Functional Requirements (MVP)

NFR-MVP-01 (Performance): Pages and search results should load reasonably fast (< 5 seconds) for up to ~1,000 assets and a few concurrent users.

NFR-MVP-02 (Usability): The UI must be simple and intuitive for IT Admins to perform core tasks (Add/Assign/Return/Search).

NFR-MVP-03 (Security):

Implement authentication (JWT) and basic role-based authorization (Admin only).

Use HTTPS.

Protect against basic OWASP Top 10 vulnerabilities (e.g., SQL Injection via JPA, basic XSS prevention in Next.js).

Securely hash passwords (e.g., BCrypt).

NFR-MVP-04 (Reliability): Application should be stable. Basic database backup strategy required.

NFR-MVP-05 (Maintainability): Follow standard Java/Spring Boot and React/Next.js best practices. Code should be reasonably documented.

7. Technology Stack (MVP)

Backend: Java 17+, Spring Boot 3.x (using Web, Data JPA, Security, Validation starters)

Frontend: Next.js 13+ (using React 18+, TypeScript)

Database: PostgreSQL 14+

ORM: Spring Data JPA (with Hibernate)

Authentication: Spring Security + JSON Web Tokens (JWT)

API: RESTful JSON APIs

Styling: Tailwind CSS (or component library like Material UI / Mantine if preferred)

Frontend State/Cache: React Context / Zustand (for global state like auth), TanStack Query (React Query) (for server state management)

Build Tools: Maven or Gradle (Backend), Next.js CLI (Frontend)

Containerization: Docker, Docker Compose (for local development)

8. Architecture Overview (MVP)

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


9. Data Model (MVP - Conceptual SQL)

-- Employees (Manually Managed for MVP)
CREATE TABLE employees (
    employee_id VARCHAR(50) PRIMARY KEY, -- Company ID
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('Active', 'Inactive')), -- Active/Inactive
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- Hardware Types (Lookup)
CREATE TABLE hardware_types (
    type_id SERIAL PRIMARY KEY,
    type_name VARCHAR(255) NOT NULL UNIQUE
);

-- Hardware Assets
CREATE TABLE hardware_assets (
    asset_id SERIAL PRIMARY KEY,
    asset_tag VARCHAR(100) UNIQUE NOT NULL,
    serial_number VARCHAR(255) UNIQUE NOT NULL,
    type_id INT NOT NULL REFERENCES hardware_types(type_id),
    make VARCHAR(100) NOT NULL,
    model VARCHAR(150) NOT NULL,
    specifications TEXT, -- Optional field
    status VARCHAR(50) NOT NULL CHECK (status IN ('Available', 'Assigned', 'In Repair', 'Damaged', 'Retired')),
    notes TEXT, -- Optional field
    current_employee_id VARCHAR(50) REFERENCES employees(employee_id) NULL, -- Nullable FK
    last_assignment_date TIMESTAMPTZ, -- Track latest assignment
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
    -- Deferred: purchase_date, warranty_expiration_date, supplier, purchase_cost, location
);

-- System Users (For EHD Login)
CREATE TABLE app_users ( -- Renamed from 'users' to avoid SQL keyword conflict
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL, -- Or use email
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL, -- BCrypt hash
    role VARCHAR(50) NOT NULL CHECK (role IN ('ROLE_ADMIN')), -- Simplified for MVP
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- Assignment History
CREATE TABLE assignment_history (
    history_id SERIAL PRIMARY KEY,
    asset_id INT NOT NULL REFERENCES hardware_assets(asset_id),
    employee_id VARCHAR(50) NOT NULL REFERENCES employees(employee_id),
    assigned_by_user_id INT REFERENCES app_users(user_id),
    assignment_date TIMESTAMPTZ NOT NULL,
    return_date TIMESTAMPTZ, -- Null if currently assigned
    returned_by_user_id INT REFERENCES app_users(user_id),
    notes TEXT -- Optional notes for this specific assignment/return
);

-- Indexes (Essential)
CREATE INDEX idx_asset_status ON hardware_assets(status);
CREATE INDEX idx_asset_type ON hardware_assets(type_id);
CREATE INDEX idx_asset_employee ON hardware_assets(current_employee_id);
CREATE INDEX idx_employee_email ON employees(email);
CREATE INDEX idx_user_email ON app_users(email);
CREATE INDEX idx_assignment_asset ON assignment_history(asset_id);
CREATE INDEX idx_assignment_employee ON assignment_history(employee_id);
IGNORE_WHEN_COPYING_START
content_copy
download
Use code with caution.
SQL
IGNORE_WHEN_COPYING_END

Note: Use Spring Data JPA Entities and a migration tool like Flyway or Liquibase to manage the schema.

10. API Endpoints (MVP - Key Examples)

Base URL: /api/v1
Authentication: JWT Bearer Token required (via Spring Security Filter Chain)
Authorization: @PreAuthorize("hasRole('ADMIN')") or similar on relevant methods/controllers.

POST /auth/login -> Body: {username, password}, Returns: { accessToken }

GET /auth/me -> Returns: Logged-in user info ({ userId, username, email, role })

GET /assets -> List assets (supports query params: status, typeId, search for tag/serial/model)

POST /assets -> Create asset (Admin only)

GET /assets/{assetId} -> Get single asset details

PUT /assets/{assetId} -> Update asset details (Admin only)

PATCH /assets/{assetId}/status -> Update only asset status (Admin only), Body: { status }

POST /assets/{assetId}/assign -> Assign asset (Admin only), Body: { employeeId }

POST /assets/{assetId}/return -> Return asset (Admin only), Body: { returnStatus } (e.g., Available, Damaged)

GET /employees -> List employees (supports query params: search for name/email)

POST /employees -> Create employee (Admin only)

GET /employees/{employeeId} -> Get single employee details

PUT /employees/{employeeId} -> Update employee details (Admin only)

GET /employees/{employeeId}/assets -> List assets currently assigned to an employee

GET /assets/{assetId}/history -> Get assignment history for an asset

GET /hardware-types -> List available hardware types

11. Frontend Structure (MVP - Next.js)

Pages:

/login: Login page.

/dashboard (or /): Simple landing page after login (maybe counts of assets by status).

/assets: List view for hardware assets (table, search, filter).

/assets/new: Form to add a new asset.

/assets/[id]: View/Edit page for a single asset (details, assignment history).

/employees: List view for employees (table, search).

/employees/new: Form to add a new employee.

/employees/[id]: View/Edit page for a single employee (details, list of assigned assets).

Components:

Layout: Main application layout (sidebar/navbar, content area).

ProtectedRoute: Wrapper to ensure user is logged in.

AssetTable, EmployeeTable: Reusable table components.

AssetForm, EmployeeForm: Forms for creating/editing.

AssignAssetModal, ReturnAssetModal: Modals for assignment actions.

Shared UI components (Button, Input, Select, Modal, Spinner, Alert).

Services/Hooks:

useAuth: Hook for authentication state and actions (login, logout).

API service functions (fetchAssets, assignAsset, etc.) possibly using TanStack Query for caching/fetching.

12. Out of Scope for MVP

Advanced Reporting & Dashboards

Data Export (CSV/Excel)

HRIS / Active Directory Integration

Barcode / QR Code Scanning

Warranty / Purchase Tracking & Alerts

Low Stock Alerts

Granular Roles (IT_Staff, ReadOnly, Manager)

Employee Self-Service Portal

Integration with IT Ticketing Systems

Software License Tracking

Bulk Import/Update Functionality

13. Success Metrics (MVP)

IT Admins can successfully add, assign, return, and search for assets.

Core inventory data accuracy increases compared to previous methods.

Successful deployment and basic stability.

Positive qualitative feedback from initial IT Admin users.

This MVP specification provides a clear, achievable scope for the initial version of the EHD using Java Spring Boot and Next.js. It focuses on delivering the core functionality quickly while setting up a solid foundation for future iterations based on the original PRD's broader vision.



Okay, here is a detailed prompt chain designed for an AI IDE like Cursor, aiming to minimize hallucinations by breaking down the Employee Hardware Directory MVP (Spring Boot + Next.js) into smaller, verifiable steps.

Core Principles for Using This Chain:

One Prompt at a Time: Feed the AI one numbered prompt at a time.

Review Output: Critically review the generated code after each step. Check for correctness, adherence to the MVP spec, and potential hallucinations (e.g., adding features not asked for, incorrect imports, wrong logic).

Use Context: Cursor is good at using file context. Explicitly mention file paths (src/main/..., pages/...) where code should be generated or modified. Open the relevant files if possible.

Correct and Refine: If the AI hallucinates or makes a mistake, correct it explicitly. You might say: "That's incorrect. The HardwareAsset entity should map to the hardware_assets table, not assets. Please update the @Table annotation in HardwareAsset.java." or "You added a purchaseDate field, but that's deferred for the MVP. Please remove it from the HardwareAsset entity and DTO."

Small Steps: The prompts are deliberately granular. Resist the urge to combine too many steps into one prompt.

Prompt Chain: EHD MVP (Spring Boot + Next.js)

Phase 1: Backend Setup & Core Data Model (Spring Boot)

Prompt: "Initialize a new Spring Boot 3.x project using Maven (or Gradle). Include the following starters: Spring Web, Spring Data JPA, Spring Security, PostgreSQL Driver, Validation, and Lombok. Set the group ID to com.ehd.mvp and the artifact ID to ehd-backend. Set the default Java version to 17."

Review: Check pom.xml or build.gradle for correct dependencies. Check basic project structure.

Prompt: "Configure the application.properties (or application.yml) file located in src/main/resources/ to connect to a PostgreSQL database. Use placeholders for database URL, username, and password (e.g., ${DB_URL}, ${DB_USERNAME}, ${DB_PASSWORD}). Also, configure JPA/Hibernate to update the schema (spring.jpa.hibernate.ddl-auto=update) for development and show SQL (spring.jpa.show-sql=true)."

Review: Check the properties file for correct keys and placeholder usage.

Prompt: "Create the JPA Entity class HardwareType in src/main/java/com/ehd/mvp/entity/HardwareType.java. It should have fields: typeId (Integer, Primary Key, auto-generated) and typeName (String, Unique, Not Null). Map it to the hardware_types table. Use Lombok annotations (@Entity, @Data, @NoArgsConstructor, @AllArgsConstructor, @Table)."

Review: Check annotations, field types, constraints, table mapping.

Prompt: "Create the JPA Entity class Employee in src/main/java/com/ehd/mvp/entity/Employee.java. Fields: employeeId (String, Primary Key), fullName (String, Not Null), email (String, Unique, Not Null), status (String, Not Null - e.g., 'Active', 'Inactive'). Map it to the employees table. Add @CreationTimestamp and @UpdateTimestamp fields (createdAt, updatedAt). Use Lombok annotations."

Review: Check annotations, field types, constraints, table mapping, timestamps.

Prompt: "Create the JPA Entity class HardwareAsset in src/main/java/com/ehd/mvp/entity/HardwareAsset.java. Fields: assetId (Long, PK, auto-generated), assetTag (String, Unique, Not Null), serialNumber (String, Unique, Not Null), make (String, Not Null), model (String, Not Null), specifications (String, nullable), status (String, Not Null - e.g., 'Available', 'Assigned'), notes (String, nullable), lastAssignmentDate (Timestamp, nullable). Add @CreationTimestamp and @UpdateTimestamp fields. Map it to the hardware_assets table. Include relationships: ManyToOne with HardwareType (named hardwareType, nullable=false, fetch=EAGER) and ManyToOne with Employee (named currentEmployee, nullable=true, fetch=LAZY, FK column current_employee_id). Use Lombok."

Review: Check annotations, fields, relationships (esp. join column names, nullability, fetch types), table mapping.

Prompt: "Create the JPA Entity class AppUser in src/main/java/com/ehd/mvp/entity/AppUser.java. Fields: userId (Long, PK, auto-generated), username (String, Unique, Not Null), email (String, Unique, Not Null), passwordHash (String, Not Null), role (String, Not Null - e.g., 'ROLE_ADMIN'), isActive (boolean, default true). Map it to app_users. Add timestamps. Use Lombok."

Review: Check annotations, fields, table mapping (app_users).

Prompt: "Create the JPA Entity class AssignmentHistory in src/main/java/com/ehd/mvp/entity/AssignmentHistory.java. Fields: historyId (Long, PK, auto-generated), assignmentDate (Timestamp, Not Null), returnDate (Timestamp, nullable), notes (String, nullable). Include relationships: ManyToOne with HardwareAsset (named asset, nullable=false), ManyToOne with Employee (named employee, nullable=false), ManyToOne with AppUser (named assignedByUser, nullable=true, FK assigned_by_user_id), ManyToOne with AppUser (named returnedByUser, nullable=true, FK returned_by_user_id). Map to assignment_history. Use Lombok."

Review: Check annotations, fields, relationships, foreign key column names if needed explicitly.

Prompt: "Create Spring Data JPA Repository interfaces for each entity (HardwareType, Employee, HardwareAsset, AppUser, AssignmentHistory) in the src/main/java/com/ehd/mvp/repository/ package. Example: HardwareAssetRepository extends JpaRepository<HardwareAsset, Long>. Add custom query methods needed for MVP search/filter to HardwareAssetRepository: findByStatus(String status), findByHardwareTypeTypeId(Integer typeId), findByAssetTagContainingIgnoreCaseOrSerialNumberContainingIgnoreCaseOrModelContainingIgnoreCase(String tag, String serial, String model). Add findByUsername(String username) to AppUser repository. Add findByAssetAssetIdOrderByAssignmentDateDesc(Long assetId) to AssignmentHistoryRepository."

Review: Check interfaces extend JpaRepository, generic types are correct, package location, custom method signatures match requirements.

Phase 2: Backend API Layer (Basic CRUD - No Auth Yet)

Prompt: "Create Data Transfer Object (DTO) classes for request/response bodies in src/main/java/com/ehd/mvp/dto/. Create basic DTOs for HardwareAsset (e.g., HardwareAssetDto, CreateAssetRequest), Employee (e.g., EmployeeDto, CreateEmployeeRequest), and HardwareType (HardwareTypeDto). Use Lombok (@Data, etc.) and include validation annotations (@NotNull, @Size, @Email) where appropriate, especially on Request DTOs."

Review: Check DTO fields match MVP entity fields (excluding sensitive/internal data like password hashes). Check validation annotations.

Prompt: "Create a service class HardwareAssetService in src/main/java/com/ehd/mvp/service/HardwareAssetService.java. Inject the HardwareAssetRepository, EmployeeRepository, and HardwareTypeRepository. Implement basic CRUD methods: findAllAssets(String status, Integer typeId, String search), findAssetById(Long id), createAsset(CreateAssetRequest dto), updateAsset(Long id, CreateAssetRequest dto). Use the repository methods. Map between Entities and DTOs (manually or using a library like MapStruct later). Handle EntityNotFoundException."

Review: Check method signatures, repository usage, basic logic for finding/creating/updating, exception handling. Don't worry about assign/return logic yet.

Prompt: "Create a REST Controller HardwareAssetController in src/main/java/com/ehd/mvp/controller/HardwareAssetController.java with base path /api/v1/assets. Inject HardwareAssetService. Implement endpoints for the service methods created in the previous step: GET / (with optional query params status, typeId, search), GET /{id}, POST /, PUT /{id}. Use the DTOs for request/response bodies and proper HTTP status codes (200, 201, 404)."

Review: Check controller mapping, method mappings (GET/POST/PUT), request/response types, service delegation, status codes.

Prompt: "Repeat steps 10 and 11 for Employee: Create EmployeeService and EmployeeController (/api/v1/employees) with basic CRUD operations (findAll, findById, create, update) using DTOs."

Review: Similar checks as for HardwareAsset.

Prompt: "Create a simple HardwareTypeController (/api/v1/hardware-types) with a GET / endpoint that returns a list of all HardwareTypeDto."

Review: Check controller, service/repository usage, response type.

Phase 3: Backend Assignment Logic & Security

Prompt: "In HardwareAssetService, add methods: assignAsset(Long assetId, String employeeId, Long assigningUserId) and returnAsset(Long assetId, String newStatus, Long returningUserId).

assignAsset: Find the asset (must be 'Available'). Find the employee. Update asset's status to 'Assigned', set currentEmployee, set lastAssignmentDate. Create and save an AssignmentHistory record.

returnAsset: Find the asset (must be 'Assigned'). Update asset's status to newStatus, clear currentEmployee, clear lastAssignmentDate. Find the latest AssignmentHistory record for this asset and update its returnDate. Save changes. Add necessary repository injections (AssignmentHistoryRepository, AppUserRepository). Handle potential errors (asset not found, not available, employee not found, etc.)."

Review: Check the business logic carefully. Ensure transactions are handled correctly (implicitly by Spring or explicitly if needed). Verify history logging.

Prompt: "In HardwareAssetController, add POST endpoints: /api/v1/assets/{assetId}/assign (request body { "employeeId": "..." }) and /api/v1/assets/{assetId}/return (request body { "returnStatus": "..." }). These should call the corresponding service methods. They will need the authenticated user's ID later."

Review: Check endpoint mappings, request body handling, service calls.

Prompt: "In EmployeeController, add a GET endpoint /api/v1/employees/{employeeId}/assets that returns a list of HardwareAssetDto currently assigned to that employee."

Review: Check endpoint, service call (might need a new method in HardwareAssetService or EmployeeService).

Prompt: "In HardwareAssetController, add a GET endpoint /api/v1/assets/{assetId}/history that returns a list of AssignmentHistoryDto (create this DTO) for the given asset."

Review: Check endpoint, DTO, service call.

Prompt: "Implement JWT-based authentication using Spring Security.

Create a JwtUtil class (or similar) in src/main/java/com/ehd/mvp/security/ to generate and validate JWTs. Use the jjwt library. Store the secret key securely (use an environment variable/property).

Create a UserDetailsService implementation (UserDetailsServiceImpl in .../security/service/) that loads AppUser by username from AppUserRepository and converts it to Spring Security's UserDetails.

Create a JwtRequestFilter extending OncePerRequestFilter in .../security/filter/ to intercept requests, extract the JWT from the Authorization: Bearer header, validate it using JwtUtil, load UserDetails using UserDetailsService, and set the authentication in SecurityContextHolder.

Create a SecurityConfig class (.../config/SecurityConfig.java) extending WebSecurityConfigurerAdapter (or using the newer Component-based configuration). Configure:

Password encoder bean (BCryptPasswordEncoder).

Authentication manager bean.

HTTP security rules: Permit /api/v1/auth/login, require authentication for all other /api/v1/** routes. Ensure CSRF is disabled for the stateless API. Configure CORS.

Add the JwtRequestFilter before the standard UsernamePasswordAuthenticationFilter.

Configure session management to STATELESS.

Create an AuthController (.../controller/AuthController.java) with a POST /api/v1/auth/login endpoint. It should take {username, password}, authenticate using AuthenticationManager, and if successful, generate a JWT using JwtUtil and return it ({ "accessToken": "..." })."

Review: This is complex. Review each part: Util, Service, Filter, Config, Controller. Check beans, filter order, security rules, CORS config (@CrossOrigin or CorsConfigurationSource bean).

Prompt: "Add @PreAuthorize("hasRole('ADMIN')") annotations to all Controller methods that modify data (POST, PUT, PATCH, DELETE, assign, return) in HardwareAssetController, EmployeeController, etc., except for the login endpoint. Enable method-level security in SecurityConfig using @EnableMethodSecurity."

Review: Check annotations are present on the correct methods. Ensure method security is enabled.

(Ensure Backend is runnable and testable with tools like Postman/Insomnia at this stage)

Phase 4: Frontend Setup & Basic Views (Next.js)

Prompt: "Initialize a new Next.js project (v13+) with TypeScript and Tailwind CSS in a separate directory (e.g., ehd-frontend). Use the App Router. npx create-next-app@latest --typescript --tailwind --eslint --app ehd-frontend"

Review: Check project structure, package.json, tailwind.config.js.

Prompt: "Install necessary frontend libraries: axios (for API calls) and @tanstack/react-query (for server state management). npm install axios @tanstack/react-query (or yarn/pnpm/bun)."

Review: Check package.json.

Prompt: "Create basic type definitions in types/index.ts matching the backend DTOs for HardwareAsset, Employee, HardwareType, AssignmentHistory, and User (for logged-in user info)."

Review: Check type definitions match the expected API responses.

Prompt: "Set up an Axios instance for API calls in lib/axios.ts. Configure the baseURL to point to the backend (e.g., http://localhost:8080/api/v1). Add an interceptor to automatically add the JWT Authorization: Bearer header if a token exists (we'll add token storage later)."

Review: Check Axios instance creation, baseURL, and the interceptor structure (it won't fully work without token logic yet).

Prompt: "Set up React Query (@tanstack/react-query). Create a client provider component components/providers/QueryProvider.tsx that wraps its children with <QueryClientProvider>. Wrap the root layout (app/layout.tsx) with this provider."

Review: Check provider setup and usage in the root layout.

Prompt: "Create a basic application layout component components/layout/AppLayout.tsx. Include a simple sidebar/navbar for navigation (links for 'Dashboard', 'Assets', 'Employees') and a main content area. Use Tailwind CSS for styling."

Review: Check component structure and basic styling.

Prompt: "Create the main page for listing hardware assets at app/assets/page.tsx.

Use the AppLayout.

Use React Query's useQuery hook to fetch data from the backend /assets endpoint using the Axios instance. Define an API function fetchAssets() in lib/api/assets.ts.

Display the assets in a table (components/assets/AssetTable.tsx) showing key MVP fields (Asset Tag, Type, Make, Model, Serial, Status, Assigned To).

Add basic loading and error states based on useQuery status."

Review: Check page structure, layout usage, useQuery setup, API call function, table component props, loading/error handling.

Prompt: "Create the main page for listing employees at app/employees/page.tsx.

Use AppLayout.

Use useQuery to fetch data from /employees (create fetchEmployees in lib/api/employees.ts).

Display employees in a table (components/employees/EmployeeTable.tsx) showing Employee ID, Name, Email, Status.

Add loading/error states."

Review: Similar checks as for the assets page.

Phase 5: Frontend Forms, Mutations & Authentication

Prompt: "Implement the frontend Login page at app/login/page.tsx.

Create a simple form with 'Username' and 'Password' fields and a 'Login' button.

On submit, call an API function loginUser(username, password) (in lib/api/auth.ts) which POSTs to /auth/login.

On successful login (receiving a token), store the JWT token securely (e.g., in localStorage or sessionStorage for MVP simplicity - acknowledge security trade-offs). Redirect the user to the dashboard (e.g., /assets).

Handle login errors (display message)."

Review: Check form structure, state management for inputs, API call on submit, token storage, redirection, error handling.

Prompt: "Create an authentication context/provider (context/AuthContext.tsx or use Zustand store) to manage user authentication state (isLoggedIn, user info, token). Load the token from storage on initial app load. Update the Axios interceptor in lib/axios.ts to read the token from this context/storage."

Review: Check context/store setup, state variables, loading token logic, integration with Axios interceptor.

Prompt: "Create a ProtectedRoute component (components/auth/ProtectedRoute.tsx) that wraps pages requiring authentication. It should check the auth state from the context/store. If not logged in, redirect the user to /login. Wrap the AppLayout or individual page layouts (app/assets/layout.tsx, app/employees/layout.tsx) with ProtectedRoute."

Review: Check authentication check logic and redirection. Ensure protected pages use it.

Prompt: "Create the 'Add New Asset' page at app/assets/new/page.tsx.

Include a form (components/assets/AssetForm.tsx) with fields for MVP asset details (Tag, Serial, Type (dropdown fetched from /hardware-types), Make, Model, etc.).

Use React Query's useMutation hook to handle the form submission. Call an API function createAsset(assetData) (in lib/api/assets.ts) which POSTs to /assets.

On success, invalidate the assets query cache (queryClient.invalidateQueries(['assets'])) and redirect to /assets. Handle loading/error states for the mutation."

Review: Check form component, state, validation (basic), useMutation setup, API call, cache invalidation, redirection.

Prompt: "Create the 'Asset Detail/Edit' page at app/assets/[id]/page.tsx.

Fetch the specific asset data using useQuery and the asset ID from the route params.

Display asset details.

Include the AssetForm component, pre-filled with the fetched asset data, allowing edits. Use useMutation to call an update API function updateAsset(id, assetData) which PUTs to /assets/{id}. Invalidate cache on success.

Fetch and display the assignment history for this asset from /assets/{id}/history."

Review: Check data fetching for single asset, form pre-population, update mutation logic, history display.

Prompt: "Implement 'Assign Asset' functionality. On the 'Asset Detail' page (app/assets/[id]/page.tsx), if the asset status is 'Available', show an 'Assign' button. Clicking it opens a modal (components/assets/AssignAssetModal.tsx). The modal should have a dropdown/searchable input to select an employee (fetch employees from /employees). On submit, use useMutation to call an API function assignAsset(assetId, employeeId) which POSTs to /assets/{assetId}/assign. Invalidate relevant queries on success and close the modal."

Review: Check button visibility logic, modal component, employee selection, assign mutation setup, API call.

Prompt: "Implement 'Return Asset' functionality. On the 'Asset Detail' page, if the asset status is 'Assigned', show a 'Return' button. Clicking it opens a modal (components/assets/ReturnAssetModal.tsx). The modal asks the admin to select the status upon return (e.g., 'Available', 'Damaged'). On submit, use useMutation to call an API function returnAsset(assetId, returnStatus) which POSTs to /assets/{assetId}/return. Invalidate relevant queries on success and close the modal."

Review: Check button visibility, modal component, status selection, return mutation setup, API call.

Prompt: "Repeat steps 31 & 32 for Employees (app/employees/new/page.tsx, app/employees/[id]/page.tsx using EmployeeForm, createEmployee, updateEmployee API calls). On the Employee Detail page, display the list of assets currently assigned to them (fetch from /employees/{employeeId}/assets)."

Review: Check employee forms, mutations, detail page including the assigned assets list.

Phase 6: Refinement & Testing

Prompt: "Review the frontend code for basic error handling. Ensure API call errors caught by React Query or Axios are displayed to the user (e.g., using toast notifications or inline messages)."

Prompt: "Review the backend code for basic exception handling. Ensure controller advice (@RestControllerAdvice) is used to map custom exceptions (like EntityNotFoundException) and validation errors (MethodArgumentNotValidException) to appropriate HTTP status codes and error response DTOs."

Prompt: (Optional) "Add basic unit tests for a key service method in the backend, like HardwareAssetService.assignAsset, using JUnit 5 and Mockito. Mock the repository dependencies."

Prompt: (Optional) "Add a basic component test for the AssetForm component in the frontend using React Testing Library. Check that form fields render and basic input changes work."

Remember to test the application end-to-end frequently throughout this process. Good luck!