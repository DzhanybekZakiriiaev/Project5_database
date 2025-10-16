# API Unit Tests Documentation

This directory contains comprehensive unit and integration tests for the Base Database Access Application.

## Test Structure

### Controller Tests
- **AuthControllerTest**: Tests authentication endpoints (signin, signup)
- **MachineControllerTest**: Tests facilities machine management endpoints
- **ItemControllerTest**: Tests inventory item management endpoints

### Service Tests
- **DefaultBaseServiceTest**: Tests the generic base service implementation

### Security Tests
- **SecurityConfigTest**: Tests Spring Security configuration and role-based access control

### Integration Tests
- **AuthenticationIntegrationTest**: End-to-end authentication flow testing

## Test Coverage

### Authentication Controller
- ✅ User registration with valid/invalid data
- ✅ User login with valid/invalid credentials
- ✅ Username/email uniqueness validation
- ✅ JWT token generation and validation

### Facilities Controller
- ✅ CRUD operations for machines
- ✅ Role-based access control (USER vs FACILITIES_ADMIN)
- ✅ Input validation and error handling
- ✅ Cross-schema access restrictions

### Inventory Controller
- ✅ CRUD operations for items
- ✅ Role-based access control (USER vs INVENTORY_ADMIN)
- ✅ Input validation and error handling
- ✅ Cross-schema access restrictions

### Service Layer
- ✅ Generic CRUD operations
- ✅ Pagination support
- ✅ Exception handling
- ✅ Entity validation

### Security Configuration
- ✅ Public endpoint access (Swagger, authentication)
- ✅ Protected endpoint access control
- ✅ Role-based authorization
- ✅ JWT token validation

### Integration Tests
- ✅ Complete user registration and authentication flow
- ✅ Multiple user scenarios
- ✅ Token-based API access
- ✅ Cross-schema access restrictions

## Running Tests

### Run All Tests
```bash
mvn test
```

### Run Specific Test Classes
```bash
mvn test -Dtest=AuthControllerTest
mvn test -Dtest=MachineControllerTest
mvn test -Dtest=ItemControllerTest
mvn test -Dtest=SecurityConfigTest
mvn test -Dtest=AuthenticationIntegrationTest
```

### Run Test Suite
```bash
mvn test -Dtest=TestSuite
```

### Run Tests with Coverage Report
```bash
mvn clean test jacoco:report
```

## Test Configuration

### Test Database
- Uses H2 in-memory database for testing
- Automatically creates/drops schema for each test
- No external database dependencies

### Test Properties
- `application-test.properties`: Test-specific configuration
- Disables Flyway migrations
- Uses shorter JWT expiration for faster tests
- Enables debug logging

### Mock Objects
- Repository layer is mocked using `@MockBean`
- External dependencies are mocked
- Security context is simulated with `@WithMockUser`

## Test Data

### Test Users
- Various roles: USER, FACILITIES_ADMIN, INVENTORY_ADMIN
- Different scenarios: valid users, invalid credentials, existing users

### Test Entities
- Sample machines, items, parts, logs, reports
- Valid and invalid data scenarios
- Edge cases and boundary conditions

## Assertions

### HTTP Status Codes
- 200 OK for successful operations
- 201 Created for resource creation
- 400 Bad Request for validation errors
- 401 Unauthorized for authentication failures
- 403 Forbidden for authorization failures
- 404 Not Found for missing resources
- 204 No Content for successful deletions

### Response Content
- JSON structure validation
- Field value assertions
- Error message verification
- Token presence and format

## Security Testing

### Authentication
- Valid/invalid credentials
- Token generation and validation
- Token expiration scenarios
- Multiple user authentication

### Authorization
- Role-based access control
- Cross-schema restrictions
- Admin vs user permissions
- Public vs protected endpoints

## Performance Considerations

- Tests use in-memory H2 database for speed
- Mock objects reduce external dependencies
- Transaction rollback for test isolation
- Minimal test data for faster execution

## Best Practices

1. **Test Isolation**: Each test is independent
2. **Clear Naming**: Test methods describe what they test
3. **Arrange-Act-Assert**: Clear test structure
4. **Mock External Dependencies**: Focus on unit under test
5. **Comprehensive Coverage**: Test happy path and error scenarios
6. **Realistic Data**: Use realistic test data
7. **Security Focus**: Verify security constraints

## Troubleshooting

### Common Issues
1. **Port Conflicts**: Tests use random ports (server.port=0)
2. **Database Issues**: H2 in-memory database is auto-configured
3. **Security Context**: Use @WithMockUser for authenticated tests
4. **CSRF Tokens**: Include .with(csrf()) for state-changing operations

### Debug Mode
Enable debug logging by setting:
```properties
logging.level.com.example.softwaredesigntechniques=DEBUG
logging.level.org.springframework.security=DEBUG
```

## Future Enhancements

- [ ] Add performance tests
- [ ] Add contract tests
- [ ] Add load testing
- [ ] Add API documentation tests
- [ ] Add database migration tests
