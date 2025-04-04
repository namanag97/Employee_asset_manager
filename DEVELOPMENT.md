# EHD Development Guide

## Prerequisites

### Required Software
- Java 17 or later
- Node.js 16 or later
- PostgreSQL 14 or later
- Maven 3.8+ or Gradle 7.5+
- Git
- Docker and Docker Compose (optional, for containerized development)

### Recommended Tools
- IntelliJ IDEA or VS Code
- Postman or Insomnia for API testing
- pgAdmin or DBeaver for database management
- Git client (command line or GUI)

## Project Structure

```
ehd/
├── backend/                 # Spring Boot application
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/ehd/mvp/
│   │   │   │       ├── config/        # Configuration classes
│   │   │   │       ├── controller/    # REST controllers
│   │   │   │       ├── dto/           # Data Transfer Objects
│   │   │   │       ├── entity/        # JPA entities
│   │   │   │       ├── repository/    # JPA repositories
│   │   │   │       ├── security/      # Security configuration
│   │   │   │       ├── service/       # Business logic
│   │   │   │       └── util/          # Utility classes
│   │   │   └── resources/
│   │   │       ├── application.yml    # Application configuration
│   │   │       └── db/                # Database migrations
│   │   └── test/                      # Test classes
│   └── pom.xml                        # Maven configuration
│
├── frontend/              # Next.js application
│   ├── src/
│   │   ├── app/          # Next.js app directory
│   │   ├── components/   # React components
│   │   ├── lib/          # Utility functions
│   │   ├── types/        # TypeScript types
│   │   └── styles/       # CSS styles
│   ├── public/           # Static files
│   └── package.json      # NPM configuration
│
└── docker/               # Docker configuration
    ├── backend/
    ├── frontend/
    └── docker-compose.yml
```

## Development Environment Setup

### 1. Database Setup

#### Using Docker (Recommended)
```bash
# Start PostgreSQL container
docker run --name ehd-postgres \
  -e POSTGRES_USER=ehd \
  -e POSTGRES_PASSWORD=ehd123 \
  -e POSTGRES_DB=ehd \
  -p 5432:5432 \
  -d postgres:14
```

#### Manual Setup
1. Install PostgreSQL 14+
2. Create database and user:
```sql
CREATE USER ehd WITH PASSWORD 'ehd123';
CREATE DATABASE ehd OWNER ehd;
```

### 2. Backend Setup

1. Clone the repository:
```bash
git clone https://github.com/your-org/ehd.git
cd ehd/backend
```

2. Configure database connection in `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ehd
    username: ehd
    password: ehd123
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

3. Build and run:
```bash
# Using Maven
mvn clean install
mvn spring-boot:run

# Using Gradle
./gradlew build
./gradlew bootRun
```

### 3. Frontend Setup

1. Navigate to frontend directory:
```bash
cd ../frontend
```

2. Install dependencies:
```bash
npm install
```

3. Configure environment variables in `.env.local`:
```env
NEXT_PUBLIC_API_URL=http://localhost:8080/api/v1
```

4. Start development server:
```bash
npm run dev
```

## Development Workflow

### 1. Git Workflow

1. Create feature branch:
```bash
git checkout -b feature/your-feature-name
```

2. Make changes and commit:
```bash
git add .
git commit -m "feat: description of your changes"
```

3. Push changes:
```bash
git push origin feature/your-feature-name
```

4. Create pull request on GitHub

### 2. Backend Development

#### Creating a New Entity

1. Create entity class in `src/main/java/com/ehd/mvp/entity/`:
```java
@Entity
@Table(name = "your_table")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class YourEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Add fields with appropriate annotations
}
```

2. Create repository interface in `src/main/java/com/ehd/mvp/repository/`:
```java
@Repository
public interface YourEntityRepository extends JpaRepository<YourEntity, Long> {
    // Add custom query methods if needed
}
```

3. Create service class in `src/main/java/com/ehd/mvp/service/`:
```java
@Service
@Transactional
public class YourEntityService {
    private final YourEntityRepository repository;
    
    // Add business logic methods
}
```

4. Create controller in `src/main/java/com/ehd/mvp/controller/`:
```java
@RestController
@RequestMapping("/api/v1/your-entities")
public class YourEntityController {
    private final YourEntityService service;
    
    // Add endpoint methods
}
```

#### Testing

1. Create test class in `src/test/java/com/ehd/mvp/`:
```java
@SpringBootTest
class YourEntityServiceTest {
    @Autowired
    private YourEntityService service;
    
