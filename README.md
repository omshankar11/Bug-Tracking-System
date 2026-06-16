# Bug Tracking System

A comprehensive bug tracking application built with Spring Boot, Thymeleaf, MySQL, and Spring Security.

## Features

- **User Management**: User registration and authentication
- **Bug Tracking**: Create, update, and manage bug reports
- **Project Management**: Organize bugs by projects (Admin only)
- **Search & Filter**: Find bugs by various criteria
- **Comments**: Add comments to bug reports
- **Role-based Access**: Admin and User roles with different permissions

## Technology Stack

- **Backend**: Spring Boot 3.2.0, Java 17
- **Frontend**: Thymeleaf templates
- **Database**: MySQL 8.0 (Production) / H2 (Development)
- **Security**: Spring Security with BCrypt password encoding
- **ORM**: Hibernate/JPA
- **Build Tool**: Maven

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0 (for production) or H2 (for development)

## Quick Start

### Development (H2 Database)

1. Clone the repository
2. Navigate to the backend directory:
   ```bash
   cd backend
   ```
3. Run with development profile:
   ```bash
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
   ```
4. Access the application at http://localhost:8080

### Production (MySQL Database)

1. Set up MySQL database and user (see README-DB.md)
2. Update `src/main/resources/application.properties` with your database credentials
3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

## Database Setup

See [README-DB.md](backend/README-DB.md) for detailed MySQL setup instructions.

## Configuration

The application uses different profiles:

- `dev`: Uses H2 in-memory database (default for development)
- `default`: Uses MySQL database (production)

## Testing

Run tests with:
```bash
./mvnw test
```

## Project Structure

```
backend/
├── .mvn/                        # Maven wrapper files
├── mvnw
├── mvnw.cmd
├── pom.xml                      # Maven build file
├── README-DB.md                 # MySQL setup instructions
├── src/
│   ├── main/
│   │   ├── java/com/example/bugtracker/
│   │   │   ├── controller/      # Web controllers
│   │   │   ├── model/           # JPA entities
│   │   │   ├── repository/      # Spring Data repositories
│   │   │   ├── service/         # Business logic
│   │   │   └── config/          # Security configuration
│   │   └── resources/
│   │       ├── templates/       # Thymeleaf templates
│   │       ├── static/          # CSS, JS, images
│   │       ├── application.properties
│   │       └── application-dev.properties
│   └── test/
│       └── java/com/example/bugtracker/
│           └── BugtrackerApplicationTests.java
``` 

## Security

- Form-based authentication
- Password encryption with BCrypt
- Role-based authorization (USER, ADMIN)
- CSRF protection enabled

## API Endpoints

- `GET /` - Home page (redirects to login if not authenticated)
- `GET /login` - Login page
- `GET /register` - User registration
- `GET /bugs` - Bug dashboard
- `GET /admin/projects` - Project management (Admin only)
- `GET /admin/users` - User management (Admin only)

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Run tests
5. Submit a pull request

## License

This project is for educational purposes.