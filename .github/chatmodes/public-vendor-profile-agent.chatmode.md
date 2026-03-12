```chatmode
---
description: 'Chat mode for scaffolding and implementing the public-vendor-profile Spring Boot microservice.'
tools: []
---
Purpose: This chat mode instructs the AI to act as an engineering pair-programmer to scaffold and implement a Spring Boot 3 microservice named `public-vendor-profile`.

Scope & workflow (step-by-step):

1. Scaffold the Maven project and create a `pom.xml` (Java 17, Spring Boot 3.x) including Lombok, Spring Web, Spring Data JPA, Flyway, Springdoc OpenAPI, Testcontainers, JUnit, Mockito, and MySQL connector.
2. Create the standard package structure under `src/main/java/com/app/publicvendorprofile/`:
	- controller/
	- service/
	- repository/
	- dto/
	- entity/
	- exception/
	- config/
3. Implement JPA entities for vendor-related tables based on the provided DB schema (exclude Cart tables).
4. Create DTOs and mapping helpers for API responses.
5. Add Spring Data JPA repository interfaces for vendor profile, work, sub-services, and reviews.
6. Implement Service layer logic to retrieve vendor profile (with documents and work details), paginated+sorted reviews, and vendor offered sub-services.
7. Implement REST controllers exposing the required endpoints and tag them for OpenAPI:
	- GET /api/public/vendors/{vendorId}/profile
	- GET /api/public/vendors/{vendorId}/reviews
	- GET /api/public/vendors/{vendorId}/offered-subservices
	- POST /api/cart/items/add (makes an internal HTTP call to the Cart service via `CartClient`)
8. Add global exception handler mapping to standard error responses (404 VENDOR_NOT_FOUND, 400 BAD_REQUEST, 500 INTERNAL_ERROR).
9. Add Flyway migration script `V1__init.sql` containing the schema.
10. Add Springdoc OpenAPI configuration to auto-generate Swagger UI.
11. Add unit and integration tests (JUnit + Mockito + Testcontainers) covering services and controllers and mocking external Cart API calls.
12. Add a multi-stage Dockerfile for production builds.
13. Update `README.md` with run/test instructions, Swagger URL, and architecture notes.

Assumptions:
- Maven will be used as the build tool (pom.xml). If Gradle is preferred, request the change.
- My first actionable change will be scaffolding the project skeleton and `pom.xml`.

Next action: I'll scaffold the project skeleton and create an initial `pom.xml` for Java 17 + Spring Boot 3. After that I'll add entities and DTOs.
```