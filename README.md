# Movie Reservation Service Backend

A robust, enterprise-grade backend system for a Movie Reservation Service built using **Spring Boot**, **Spring Data JPA**, **MySQL**, and **Spring Security**. This system features role-based access control (RBAC), secure stateless JWT authentication, transactional seat reservation logic to prevent overbooking, dynamic reporting, and multipart file upload processing for movie posters.

---

## 🚀 Key Features

* **Stateless JWT Authentication:** Secure registration and login flow issuing cryptographically signed JSON Web Tokens (HMAC-SHA256).
* **Role-Based Access Control (RBAC):** * `USER`: Can browse movies, check showtimes, view real-time seat availability, and book/cancel reservations.
    * `ADMIN`: Full CRUD privileges over movies/showtimes, access to system capacity, and revenue performance reports.
* **Concurrency Guard (Anti-Overbooking):** Utilizes **Optimistic Locking (`@Version`)** and Database Transactions (`@Transactional`) to guarantee data integrity during high-traffic booking windows.
* **Multipart File Uploads:** Supports direct form-data media streams to save movie posters securely on the local server file system while preserving lightweight tracking paths in MySQL.
* **Global Exception Framework:** Centralized exception handling that transforms raw internal stack traces into uniform, user-friendly JSON error schemas.

---

## 🛠️ Technology Stack

* **Framework:** Spring Boot 3.x (Web, Security, Data JPA)
* **Database:** MySQL 8.x
* **Security & Identity:** Spring Security, JWT (Java JWT - JJWT)
* **Build Automation:** Maven
* **Utility Tools:** Lombok

---

## 📂 Project Architecture & Directory Structure

The system follows a clean, decoupled layer architecture:

```text
src/main/java/com/movie/reservation/
├── ReservationApplication.java      # Application bootstrap entry point
├── config/                          # Security configuration and JWT filters
├── controller/                      # REST API Endpoint layer (Handles HTTP traffic)
├── dto/                             # Data Transfer Objects for decoupled payloads
├── entity/                          # JPA Core Entities mapped to MySQL schemas
├── exception/                       # Custom exceptions and Global Interceptor
├── repository/                      # Database communication abstraction (Spring Data JPA)
└── service/                         # Transactional business logic validation layer