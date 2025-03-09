# 🚀 Spring Boot User Registration API

This is a simple **User Registration API** built using **Spring Boot 3.4.3**, **Java 21**, and **MySQL**. It allows users to register with their credentials securely.

## 📌 Features
✅ User Registration (`POST /api/register`)  
✅ Password Encryption (BCrypt)  
✅ HikariCP Connection Pooling  
✅ Spring Security Integration  
✅ MySQL Database Support  

---

## ⚙️ **Technologies Used**
- Java 21
- Spring Boot 3.4.3
- Spring Data JPA
- Spring Security
- Hibernate ORM
- MySQL
- HikariCP Connection Pool
- Postman (for testing)

---

## 📌 **API Documentation**
### 📝 **User Registration**
- **URL:** `POST /api/register`
- **Headers:** `Content-Type: application/json`
- **Request Body:**
  ```json
  {
    "username": "john_doe",
    "email": "john@example.com",
    "password": "securepassword"
  }
