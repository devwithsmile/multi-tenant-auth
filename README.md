# 🔐 Auth Microservice

Welcome to the **Auth Microservice**! This service handles authentication, authorization, and user management in a **secure, scalable, and multi-tenant** environment. 🚀

---

## 📌 Features
- **Multi-Tenancy** 🏢: Each organization has isolated authentication.
- **Role-Based Access Control (RBAC) 🔑**: Fine-grained permissions per user.
- **JWT Authentication** 🔒: Secure access with refresh token support.
- **Redis Caching** ⚡: Speed up authentication & permission checks.
- **Kafka Event-Driven Architecture** 📡: Sync permissions across microservices.
- **API Rate Limiting** 🛑: Prevent abuse with Redis-based rate limiting.
- **OpenAPI Documentation** 📜: Self-documented, easy-to-use APIs.

---

## 📦 Tech Stack
| Component     | Technology  |
|--------------|------------|
| **Backend**  | Java + Spring Boot ☕ |
| **Database** | PostgreSQL 🐘 |
| **Cache**    | Redis 🚀 |
| **Message Broker** | Kafka 📡 |
| **Security** | JWT + BCrypt 🔐 |
| **API Docs** | OpenAPI / Swagger 📜 |
| **Monitoring** | Spring Boot Actuator + Micrometer 📊 |

---

## ⚙️ Installation & Setup

### 1️⃣ Clone the Repository
```sh
git clone https://github.com/yourusername/auth-microservice.git
cd auth-microservice
```

### 2️⃣ Setup Environment Variables
Create a `.env` file and configure:
```env
DB_URL=jdbc:postgresql://localhost:5432/auth_db
REDIS_HOST=localhost
KAFKA_BROKER=localhost:9092
JWT_SECRET=your_super_secret_key
```

### 3️⃣ Run the Application
#### Using Maven
```sh
./mvnw spring-boot:run
```
#### Using Docker
```sh
docker-compose up --build
```

---

## 🏗️ Architecture Overview
1️⃣ **User logs in** → Receives JWT token 🔑
2️⃣ **Frontend sends token** in requests → Backend verifies 🔍
3️⃣ **RBAC check happens** → Redis caches permissions ⚡
4️⃣ **Event-driven updates** → Kafka syncs permission changes 📡

📌 **Security Measures**:
✅ **HTTPS enforced**  
✅ **BCrypt password hashing** 🔒  
✅ **Rate limiting to prevent abuse** 🚦


## 📢 Support & Contact
For issues and suggestions, open a GitHub Issue or reach out via email: **devsaini7970@gmail.com** 💬

Give a ⭐ if you like this project! 😃

