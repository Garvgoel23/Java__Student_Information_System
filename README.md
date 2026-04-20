# Student Information System (SIS)

A complete Student Information System featuring a **Web Dashboard** for students and admins, and a **Java Swing Desktop Application** for administrative CRUD operations.

> **Note:** This project uses **PostgreSQL** as the database (migrated from MySQL).

---

## 🚀 Quick Start: Start Clean & Build

Follow these steps to reset the environment and build the project from scratch.

### 1. Reset & Setup Database
Execute these commands to drop any existing database and create a fresh one with sample data.

```bash
# Create the database (as postgres superuser)
psql -U postgres -c "DROP DATABASE IF EXISTS sis_db;"
psql -U postgres -c "CREATE DATABASE sis_db;"

# Import the schema and sample data
psql -U postgres -d sis_db -f database/schema.sql
```

> **Note:** The project connects to PostgreSQL with user `postgres` and password `postgres`. If your setup uses different credentials, update them in:
> - `web/src/main/java/com/sis/util/DBConnection.java` (Lines 9–11)
> - `desktop/AdminDesktop.java` (Lines 15–17)

### 2. Build Web Application
The web module uses Maven for dependency management and packaging. This project is configured for **Jakarta EE 10** (JSTL 3.0, Servlet 6.0, JSP 3.1) with the **PostgreSQL JDBC driver (42.7.3)**.

```bash
mvn clean install -f web/pom.xml
```

---

## 🏗 Running in Tomcat

### Option A: Embedded Tomcat (via Maven plugin)
The easiest way — no separate Tomcat installation needed:

```bash
mvn tomcat7:run -f web/pom.xml
```

Then access: `http://localhost:8081/sis`

### Option B: Manual Deployment (Standalone Tomcat 10.1+)
1. Ensure you have **Apache Tomcat 10.1+** installed (required for Jakarta EE 10).
2. Build the project using `mvn clean install -f web/pom.xml`.
3. Copy the generated WAR file:
   - Source: `web/target/sis.war`
   - Destination: `[TOMCAT_HOME]/webapps/`
4. Start Tomcat and access the application:
   - Access: `http://localhost:8080/sis`

---

## 🖥 Build & Run Desktop Admin App
The desktop application requires the PostgreSQL JDBC driver. After running the Maven build in the web module, the driver will be available in the Maven local repository.

First, find the driver JAR path:
```
C:\Users\<you>\.m2\repository\org\postgresql\postgresql\42.7.3\postgresql-42.7.3.jar
```

```bash
cd desktop

# Compile (Windows)
javac -cp ".;%USERPROFILE%\.m2\repository\org\postgresql\postgresql\42.7.3\postgresql-42.7.3.jar" AdminDesktop.java

# Run (Windows)
java -cp ".;%USERPROFILE%\.m2\repository\org\postgresql\postgresql\42.7.3\postgresql-42.7.3.jar" AdminDesktop
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
- **Database**: PostgreSQL 17 with JDBC connection (port 5432).
