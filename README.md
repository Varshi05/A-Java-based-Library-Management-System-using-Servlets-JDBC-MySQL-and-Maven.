# Library Management System

A browser-based Library Management System built using Java Servlets, JDBC, MySQL, HTML, and CSS. The application runs on Apache Tomcat 11 and supports adding, viewing, updating, and deleting books.

## Features

- **Add Books:** Validated form with automatically generated book IDs using MySQL `AUTO_INCREMENT`.
- **View Books:** Displays books in a formatted table with an empty-state message and database error handling.
- **Update Books:** Select a book by ID, edit its prefilled details, and save changes.
- **Delete Books:** Select a book, review its details on a confirmation page, and confirm deletion.
- **Server-Side Validation:** Validates user input before database operations.
- **SQL Injection Prevention:** Uses prepared statements for database queries.
- **HTML Escaping:** Displays user-provided content safely as text.
- **Error Handling:** Provides friendly error pages without exposing stack traces.
- **Responsive Design:** Supports desktop and mobile screen sizes.

## Technology Stack

| Technology | Purpose |
|---|---|
| Java 17+ | Application programming language |
| Jakarta Servlets 6.1 | Handles HTTP requests and responses |
| JDBC | Connects Java to MySQL |
| MySQL 8.0 | Relational database |
| MySQL Connector/J | MySQL JDBC driver |
| Apache Tomcat 11.0.x | Web application server |
| HTML5 | Web page structure |
| CSS3 | Styling and responsive layout |
| Apache Maven | Dependency management and WAR packaging |
| Visual Studio Code | Development environment |
| Git and GitHub | Version control and project hosting |

The project was developed using JDK 25.

## Application Architecture

The application follows a simple layered architecture:

```text
Browser
   |
   v
Apache Tomcat
   |
   v
Java Servlets
   |
   v
BookDAO (JDBC and PreparedStatement)
   |
   v
MySQL Database
   |
   v
HTTP Response / Redirect
```

### Main Components

- `com.library.servlet` — Handles HTTP requests, input validation, and responses.
- `com.library.dao.BookDAO` — Contains database operations for books.
- `com.library.model.Book` — Represents a book and its properties.
- `com.library.util.DBConnection` — Creates JDBC connections using database configuration.
- `com.library.util.BookValidator` — Validates book input.
- `com.library.util.HtmlUtil` — Provides HTML output and escaping utilities.
- `src/main/webapp/css/style.css` — Contains application styling.

## Prerequisites

Install the following software before running the project:

- JDK 17 or newer
- Apache Maven
- Apache Tomcat 11.0.x
- MySQL Server 8.0 and MySQL Workbench
- Git

Verify your Java and Maven installations:

```bash
java -version
javac -version
mvn -version
git --version
```

Configure `JAVA_HOME` to point to your JDK installation directory. Configure `CATALINA_HOME` to point to your Tomcat installation directory if you want to use the deployment instructions below.

## Database Setup

1. Start MySQL Server and open MySQL Workbench.
2. Connect to your local MySQL instance. The project configuration uses port `3307` by default.
3. Open `database/schema.sql` in MySQL Workbench.
4. Execute the SQL script.
5. Refresh the Schemas panel and verify that the `librarydb` database and `books` table exist.

You can verify the table structure with:

```sql
USE librarydb;
DESCRIBE books;
```

## Database Configuration

The application reads its database configuration from:

```text
src/main/resources/db.properties
```

If the project includes `db.properties.example`, copy it to `db.properties` in the same directory.

Update the local configuration with your own MySQL username, password, and database URL. The default database URL uses port `3307`.

**Security note:** The real `db.properties` file is excluded using `.gitignore`. Never commit actual database passwords or other secrets to GitHub. The example configuration should contain placeholders only.

## Build the Project

Clone the repository and open the project directory in VS Code or a terminal.

```bash
git clone <repository-url>
cd <repository-folder>
```

Build the application using Maven:

```bash
mvn clean package
```

After a successful build, Maven generates the WAR file:

```text
target/library-management-system.war
```

## Deploy and Run

### Option 1: Use the deployment script

Run `deploy.bat` from the project root if its Tomcat path is configured correctly.

### Option 2: Deploy manually

1. Build the project using Maven.
2. Copy `target/library-management-system.war` into the `webapps` directory of your Tomcat 11 installation.
3. Start Tomcat. On Windows, you can run:

```bat
"%CATALINA_HOME%\bin\catalina.bat" run
```

Keep the terminal open to view server logs. Press `Ctrl+C` in that terminal to stop Tomcat.

Open the application in your browser:

```text
http://localhost:8080/library-management-system/
```

If you change the Java code, rebuild the project and redeploy the updated WAR file before testing your changes.

## Testing Checklist

Use this checklist to verify the main application functionality.

| Test | Expected result |
|---|---|
| Open the home page | Page loads with four menu options |
| Add a valid book | Book is saved successfully |
| View books | Book details appear in a table |
| Submit an empty form | Validation errors are displayed |
| Enter an invalid price | A validation error is displayed |
| Update an existing book | Updated details are saved |
| Update a nonexistent book ID | A book-not-found message appears |
| Cancel a deletion | The book remains in the database |
| Confirm a deletion | The selected book is deleted |
| Open an invalid application URL | A friendly error page appears |
| Stop MySQL and access the book list | A friendly database error is displayed |

## Troubleshooting

| Problem | Possible solution |
|---|---|
| `mvn` is not recognized | Install Maven, configure `PATH`, and reopen the terminal |
| `JAVA_HOME` is not defined correctly | Set it to the JDK directory, not the `bin` directory |
| Build fails because of the Java release version | Check the installed JDK and the Java version configured in `pom.xml` |
| Application returns HTTP 404 | Verify that the WAR is deployed and Tomcat is running |
| Port 8080 is already in use | Stop the conflicting process or configure another Tomcat port |
| `db.properties` is not found | Create the local configuration file and rebuild the project |
| Communications link failure | Check that MySQL is running and the configured port is correct |
| Access denied for MySQL user | Verify the username and password in the local configuration |
| Unknown database `librarydb` | Execute `database/schema.sql` in MySQL Workbench |
| Application displays an unexpected error | Review the Tomcat console and log files |

## Screenshots

Add screenshots of the running application to `docs/screenshots/` and include them here when available.

For example:

```markdown
![Home Page](docs/screenshots/home.png)

![View Books](docs/screenshots/view-books.png)
```

## Known Limitations

- No authentication or user roles are implemented.
- The database password is stored in a local configuration file; this approach is intended for a learning project, not production deployment.
- SSL is not configured for the local development database connection.
- The application does not currently include pagination or book search.
- JDBC connections use `DriverManager` rather than a connection pool.
- CSRF protection has not been implemented for the forms.

## Future Improvements

- Add user authentication and role-based access control.
- Implement book search, filtering, and pagination.
- Add automated unit and integration tests.
- Introduce connection pooling.
- Add CSRF protection and production-ready security configuration.
- Improve the interface with additional accessibility features.

## Author

**Varshita**

GitHub: [Varshi05](https://github.com/Varshi05)

## License

No license has been specified yet. Add a license if you intend to define how others may use, modify, and distribute this project.
