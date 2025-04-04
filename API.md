# EHD API Documentation

## Base URL

All API endpoints are prefixed with `/api/v1`

## Authentication

All endpoints except `/auth/login` require a JWT token in the Authorization header:
```
Authorization: Bearer <token>
```

## Endpoints

### Authentication

#### Login
```http
POST /auth/login
```

Request body:
```json
{
  "username": "string",
  "password": "string"
}
```

Response:
```json
{
  "accessToken": "string",
  "tokenType": "Bearer",
  "expiresIn": 3600
}
```

#### Get Current User
```http
GET /auth/me
```

Response:
```json
{
  "userId": "number",
  "username": "string",
  "email": "string",
  "role": "string"
}
```

### Hardware Assets

#### List Assets
```http
GET /assets
```

Query Parameters:
- `status` (optional): Filter by status (Available, Assigned, In Repair, Damaged, Retired)
- `typeId` (optional): Filter by hardware type ID
- `search` (optional): Search by asset tag, serial number, or model

Response:
```json
{
  "content": [
    {
      "assetId": "number",
      "assetTag": "string",
      "serialNumber": "string",
      "type": {
        "typeId": "number",
        "typeName": "string"
      },
      "make": "string",
      "model": "string",
      "status": "string",
      "currentEmployee": {
        "employeeId": "string",
        "fullName": "string"
      },
      "lastAssignmentDate": "string (ISO-8601)"
    }
  ],
  "totalElements": "number",
  "totalPages": "number",
  "size": "number",
  "number": "number"
}
```

#### Create Asset
```http
POST /assets
```

Request body:
```json
{
  "assetTag": "string",
  "serialNumber": "string",
  "typeId": "number",
  "make": "string",
  "model": "string",
  "specifications": "string (optional)",
  "notes": "string (optional)"
}
```

Response:
```json
{
  "assetId": "number",
  "assetTag": "string",
  "serialNumber": "string",
  "type": {
    "typeId": "number",
    "typeName": "string"
  },
  "make": "string",
  "model": "string",
  "status": "Available",
  "specifications": "string",
  "notes": "string"
}
```

#### Get Asset
```http
GET /assets/{assetId}
```

Response:
```json
{
  "assetId": "number",
  "assetTag": "string",
  "serialNumber": "string",
  "type": {
    "typeId": "number",
    "typeName": "string"
  },
  "make": "string",
  "model": "string",
  "status": "string",
  "specifications": "string",
  "notes": "string",
  "currentEmployee": {
    "employeeId": "string",
    "fullName": "string"
  },
  "lastAssignmentDate": "string (ISO-8601)"
}
```

#### Update Asset
```http
PUT /assets/{assetId}
```

Request body:
```json
{
  "assetTag": "string",
  "serialNumber": "string",
  "typeId": "number",
  "make": "string",
  "model": "string",
  "specifications": "string (optional)",
  "notes": "string (optional)"
}
```

Response: Same as GET /assets/{assetId}

#### Assign Asset
```http
POST /assets/{assetId}/assign
```

Request body:
```json
{
  "employeeId": "string"
}
```

Response: Same as GET /assets/{assetId}

#### Return Asset
```http
POST /assets/{assetId}/return
```

Request body:
```json
{
  "returnStatus": "string (Available, Damaged, In Repair)"
}
```

Response: Same as GET /assets/{assetId}

### Employees

#### List Employees
```http
GET /employees
```

Query Parameters:
- `search` (optional): Search by name or employee ID

Response:
```json
{
  "content": [
    {
      "employeeId": "string",
      "fullName": "string",
      "email": "string",
      "status": "string"
    }
  ],
  "totalElements": "number",
  "totalPages": "number",
  "size": "number",
  "number": "number"
}
```

#### Create Employee
```http
POST /employees
```

Request body:
```json
{
  "employeeId": "string",
  "fullName": "string",
  "email": "string"
}
```

Response:
```json
{
  "employeeId": "string",
  "fullName": "string",
  "email": "string",
  "status": "Active"
}
```

#### Get Employee
```http
GET /employees/{employeeId}
```

Response:
```json
{
  "employeeId": "string",
  "fullName": "string",
  "email": "string",
  "status": "string"
}
```

#### Update Employee
```http
PUT /employees/{employeeId}
```

Request body:
```json
{
  "fullName": "string",
  "email": "string",
  "status": "string"
}
```

Response: Same as GET /employees/{employeeId}

#### Get Employee's Assets
```http
GET /employees/{employeeId}/assets
```

Response:
```json
[
  {
    "assetId": "number",
    "assetTag": "string",
    "serialNumber": "string",
    "type": {
      "typeId": "number",
      "typeName": "string"
    },
    "make": "string",
    "model": "string",
    "status": "string",
    "lastAssignmentDate": "string (ISO-8601)"
  }
]
```

### Hardware Types

#### List Hardware Types
```http
GET /hardware-types
```

Response:
```json
[
  {
    "typeId": "number",
    "typeName": "string"
  }
]
```

## Error Responses

All error responses follow this format:
```json
{
  "timestamp": "string (ISO-8601)",
  "status": "number",
  "error": "string",
  "message": "string",
  "path": "string"
}
```

Common HTTP Status Codes:
- 200: Success
- 201: Created
- 400: Bad Request
- 401: Unauthorized
- 403: Forbidden
- 404: Not Found
- 409: Conflict
- 500: Internal Server Error

## Rate Limiting

The API implements rate limiting to prevent abuse. The current limits are:
- 100 requests per minute per IP address
- 1000 requests per hour per IP address

Rate limit headers are included in responses:
```
X-RateLimit-Limit: 100
X-RateLimit-Remaining: 99
X-RateLimit-Reset: 1609459200
```

## Versioning

The API is versioned through the URL path (/api/v1/). Future versions will increment the version number (v2, v3, etc.).

## Pagination

List endpoints support pagination using the following query parameters:
- `page`: Page number (0-based)
- `size`: Number of items per page
- `sort`: Sort field and direction (e.g., `assetTag,asc`)

Pagination metadata is included in the response:
```json
{
  "content": [...],
  "totalElements": "number",
  "totalPages": "number",
  "size": "number",
  "number": "number"
}
``` 