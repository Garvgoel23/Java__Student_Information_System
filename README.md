# Student Information System (SIS)

A complete Student Information System featuring a **Web Dashboard** for students and admins, and a **Java Swing Desktop Application** for administrative CRUD operations.

---

## 🚀 Quick Start: Start Clean & Build

Follow these steps to reset the environment and build the project from scratch.

### 1. Reset & Setup Database
Execute these commands to drop any existing database and create a fresh one with sample data.

```bash
# Log into MySQL (e.g., as root)
mysql -u root -e "DROP DATABASE IF EXISTS sis_db; CREATE DATABASE sis_db;"

# Import the schema and sample data
mysql -u root sis_db < database/schema.sql
```

> **Note:** The project is currently configured to connect to MySQL with the `root` user and **no password**. If your setup requires a password, update it in:
> - `web/src/main/java/com/sis/util/DBConnection.java` (Line 11)
> - `desktop/AdminDesktop.java` (Line 18)

### 2. Build Web Application
The web module uses Maven for dependency management and packaging. This project is configured for **Jakarta EE 10** (JSTL 3.0, Servlet 6.0, JSP 3.1).

```bash
mvn clean install -f web/pom.xml
```

---

## 🏗 Running in Tomcat

### Manual Deployment (Standalone Tomcat 10.1+)
1. Ensure you have **Apache Tomcat 10.1+** installed (required for Jakarta EE 10).
2. Build the project using `mvn clean install -f web/pom.xml`.
3. Copy the generated WAR file:
   - Source: `web/target/sis.war`
   - Destination: `[TOMCAT_HOME]/webapps/`
4. Start Tomcat and access the application:
   - Access: `http://localhost:8080/sis`

---

## 🖥 Build & Run Desktop Admin App
The desktop application requires the MySQL JDBC driver. After running the Maven build in the web module, the driver will be available in the `target` directory.

```bash
cd desktop

# Compile (Mac/Linux/Windows)
javac -cp "../web/target/sis/WEB-INF/lib/mysql-connector-j-8.0.33.jar" AdminDesktop.java

# Run (Mac/Linux)
java -cp ".:../web/target/sis/WEB-INF/lib/mysql-connector-j-8.0.33.jar" AdminDesktop

# Run (Windows)
java -cp ".;../web/target/sis/WEB-INF/lib/mysql-connector-j-8.0.33.jar" AdminDesktop
```

---

## 🔐 Login Credentials

Use these credentials to test the application after setup:

| Role        | Username | Password   | Platform       |
|-------------|----------|------------|----------------|
| **Admin**   | `admin`  | `admin123` | Web & Desktop  |
| **Student** | `CS001`  | `pass123`  | Web Dashboard  |

---

## 🛠 Features
- **Web Dashboard**: Student profile view, marks, and attendance tracking.
- **Admin Web Portal**: Full student record management via browser.
- **Admin Desktop App**: High-performance Swing-based CRUD for bulk management.
- **Database**: MySQL 8.0+ with optimized JDBC connection strings.

