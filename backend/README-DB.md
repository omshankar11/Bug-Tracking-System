# MySQL Setup

This project can run with MySQL in production. Follow these steps to configure the database.

1. Install MySQL 8.0 or later.
2. Create a database for the application:

```sql
CREATE DATABASE bugtracker CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'buguser'@'localhost' IDENTIFIED BY 'bugpassword';
GRANT ALL PRIVILEGES ON bugtracker.* TO 'buguser'@'localhost';
FLUSH PRIVILEGES;
```

3. Update `src/main/resources/application.properties` with your database credentials.
4. Start the application with Maven:

```bash
./mvnw spring-boot:run
```

Optional production settings:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bugtracker?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=buguser
spring.datasource.password=bugpassword
spring.jpa.hibernate.ddl-auto=update
```
