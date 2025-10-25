# Reddit-clone (Spring Boot Backend)

A backend for a Reddit-style web application built with **Spring Boot**, featuring JWT authentication (access and refresh tokens), role-based authorization, and CRUD operations for posts, comments, votes, and subreddits.  
Designed as a clean, modular, and testable REST API that pairs with an Angular frontend.

> This repository hosts the backend service.  
> The Angular frontend lives in a separate repository: [Reddit Clone Frontend](https://github.com/Shyam3225/Reddit-clone-frontend)

---

## 🚀 Features

- **Authentication & Authorization**
  - Sign up, login, refresh token, and logout functionality
  - Spring Security with JWT (access + refresh)
  - Password encryption with BCrypt

- **Posts & Comments**
  - Create, read, update (optional), delete posts and comments
  - List posts by subreddit or by user

- **Votes**
  - Upvote/Downvote posts with idempotency to prevent duplicate actions

- **Subreddits**
  - Create and browse multiple subreddits

- **User Feed**
  - Timeline and activity feed by subreddit or followed users

- **API Documentation**
  - Swagger UI and OpenAPI integration for easy API testing

---

## 🛠 Tech Stack

- **Backend:** Java 17, Spring Boot, Spring Security, Spring Data JPA  
- **Database:** PostgreSQL *(can be configured to MySQL)*  
- **Authentication:** JWT (access + refresh tokens)  
- **Build Tool:** Maven  
- **Frontend:** Angular 20 *(separate repo)*  
- **Testing:** JUnit, Mockito  
- **Documentation:** Swagger / OpenAPI  

---

## ⚙️ Project Structure

## 📁 Project Structure

```bash
Reddit-clone/
│
├── src/
│   ├── main/java/com/example/redditclone/
│   │   ├── config/          # Security, Swagger, CORS configs
│   │   ├── controller/      # Auth, Post, Comment, Subreddit, Vote Controllers
│   │   ├── dto/             # Request and Response DTOs
│   │   ├── entity/          # JPA Entities (User, Post, Comment, etc.)
│   │   ├── exception/       # Custom exception handling
│   │   ├── repository/      # JPA Repositories
│   │   ├── security/        # JWT filters, providers, utils
│   │   └── service/         # Business logic services
│   └── main/resources/
│       ├── application.yml  # Configuration
│       └── schema.sql       # (Optional) DB schema
│
├── pom.xml
└── README.md


## ⚙️ Configuration

Update your database credentials and JWT settings in `application.yml`:

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/reddit
    username: reddit_user
    password: reddit_pass
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

jwt:
  secret: your-secret-key
  access-token-expiration: 15m
  refresh-token-expiration: 7d
🐳 Running the Application
1️⃣ Start PostgreSQL (Docker)
Create a docker-compose.yml:

yaml
Copy code
version: "3.8"
services:
  postgres:
    image: postgres:16
    container_name: reddit_db
    environment:
      POSTGRES_DB: reddit
      POSTGRES_USER: reddit_user
      POSTGRES_PASSWORD: reddit_pass
    ports:
      - "5432:5432"
    volumes:
      - reddit_data:/var/lib/postgresql/data

volumes:
  reddit_data:
Start the container:

bash
Copy code
docker compose up -d
2️⃣ Run the Backend
bash
Copy code
./mvnw spring-boot:run
3️⃣ Access API Docs
Swagger UI: http://localhost:8080/swagger-ui.html

🔐 Authentication Flow
Register: /api/auth/signup

Login: /api/auth/login → returns accessToken + refreshToken

Access protected endpoints: use Authorization: Bearer <accessToken>

Refresh token: /api/auth/refresh

Logout: /api/auth/logout

📡 Key API Endpoints
Endpoint	Method	Description
/api/auth/signup	POST	Register a new user
/api/auth/login	POST	Login with username & password
/api/posts	GET/POST	Get or create posts
/api/comments	GET/POST	Manage comments
/api/votes	POST	Upvote or downvote a post
/api/subreddits	GET/POST	Manage subreddits

🧪 Testing
Run all tests:

bash
Copy code
./mvnw test
🌐 Frontend Integration
Frontend built with Angular 20

Development server: http://localhost:4200

CORS enabled for http://localhost:4200 in backend

Angular repo link: Reddit Clone Frontend

🚀 Future Enhancements
Image upload support

Notification service

Search & filtering

Caching & pagination

Dockerized full-stack deployment

👨‍💻 Author
Shyam Kumar Kurapati
GitHub • LinkedIn
