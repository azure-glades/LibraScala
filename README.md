## Project Architecture

The project is built with a clean, **layered architecture** that provides a strong separation of concerns. This design makes the application scalable, maintainable, and easy to debug. The three primary layers are the Controller, Service, and Repository layers.

***

### 1. Controller Layer

This is the presentation layer that serves as the "front door" to the application. It handles all incoming **HTTP requests** (POST, GET, PUT, DELETE) from clients. Its main responsibilities are to receive requests, validate input, and route them to the correct service methods. The endpoints are designed in a **RESTful** manner and are split into dedicated classes for users, books, and loans, improving organization.

***

### 2. Service Layer

The service layer contains all the core **business logic** and rules of the application (e.g., checking for user existence, managing borrow limits, calculating fines). This is the "brain" of the application. It coordinates actions between the controller and the repository layers. Methods in this layer are typically **transactional** (`@Transactional`) to ensure that all database operations within a single method succeed or fail as a single unit, maintaining data integrity.

***

### 3. Repository Layer

This is the data persistence layer. It is responsible for all database interactions. Instead of using custom Data Access Objects (DAOs), it leverages **Spring Data JPA** interfaces (`JpaRepository`). This provides a rich set of built-in methods for performing **CRUD** (Create, Read, Update, Delete) operations without writing any boilerplate code. It also supports **derived query methods**, which allow for complex queries simply by defining the method name. This layer currently connects to a **PostgreSQL** database.