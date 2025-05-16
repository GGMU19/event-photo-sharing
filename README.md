# 📸 Event Photo Sharing Platform – Spring Boot Backend

A secure backend REST API for uploading and managing photos tied to specific events. Designed for use by event organizers and attendees with role-based access, S3 photo storage, and robust authentication using JWT.

---

## Tech Stack

- **Language:** Java
- **Framework:** Spring Boot
- **Security:** Spring Security + JWT (JSON Web Tokens)
- **Database:** PostgreSQL
- **File Storage:** AWS S3
- **Build Tool:** Maven
- **Testing Tool:** Postman

---

## Features

### Authentication & Authorization

- User registration & login with hashed passwords (BCrypt)
- JWT token-based authentication
- Role-based access: `Organizer`, `Attendee`

### Events

- Organizers can create, update, delete events
- Users can be assigned to events with specific roles

### Photo Upload & Management

- Upload photos to events using AWS S3
- List event photos with pagination
- Organizers can delete their own photos
- Attendees can only upload and view photos for events they’re assigned to

---

## API Testing with Postman

All endpoints were tested using Postman to validate authentication, file uploads, access control, and error handling.

To test locally:

1. Start the Spring Boot application.
2. Register a new user via `/api/auth/register`
3. Login via `/api/auth/login` to receive a JWT token
4. Add the token to the `Authorization: Bearer <token>` header in Postman
5. Use `form-data` in POST requests to upload photos

---

### ✅ Prerequisites

- Java 17+
- Maven
- PostgreSQL
- AWS S3 account + bucket

### 🚀 Setup Steps

1. **Clone the repository**

```bash
git clone https://github.com/your-username/event-photo-backend.git
cd event-photo-backend
```
