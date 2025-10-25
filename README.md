Reddit-clone (Spring Boot Backend)

A backend for a Reddit-style application with JWT authentication (access + refresh tokens), role-based authorization, and CRUD APIs for posts, comments, votes, and subreddits. Designed as a clean, testable Spring Boot service that pairs with an Angular frontend.

💡 This repository is the backend. The Angular client lives in a separate repo.
👉 Update with link: https://github.com/<your-username>/<angular-repo>

✨ Features

Auth: Sign up, email verification (optional), login, refresh token, logout/ revoke

Security: Spring Security + JWT (access + refresh), password hashing

Posts & Comments: Create, read, update (optional), delete, list by subreddit/ user

Votes: Upvote/Downvote with idempotency

Subreddits: Create & browse communities

User feed: Timeline by subreddit/author (basic)

API Docs: Swagger/OpenAPI UI

Prod-ready basics: Global exception handling, DTO mapping, validation, logging

🏗 Tech Stack

Java 17 👉 Update if different

Spring Boot 3.x, Spring Web, Spring Data JPA, Spring Security, Validation

JWT (Access + Refresh)

Database: PostgreSQL 👉 Update if you used MySQL

Build: Maven

Docs: springdoc-openapi / Swagger UI

Testing: JUnit, Mockito 👉 Update if not yet added

📁 Project Structure
.
├─ src
│  ├─ main
│  │  ├─ java/com/yourorg/redditclone
│  │  │  ├─ config/         # Security, Swagger, CORS
│  │  │  ├─ controller/     # AuthController, PostController, CommentController, ...
│  │  │  ├─ dto/            # Request/Response DTOs
│  │  │  ├─ entity/         # User, Post, Comment, Subreddit, Vote, RefreshToken
│  │  │  ├─ exception/      # Custom exceptions + handlers
│  │  │  ├─ mapper/         # Map entities <-> DTOs
│  │  │  ├─ repository/     # Spring Data JPA repos
│  │  │  ├─ security/       # JWT filters, services, utils
│  │  │  └─ service/        # Business logic
│  │  └─ resources/
│  │     ├─ application.yml
│  │     └─ schema.sql / data.sql (optional)
├─ pom.xml
└─ README.md

⚙️ Configuration

Create src/main/resources/application.yml:

server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/reddit
    username: reddit_user
    password: reddit_pass
  jpa:
    hibernate:
      ddl-auto: update   # 👉 For dev only. Use migrations in prod.
    show-sql: true

jwt:
  issuer: reddit-clone
  access-token-exp-min: 15
  refresh-token-exp-days: 7
  secret: ${JWT_SECRET:change-me}

cors:
  allowed-origins:
    - http://localhost:4200   # Angular dev server


👉 If you used MySQL, replace the datasource URL/driver accordingly.

🐳 Quick Start (Local + Docker)
1) Start Database (PostgreSQL)

Create a docker-compose.yml at project root:

version: "3.8"
services:
  postgres:
    image: postgres:16
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

docker compose up -d

2) Run the Backend
./mvnw spring-boot:run


Backend runs at: http://localhost:8080

Swagger UI: http://localhost:8080/swagger-ui.html
OpenAPI JSON: http://localhost:8080/v3/api-docs

🔐 Authentication Flow (JWT + Refresh)

Register → /api/auth/signup

Login → /api/auth/login → returns accessToken (short-lived) + refreshToken (longer-lived)

Use Authorization: Bearer <accessToken> for protected endpoints

Refresh → /api/auth/refresh with refresh token to get a new access token

Logout → /api/auth/logout (optional revoke refresh token)

🧪 Example Requests (cURL)

Sign Up

curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","email":"alice@example.com","password":"Passw0rd!"}'


Login

curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","password":"Passw0rd!"}'
# => { "accessToken": "...", "refreshToken": "..." }


Create Post

curl -X POST http://localhost:8080/api/posts \
  -H "Authorization: Bearer <ACCESS_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"title":"Hello","content":"First post!","subreddit":"general"}'


Comment on Post

curl -X POST http://localhost:8080/api/comments \
  -H "Authorization: Bearer <ACCESS_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"postId":1,"content":"Nice post!"}'


Vote

curl -X POST http://localhost:8080/api/votes \
  -H "Authorization: Bearer <ACCESS_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"postId":1,"type":"UPVOTE"}'

🧭 API Endpoints (Overview)

POST /api/auth/signup – register

POST /api/auth/login – login (JWT access + refresh)

POST /api/auth/refresh – refresh access token

POST /api/auth/logout – revoke refresh (if implemented)

GET /api/subreddits • POST /api/subreddits

GET /api/posts • GET /api/posts/{id} • POST /api/posts

GET /api/posts/by-subreddit/{name} • GET /api/posts/by-user/{username}

POST /api/comments • GET /api/comments/by-post/{postId}

POST /api/votes (UPVOTE / DOWNVOTE)

👉 Exact paths may differ—adjust to your controllers.

🧰 Development

Run tests

./mvnw test


Build jar

./mvnw clean package


Run jar

java -jar target/reddit-clone-*.jar

🔄 Frontend (Angular)

Angular 20 client with authentication (JWT), posting, commenting, voting

Dev server: http://localhost:4200

Configure CORS in backend (application.yml) to allow http://localhost:4200

📄 License

MIT 👉 Update if you prefer a different license.

🙌 Credits

Built by Shyam Kumar Kurapati
 using Spring Boot and Angular.
