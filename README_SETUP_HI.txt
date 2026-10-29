PAYPILOT - EMPLOYEE PAYROLL MANAGEMENT SYSTEM
============================================

WHAT'S IMPROVED
- Professional responsive dashboard with payroll summary cards.
- Searchable employee table with add, edit, delete and payslip actions.
- Salary slip preview with Print / Save as PDF.
- CSV export for employee records.
- Employee type distribution and payroll overview visualizations.
- Responsive layout for desktop, tablet and mobile.
- UI is served by Spring Boot; backend business logic and salary calculations are Java.

TECH STACK
- Java 17
- Spring Boot 3.5.5 (Java backend and REST API)
- HTML, CSS and JavaScript (frontend only)
- Maven

IMPORTANT STORAGE NOTE
This version keeps employee records in memory, just like the original project. Records are lost when the application restarts. MySQL persistence is not configured in this ZIP yet. The frontend uses the Java REST API; JavaScript is only used for UI interactions and HTTP requests, not as a backend.

RUN THE PROJECT
1. Install JDK 17 or newer.
2. Install Maven, or open the project in IntelliJ IDEA / VS Code as a Maven project.
3. Open a terminal in the folder containing pom.xml.
4. Run: mvn spring-boot:run
5. Open http://localhost:8080 in your browser.

API ENDPOINTS
GET    /api/employees                 List employees
POST   /api/employees                 Add employee
GET    /api/employees/{id}            Get employee
PUT    /api/employees/{id}            Update employee
DELETE /api/employees/{id}            Delete employee
GET    /api/employees/{id}/salary-slip Get salary breakdown
GET    /api/summary                   Dashboard totals

NOTES
- Salary formulas remain in the Java Employee classes.
- The dashboard charts are illustrative visual summaries; the cards and employee table use live API data.
- Use the Payslip action, then Print / Save PDF in the browser print dialog to save a payslip.
- If port 8080 is occupied, change server.port in src/main/resources/application.properties.
