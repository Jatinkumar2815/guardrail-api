# Guardrail API

This is a Spring Boot microservice that implements Redis-based guardrails for concurrency control and system protection.

##  Tech Stack

- Java 17
- Spring Boot
- MySQL
- Redis

##  Features

- Create Posts
- Add Comments
- Like Posts
- Redis Guardrails (Bot limit, Cooldown, Depth)
- Virality Score

##  API Endpoints

### Create Post
POST /api/posts

### Add Comment
POST /api/posts/{postId}/comments

### Like Post
POST /api/posts/{postId}/like

##  Redis Guardrails

### 1. Horizontal Cap (Bot Limit)

- Max 100 bot replies per post
- Redis Key: post:{id}:bot_count
- Uses Redis INCR (atomic)

### 2. Cooldown Cap

- Bot cannot interact with same user within 10 minutes
- Redis Key: cooldown:bot_{botId}:human_{humanId}
- Uses SETNX with TTL

### 3. Vertical Cap

- Max comment depth = 20
- Checked in service layer

##  Virality Score

- Bot Reply = +1
- Human Like = +20
- Human Comment = +50

Stored in Redis:
post:{id}:virality_score

##  Thread Safety

Redis ensures thread safety using atomic operations:

- INCR → safe counter increment
- SETNX → prevents duplicate actions

Even with concurrent requests, data remains consistent.

##  Stateless Architecture

The application is fully stateless.

All runtime data is stored in Redis:
- bot count
- cooldown
- virality score

No in-memory storage is used.

## How to Run

1. Start Redis
2. Start MySQL
3. Run project:

mvn spring-boot:run

Application runs at:
http://localhost:8080

##  Postman Collection

Included in repository as:
postman_collection.json
