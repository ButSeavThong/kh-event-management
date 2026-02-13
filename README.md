# 🎉 KH Event Management – Backend API

A RESTful Event Management System built with Spring Boot.  
This backend provides authentication, role-based authorization, event management, category management, speaker management, pagination, and filtering features.

---

## 🚀 Tech Stack

- Java 17+
- Spring Boot
- Spring Security (JWT Authentication)
- Spring Data JPA
- Hibernate
- MySQL / PostgreSQL
- Lombok
- Pageable & Filtering
- RESTful API Architecture

---

## 🔐 Authentication & Authorization

The system uses JWT-based authentication with Role-Based Access Control (RBAC).

### Roles

- ROLE_USER
- ROLE_ADMIN

---

## 👤 Public Access (No Login Required)

- View published events
- View event details
- View event speakers
- View categories
- Pagination support
- Filter events by category

---

## 👥 Authenticated User (ROLE_USER)

- Join events
- View joined events
- Access protected endpoints

---

## 👑 Admin Access (ROLE_ADMIN)

- Create events
- Update events
- Delete events
- Change event status (DRAFT / PUBLISHED / CANCELLED)
- Manage categories
- Manage speakers

---

## 📦 Core Features

### 🎟 Event Management

- Create / Update / Delete events
- Set event capacity
- Manage event pricing
- Upload event images
- Pagination support
- Filter by category (ID or Name)
- Only PUBLISHED events visible to public

---

### 🗂 Category Management

- Create category
- Update category
- Delete category
- Public category listing

---

### 🎤 Speaker Management

- Add speakers to event
- Update speaker details
- Delete speaker
- Retrieve speakers by event ID

---

## 🔎 Pagination Example

Endpoint:

GET /api/v1/events?page=0&size=12

Response:

```json
{
  "content": [],
  "totalElements": 25,
  "totalPages": 3,
  "size": 12,
  "number": 0,
  "first": true,
  "last": false
}
🔗 API Structure
🔓 Public Endpoints
GET  /api/v1/events
GET  /api/v1/events/{id}
GET  /api/v1/events/{id}/speakers
GET  /api/v1/categories
POST /api/v1/auth/login
POST /api/v1/auth/register
👤 User Endpoints (ROLE_USER)
POST /api/v1/user/events/{id}/join
GET  /api/v1/user/events
👑 Admin Endpoints (ROLE_ADMIN)
POST   /api/v1/admin/events
PUT    /api/v1/admin/events/{id}
DELETE /api/v1/admin/events/{id}

POST   /api/v1/admin/categories
PUT    /api/v1/admin/categories/{id}
DELETE /api/v1/admin/categories/{id}

POST   /api/v1/admin/events/{eventId}/speakers
DELETE /api/v1/admin/speakers/{id}
🛡 Security
JWT Token authentication

Method-level security using @PreAuthorize

CORS configuration

Separation of:

/api/v1/admin/**

/api/v1/user/**

/api/v1/** (public)

⚙️ Running the Application
1️⃣ Clone Repository
git clone https://github.com/your-username/kh-event-management.git
cd kh-event-management
2️⃣ Configure Database
Update application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/event_db
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
3️⃣ Run Application
Using Maven:

mvn clean install
mvn spring-boot:run
Application will start at:

http://localhost:8080
📁 Project Structure
src/main/java/com/thong/event
 ├── controller
 ├── service
 ├── repository
 ├── security
 ├── dto
 ├── mapper
 ├── entity
 └── utils
🧠 Future Improvements
Payment integration

Email notification system

Event reminder scheduler

Cloud image storage (AWS S3)

Docker deployment

Swagger API documentation

👨‍💻 Author
Thong – Backend Developer

📜 License
This project is for learning and portfolio purposes.
