# Internal Go Links Service (Java / Spring Boot)

An internal URL shortener service built with **Java 17**, **Spring Boot 3**, **Spring Data JPA**, **H2 Database**, and **Tailwind CSS**.

## Features
- **302 Fast Redirection**: Redirects `go/keyword` to target destinations.
- **Management UI**: Web dashboard to add, search, and delete shortcuts.
- **Analytics**: Real-time click counting for all shortcuts.
- **In-Memory H2 DB**: Pre-seeded with example shortcuts (`go/design-system`, `go/oncall`, `go/payroll`).

## How to Run

1. Make sure you have **Java 17+** and **Maven** installed.
2. Run the application:
   ```bash
   mvn spring-boot:run
   ```
3. Open your browser and navigate to:
   - Dashboard UI: `http://localhost:8080/`
   - Test Redirection: `http://localhost:8080/oncall`

## Local `go/` Domain Setup
To make `go/<keyword>` work directly in your browser:
- Edit `/etc/hosts` (macOS/Linux) or `C:\Windows\System32\drivers\etc\hosts` (Windows):
  ```text
  127.0.0.1   go
  ```
- Access shortcuts directly via `http://go:8080/design-system`.
