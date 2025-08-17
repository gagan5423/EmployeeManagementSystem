# Employee Management System (EMS)

## Project Overview

The **Employee Management System (EMS)** is a Spring Boot web application for efficiently managing employee data. It provides **role-based access** to perform actions like viewing, searching, adding, editing, deleting employee records, and generating reports. The application uses **MySQL** as its database.

## Key Features

- Role-based access control (Employee, Manager, Admin)
- View all employees
- Search employees by department or job title
- Add new employees (Manager/Admin)
- Edit employee details (Manager/Admin)
- Delete employees with confirmation (Admin only)
- Generate employee reports (Manager/Admin)
- Audit logging for critical actions
- Custom error pages (403 and general errors)

## Prerequisites

- **Java 17+**
- **Maven**
- **MySQL database**
- **Spring Boot 3.x**

## Setup Instructions

1. **Clone the repository**

```
git clone <repository-url>
cd EmployeeManagementSystem
```

2. **Create the MySQL database**

```
CREATE DATABASE emp_crud;
```

3. **Configure database connection** Update `src/main/resources/application.properties` with your MySQL credentials:

```
spring.datasource.url=jdbc:mysql://localhost:3306/emp_crud
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

4. **Build and run the application**

```
mvn clean install
mvn spring-boot:run
```

5. **Access the application** Open your browser and go to: [http://localhost:8080/](http://localhost:8080/)

## User Roles and Credentials

| Role     | Username | Password    | Permissions                            |
| -------- | -------- | ----------- | -------------------------------------- |
| Employee | employee | employee123 | View employee list, search employees   |
| Manager  | manager  | manager123  | Employee + add, edit, generate reports |
| Admin    | admin    | admin123    | Manager + delete employees             |

## Usage

- **Home Page:** Lists all employees (`/`)
- **Add Employee:** `/loadEmpSave` (Manager/Admin)
- **Edit Employee:** `/EditEmp/{id}` (Manager/Admin)
- **Delete Employee:** `/confirmDelete/{id}` (Admin only)
- **Search Employees:** `/search/department` or `/search/job`
- **Generate Report:** `/report` (Manager/Admin)

## Logging

- **Audit logs:** `logs/audit.log` (records critical actions)
- **Application logs:** `logs/EmployeeManagementSystem.log`

## Error Handling

- **403 Forbidden:** Displayed for unauthorized access attempts
- **Generic error page:** Handles other application errors

## Notes

- Ensure the **MySQL server is running** before starting the application.
- **Spring Security** handles authentication and role-based access control.


