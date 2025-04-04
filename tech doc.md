tech doc 
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