# url-shortner-java-springboot-postgres
## Status: WIP

### Core Tech Stack
- [x] Backend: Java, Spring Boot, Spring Data JPA
- [ ] Frontend: React.js (for dashboard + creation UI)
- [x] Database: PostgreSQL 
- [ ] Caching: Redis/ Inmemory caching
- [ ] Security: JWT for auth, rate limiting
- [ ] DevOps: Docker, CI/CD (GitHub Actions), deployment on Render/AWS

### Functional Requirements
- [x] Shorten and expand URLs (core functionality)
- [ ] Percent Encoding (Optional)
- [ ] Custom aliases (yourdomain.com/p/myslug)
- [x] URL expiry (TTL for each link)
- [ ] Rate limiting for anonymous users (prevent abuse)
- [ ] Analytics dashboard (optional)
- [x] Total clicks
- [ ] Geo/IP location (use IP-to-location APIs)
- [ ] Browser/OS/device breakdown (optional with User-Agent parsing) (optional)
- [ ] User authentication (optional)

### Non-Functional Requirements
- [ ] High availability and scalability
- [ ] Low latency
- [ ] Security (HTTPS, JWT, etc.)
- [ ] Monitoring and logging
- [ ] Documentation (API docs, README)
- [ ] Testing (unit tests, integration tests)
- [ ] CI/CD pipeline

### User Stories
1. As a user, I want to shorten a long URL so that I can share it easily.
2. As a user, I want to expand a shortened URL to see the original link.
3. As a user, I want to create custom aliases for my shortened URLs.
4. As a user, I want to set an expiry date for my shortened URLs.
5. As a user, I want to see analytics for my shortened URLs, including total clicks and geographic location of users.
6. As a user, I want to be able to register and log in to manage my shortened URLs.
7. As a user, I want to be able to rate limit my URL shortening requests to prevent abuse.
8. As a user, I want to be able to view the history of my shortened URLs and their analytics.
9. As a user, I want to be able to delete my shortened URLs.
10. As a user, I want to be able to view the most popular shortened URLs on the platform.
11. As a user, I want to be able to report spam or malicious URLs.
12. As a user, I want to be able to view the most popular shortened URLs on the platform.

### Low-Level Design
- **URL Shortening Algorithm**: Base62 encoding or hash-based (MD5/SHA)
- **Database Schema**:
  - `urls` table: id, original_url, short_url, created_at, updated_at, user_id (optional)
  - `users` table: id, username, password_hash, created_at, updated_at
  - `clicks` table: id, url_id, user_id (optional), timestamp, user_agent (optional), ip_address (optional)

### Project Structure
As the project is progressed, you'd see the structure below evident. 

```
url-shortner-java-springboot-postgres
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── prashanth
│   │   │           └── urlshortner
│   │   │               ├── UrlShortnerApplication.java
│   │   │               ├── config
│   │   │               │   ├── AppConfig.java
│   │   │               │   └── SecurityConfig.java
│   │   │               ├── controller
│   │   │               │   ├── UrlController.java
│   │   │               │   └── UserController.java
│   │   │               ├── dto
│   │   │               │   ├── UrlRequestDto.java
│   │   │               │   └── UrlResponseDto.java
│   │   │               ├── entity
│   │   │               │   ├── Url.java
│   │   │               │   └── User.java
│   │   │               ├── exception
│   │   │               │   ├── GlobalExceptionHandler.java
│   │   │               │   ├── ResourceNotFoundException.java
│   │   │               │   └── UrlAlreadyExistsException.java
│   │   │               ├── repository
│   │   │               │   ├── UrlRepository.java
│   │   │               │   └── UserRepository.java
│   │   │               ├── service
│   │   │               │   ├── UrlService.java
│   │   │               │   └── UserService.java
│   │   │               └── util
│   │   │                   ├── UrlShortenerUtil.java
│   │   │                   └── JwtUtil.java
│   │   └── resources
│   │       ├── application.properties
│   │       └── static
│   │           └── index.html
│   └── test
│       └── java
│           └── com
│               └── prashanth
│                   └── urlshortner
│                       ├── UrlShortnerApplicationTests.java
│                       ├── controller
│                       │   ├── UrlControllerTest.java
│                       │   └── UserControllerTest.java
│                       ├── service
│                       │   ├── UrlServiceTest.java
│                       │   └── UserServiceTest.java
│                       └── repository
│                           ├── UrlRepositoryTest.java
│                           └── UserRepositoryTest.java
└── Dockerfile
```

### Core Logic
The section describes, how the encoding is implemented. 

- Input : url + custom slug which unique across the database(optional) 
- A unique short code needs to be generated
#### Approach 1: Generate short code using Base62(ID)
Base62 of auto-increment DB ID
Predictable
Simple, Fast

#### Approach 2: Generate Random Base62 string of n characters 
Using Base62, 62^n unique codes can be generated, where `n is the number of characters the short code would contain` 
- When a url is passed, a `n character length` code is generated and persisted in the database. 
- For instance, if n=6, 62^6 = 56800235584 = 56 billion unique short codes can be generated. 
Collision Handling at scale

Not Predictable
Hard to guess
Need collision handling

#### Approach 3: Hash(URL) + Salt

Not Predictable
Unique+secure
Slower
