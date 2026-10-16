# Job Portal Management System

A RESTful backend application built with **Java, Spring Boot, Spring
Security, JWT, Spring Data JPA, Hibernate, and MySQL**. It supports user
authentication, job posting, job applications, and recruiter-side
application management.

## Features

-   User registration and login
-   Password hashing with BCrypt
-   JWT-based authentication
-   Role-based access control for `ADMIN`, `RECRUITER`, and `JOB_SEEKER`
-   Job creation, retrieval, updating, and deletion
-   Recruiter ownership checks for job management
-   Job seekers can apply for jobs and view their applications
-   Recruiters can view applications for their jobs
-   Duplicate job-application prevention
-   Recruiter application-status updates (`APPLIED`, `SHORTLISTED`,
    `REJECTED`, `HIRED`)
-   DTO-based API responses that avoid returning user passwords
-   Request validation and centralized exception handling
-   API testing with Postman
-   OpenAPI/Swagger documentation, if enabled in the current
    configuration

## Tech Stack

Technology                Purpose
  ------------------------- ----------------------------------
Java 21                   Programming language
Spring Boot               Application framework
Spring Web MVC            REST API endpoints
Spring Security           Authentication and authorization
JJWT                      Token-based authentication
Spring Data JPA           Data access
Hibernate                 ORM
MySQL                     Relational database
Maven                     Build and dependency management
Jakarta Bean Validation   Request validation
Lombok                    Reduces boilerplate code
Postman                   API testing
Swagger/OpenAPI           API documentation

## Architecture

The application follows a layered architecture:

``` text
Client / Postman
       |
       v
Controller layer
       |
       v
Service layer
       |
       v
Repository layer
       |
       v
JPA / Hibernate
       |
       v
MySQL database
```

Spring Security processes protected requests. For authenticated
requests, the JWT filter validates the bearer token and establishes the
authenticated user before controller authorization rules are applied.

### Main package structure

``` text
com.rajesh.jobportal
├── config
├── controller
├── dto
├── entity
├── enums
├── exception
├── repository
├── security
├── service
└── JobportalApplication.java
```

## Roles

-   **ADMIN** --- administrative access, according to the configured
    authorization rules.
-   **RECRUITER** --- manages job postings and views applications for
    owned jobs; can update application status.
-   **JOB_SEEKER** --- views jobs and manages their own job
    applications.

Administrative accounts should be provisioned through a controlled
process rather than open public registration.

## Prerequisites

-   JDK 21
-   Maven (or use the included Maven Wrapper)
-   MySQL Server
-   Postman (for API testing)

## Setup and Run

### 1. Clone the repository

``` bash
git clone <YOUR_GITHUB_REPOSITORY_URL>
cd <YOUR_PROJECT_DIRECTORY>
```

Replace the placeholders with your repository URL and directory name.

### 2. Create the database

``` sql
CREATE DATABASE jobportal;
```

### 3. Configure the database and JWT secret

Configure `src/main/resources/application.properties` for your local
MySQL instance. For example:

``` properties
spring.application.name=jobportal
spring.datasource.url=jdbc:mysql://localhost:3306/jobportal
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
jwt.secret=${JWT_SECRET}
```

Set `DB_USERNAME`, `DB_PASSWORD`, and `JWT_SECRET` in your environment
before starting the application. `JWT_SECRET` must be a sufficiently
long Base64-encoded secret compatible with the signing algorithm used by
the application.

**Security note:** Do not commit real database credentials, JWT signing
secrets, access tokens, or passwords to GitHub. Move secrets from local
configuration into environment variables before pushing the repository.

### 4. Build the project

Windows Maven Wrapper:

``` powershell
.\mvnw.cmd clean install
```

Or with Maven installed:

``` bash
mvn clean install
```

### 5. Run the application

Windows Maven Wrapper:

``` powershell
.\mvnw.cmd spring-boot:run
```

Or with Maven installed:

``` bash
mvn spring-boot:run
```

The API will normally be available at `http://localhost:8080`.

## API Endpoints

Protected endpoints require:

``` http
Authorization: Bearer <JWT_TOKEN>
```

  ---------------------------------------------------------------------------------------
Method            Endpoint                          Purpose           Access
  ----------------- --------------------------------- ----------------- -----------------
`POST`            `/api/users/register`             Register a user   Public

