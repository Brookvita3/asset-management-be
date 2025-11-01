# Asset Management System

##  Getting Started

Follow these instructions to get the project up and running locally for development.

### Prerequisites

You must have the following tools installed:
* JDK 17
* Apache Maven

---
### 1. Configure Environment Variables

Your Spring application (`application.yml` or `application.properties`) is set up to read credentials from environment variables.

You must set these variables in your terminal session **before** running the application.

* `DATABASE_URL`: The JDBC connection string.
* `DATABASE_USERNAME`: The username.
* `DATABASE_PASSWORD`: The password.
* `SECRET_KEY`: Your unique secret for JWT.

---

### 2. Run the Spring Boot App (Maven)

1.  **Set the variables in your terminal.**

    *(Example for Linux/macOS)*
    ```bash
    export DATABASE_URL=jdbc:mysql://localhost:5432/[your_db_name]
    export DATABASE_USERNAME=[your_username]
    export DATABASE_PASSWORD=[your_password]
    export SECRET_KEY=your-very-strong-and-secret-key-12345
    ```

    *(Example for Windows PowerShell)*
    ```powershell
    $env:DATABASE_URL = "jdbc:mysql://localhost:5432/[your_db_name]"
    $env:DATABASE_USERNAME = "[your_username]"
    $env:DATABASE_PASSWORD = "[your_password]"
    $env:SECRET_KEY = "your-very-strong-and-secret-key-12345"
    ```

2.  **Run the application:**
    Once the variables are set, run the app from the same terminal session:
    ```bash
    mvn spring-boot:run
    ```
    The application will start and connect to the MySQL database using the environment variables you provided.

