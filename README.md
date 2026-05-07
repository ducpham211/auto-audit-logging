# Auto Audit Logging Starter

[![Java Support](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)

**Auto Audit Logging Starter** is a plug-and-play library for the Spring Boot ecosystem. This project provides an automatic mechanism to track and log all sensitive data modifications using Aspect-Oriented Programming (AOP). It eliminates the need for developers to manually write logging code in every business logic function.

## 🚀 Key Features

*   **Plug-and-play Integration:** Works immediately just by adding the dependency and configuring the `application.properties` file.
*   **Non-Invasive:** Completely separates logging logic from the main business logic using the Custom `@AutoAudit` Annotation.
*   **Detailed Data Collection:** Automatically extracts:
    *   Executor's name (from Spring Security Context).
    *   Class and Method names.
    *   Input Arguments.
    *   Execution status (Success / Exception).
    *   Execution time.
*   **Data Masking:** Automatically masks sensitive data such as passwords and credit card numbers in the arguments before logging.
*   **Memory Safe (Skip Large Objects):** Automatically skips large files (MultipartFile, byte[]) to avoid OutOfMemory (OOM) risks.
*   **High Performance:** Logging is completely **Asynchronous**, ensuring zero latency impact on the main transaction.
*   **Flexible:** Supports pushing logs to the Console (JSON format) or inserting into a Database (PostgreSQL).

## 🏗 Architecture

The library operates on a simple, high-performance asynchronous flow:

```text
Method call → @AutoAudit intercept (AOP)
            → Extract context (user, args, time)
            → Mask sensitive fields
            → Async queue (ThreadPool)
            → Log to Console / DB
```

*(Visual Flow)*
```mermaid
flowchart LR
    A[Method call] -->|@AutoAudit| B(AOP Interceptor)
    B --> C{Extract Context}
    C -->|user, args, time| D[Mask Sensitive Fields]
    D --> E[Async Queue / ThreadPool]
    E --> F[(Log to Console / DB)]
```

## 🛠 System Requirements & Technologies

*   **Java:** 17+
*   **Framework:** Spring Boot 3.x, Spring AOP, Spring Data JPA
*   **Mechanism:** Standard packaged via Spring Boot 3 Auto-Configuration mechanism (`META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`).

---

## 💻 Local Installation Guide (For Developers)

Since this is a local Spring Boot Starter library, you need to build and install it into your local Maven Repository before using it in other projects.

### 1. Build and Install

1.  Clone this repository to your machine.
2.  Open your Terminal (or CMD/PowerShell) at the project root directory (where `pom.xml` is located).
3.  Run the following command to build and push the library to your local `.m2` repository:

```bash
mvn clean install
```

> **Note:** If you are using an IDE like IntelliJ IDEA or Eclipse, you can open the **Maven** tab -> select project -> **Lifecycle** -> run `clean` then `install`.

When the Terminal shows `BUILD SUCCESS`, the library is ready on your machine.

---

## 📖 Usage Guide in Other Projects

After successfully running `install` in the previous step, you can apply this library to any Spring Boot 3 project.

### 1. Add Dependency

Open the `pom.xml` file of the target project and add the following dependency:

```xml
<dependency>
    <groupId>io.github.ducpham211</groupId>
    <artifactId>auto-audit-logging-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### 2. Enable Audit Logging

The library uses the `@ConditionalOnProperty` mechanism, so you must explicitly enable it.
In the `application.properties` (or `application.yml`) file of your project, add the configuration:

```properties
# Enable Auto Audit Logging library
audit.enabled=true
```

### 3. Use the `@AutoAudit` Annotation

You only need to place the `@AutoAudit` annotation on any Service or Controller method where you want to track operations:

```java
import io.github.ducpham211.audit.annotation.AutoAudit;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @AutoAudit
    public void updateUserProfile(Long userId, String newName) {
        // Your business logic here...
    }
}
```

Whenever the `updateUserProfile` function is called, the system will automatically scan, extract information, and record the log asynchronously in the background.

## 🤝 Contributing
All contributions (Pull Requests, Issues) are welcome to make this library better!