`POST`            `/api/auth/login`                 Authenticate and  Public
receive a JWT

`GET`             `/api/users`                      List users        Admin, according
to security
configuration

`GET`             `/api/users/{id}`                 Retrieve a user   Authenticated,
according to
security
configuration

`PUT`             `/api/users/{id}`                 Update a user     Verify configured
authorization
before production
use

`DELETE`          `/api/users/{id}`                 Delete a user     Verify configured
authorization
before production
use

`POST`            `/api/jobs`                       Create a job      Recruiter

`GET`             `/api/jobs`                       List jobs         Depends on
security
configuration

`GET`             `/api/jobs/{id}`                  Retrieve a job    Depends on
security
configuration

`PUT`             `/api/jobs/{id}`                  Update an owned   Recruiter / owner
job

`DELETE`          `/api/jobs/{id}`                  Delete an owned   Recruiter / owner
job

`POST`            `/api/applications`               Apply for a job   Job seeker

`GET`             `/api/applications/my`            View the current  Job seeker
seeker's          
applications

`GET`             `/api/applications/recruiter`     View applications Recruiter
for the           
recruiter's jobs

`PUT`             `/api/applications/{id}/status`   Update an         Recruiter / owner
application's     
status
  ---------------------------------------------------------------------------------------

Review authorization rules in `SecurityConfig` and controller
annotations before deploying publicly. User update/delete endpoints
should enforce ownership or administrator access.

## Example Requests

### Register a job seeker

`POST /api/users/register`

``` json
{
  "name": "Example Job Seeker",
  "email": "seeker@example.com",
  "phoneNumber": 9876543210,
  "password": "ChangeMe123!",
  "role": "JOB_SEEKER"
}
```

Use a unique email address for each registration.

### Log in

`POST /api/auth/login`

``` json
{
  "email": "seeker@example.com",
  "password": "ChangeMe123!"
}
```

Copy the JWT returned by the API and use it as a Bearer Token in Postman
for protected endpoints.

### Create a job

`POST /api/jobs`

``` json
{
  "title": "Java Backend Developer",
  "description": "Looking for a Java developer with Spring Boot and MySQL skills.",
  "companyName": "Example Company",
  "location": "Hyderabad",
  "salary": 600000,
  "employmentType": "FULL_TIME"
}
```

Send this request with a recruiter token.

### Apply for a job

`POST /api/applications`

``` json
{
  "jobId": 1
}
```

Send this request with a job-seeker token and replace `1` with an
existing job ID.

### Update application status

`PUT /api/applications/1/status`

``` json
{
  "status": "SHORTLISTED"
}
```

Send this request with the token of the recruiter who owns the job
associated with the application.

## Error Responses

Expected HTTP status codes include:

-   `400 Bad Request` --- invalid request data
-   `401 Unauthorized` --- missing or invalid authentication, depending
    on the security configuration
-   `403 Forbidden` --- insufficient role or ownership permissions
-   `404 Not Found` --- requested resource does not exist
-   `409 Conflict` --- duplicate application, if handled by the
    configured exception handler
-   `500 Internal Server Error` --- unexpected server-side error

Verify the actual responses with Postman.

## API Documentation

If OpenAPI/Swagger is enabled successfully, open:

`http://localhost:8080/swagger-ui/index.html`

## Testing Checklist

-   [ ] Register a job seeker and a recruiter
-   [ ] Log in and obtain JWTs for both roles
-   [ ] Create a job using the recruiter token
-   [ ] Apply using the job-seeker token
-   [ ] View the job seeker's applications
-   [ ] View applications for the recruiter's jobs
-   [ ] Update application status as the owning recruiter
-   [ ] Confirm a job seeker cannot update application status
-   [ ] Confirm a different recruiter cannot update another recruiter's
    application
-   [ ] Confirm duplicate applications are rejected
-   [ ] Confirm nonexistent resource IDs return the intended error
-   [ ] Verify user update/delete authorization before production use

## Future Improvements

-   Automated unit and integration tests with JUnit and Mockito
-   Pagination, sorting, and richer job search/filtering
-   More granular authorization for user management
-   Production profile and environment-based configuration
-   Continuous integration build workflow
-   Optional frontend, resume upload, and email notifications

## Author

**Rajesh P**

This project was developed as a learning and portfolio project to
practice Java backend development, REST API design, Spring Security, JWT
authentication, and relational database integration.