    // Add test methods
}
```

### 3. Frontend Development

#### Creating a New Component

1. Create component file in `src/components/`:
```typescript
interface YourComponentProps {
    // Add props interface
}

export const YourComponent: React.FC<YourComponentProps> = ({ /* props */ }) => {
    // Add component logic
    return (
        // Add JSX
    );
};
```

2. Create page in `src/app/your-page/`:
```typescript
export default function YourPage() {
    // Add page logic
    return (
        // Add JSX
    );
}
```

#### API Integration

1. Create API service in `src/lib/api/`:
```typescript
export const yourEntityApi = {
    getAll: async () => {
        const response = await axios.get('/api/v1/your-entities');
        return response.data;
    },
    // Add other API methods
};
```

2. Use in component with React Query:
```typescript
const { data, isLoading, error } = useQuery('your-entities', yourEntityApi.getAll);
```

## Code Style and Best Practices

### Backend

1. **Naming Conventions**
   - Class names: PascalCase
   - Method names: camelCase
   - Variable names: camelCase
   - Constants: UPPER_SNAKE_CASE

2. **Annotations**
   - Use Lombok annotations for boilerplate code
   - Use Spring annotations consistently
   - Add Javadoc for public methods

3. **Error Handling**
   - Use custom exceptions
   - Implement global exception handler
   - Return appropriate HTTP status codes

### Frontend

1. **Component Structure**
   - Use functional components with hooks
   - Separate presentational and container components
   - Use TypeScript for type safety

2. **State Management**
   - Use React Query for server state
   - Use Context for global UI state
   - Use local state for component-specific state

3. **Styling**
   - Use Tailwind CSS utility classes
   - Follow responsive design principles
   - Maintain consistent spacing and typography

## Testing

### Backend Tests

1. **Unit Tests**
   - Test service layer methods
   - Mock dependencies
   - Use JUnit 5 and Mockito

2. **Integration Tests**
   - Test controller endpoints
   - Use TestRestTemplate
   - Test database operations

### Frontend Tests

1. **Component Tests**
   - Test component rendering
   - Test user interactions
   - Use React Testing Library

2. **Integration Tests**
   - Test API integration
   - Test form submissions
   - Test navigation

## Debugging

### Backend Debugging

1. **Logging**
   - Use SLF4J with Logback
   - Add appropriate log levels
   - Include context in log messages

2. **Remote Debugging**
   - Enable remote debugging in IDE
   - Attach debugger to running application
   - Set breakpoints in code

### Frontend Debugging

1. **Browser DevTools**
   - Use React DevTools
   - Monitor network requests
   - Check console logs

2. **Debugging Tools**
   - Use VS Code debugger
   - Use React Query DevTools
   - Use Redux DevTools (if using Redux)

## Performance Optimization

### Backend

1. **Database**
   - Use appropriate indexes
   - Optimize queries
   - Use connection pooling

2. **Caching**
   - Implement caching where appropriate
   - Use Spring Cache abstraction
   - Cache frequently accessed data

### Frontend

1. **Code Splitting**
   - Use dynamic imports
   - Split routes
   - Lazy load components

2. **Performance Monitoring**
   - Use React.memo for pure components
   - Use useMemo and useCallback
   - Monitor bundle size

## Security

### Backend Security

1. **Authentication**
   - Use JWT for stateless authentication
   - Implement proper password hashing
   - Use secure session management

2. **Authorization**
   - Implement role-based access control
   - Use method-level security
   - Validate user permissions

### Frontend Security

1. **Data Protection**
   - Store sensitive data securely
   - Use HTTPS for API calls
   - Implement CSRF protection

2. **Input Validation**
   - Validate form inputs
   - Sanitize user input
   - Prevent XSS attacks

## Deployment

### Backend Deployment

1. **Build**
```bash
mvn clean package -DskipTests
```

2. **Run**
```bash
java -jar target/ehd-backend.jar
```

### Frontend Deployment

1. **Build**
```bash
npm run build
```

2. **Deploy**
- Copy build output to web server
- Configure reverse proxy
- Set up SSL certificate

## Troubleshooting

### Common Issues

1. **Database Connection**
   - Check database credentials
   - Verify database is running
   - Check network connectivity

2. **API Integration**
   - Verify API endpoints
   - Check CORS configuration
   - Validate request/response format

3. **Build Issues**
   - Clear Maven/Gradle cache
   - Update dependencies
   - Check Java/Node.js versions

## Support and Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Next.js Documentation](https://nextjs.org/docs)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [React Documentation](https://reactjs.org/docs)
- [TypeScript Documentation](https://www.typescriptlang.org/docs/) 