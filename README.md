# SmartStock AI – AI-Powered Inventory Management System

> A production-ready full-stack inventory management platform built with Spring Boot, Angular, PostgreSQL, Docker, and AI-powered inventory insights using Groq LLM.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0-green)
![Angular](https://img.shields.io/badge/Angular-20-red)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-18-blue)
![Docker](https://img.shields.io/badge/Docker-Containerized-2496ED)
![Render](https://img.shields.io/badge/Backend-Render-purple)
![Vercel](https://img.shields.io/badge/Frontend-Vercel-black)
![Groq](https://img.shields.io/badge/AI-Groq-blueviolet)

---

# 🚀 Live Demo

### Frontend

https://smartstock-ai-349w.vercel.app

### Backend API

https://smartstock-ai-3.onrender.com

### Swagger Documentation

https://smartstock-ai-3.onrender.com/swagger-ui.html

---

# 📖 Overview

SmartStock AI is a cloud-native inventory management platform designed for businesses to efficiently manage products, suppliers, stock levels, and inventory transactions.

Unlike traditional inventory systems, SmartStock AI integrates an AI assistant powered by Groq's GPT-OSS-20B model to generate intelligent inventory insights, dashboard summaries, and answer natural language questions using live inventory data.

The application follows a modern three-tier architecture consisting of an Angular frontend, Spring Boot REST API backend, PostgreSQL database, JWT-based authentication, Docker containerization, and cloud deployment.

---

# ✨ Features

## Authentication

- Secure JWT Authentication
- BCrypt Password Encryption
- Spring Security
- Role-Based Authorization
- Protected REST APIs

---

## Inventory Management

- Product Management
- Category Management
- Supplier Management
- Inventory Tracking
- Stock In / Stock Out
- Inventory Value Calculation
- Low Stock Monitoring
- Out-of-Stock Detection

---

## Transaction Management

Every inventory movement is recorded with:

- Transaction Type
- Quantity
- Previous Stock
- Updated Stock
- Timestamp

This creates a complete audit trail for inventory operations.

---

## AI Features

Powered by Groq GPT-OSS-20B

### AI Dashboard Summary

Generates:

- Overall inventory health
- Inventory risk analysis
- Executive summary
- Business recommendations

---

### AI Inventory Insights

Analyzes

- Low stock products
- Out-of-stock products
- Inventory value
- Stock movement

Provides intelligent recommendations for inventory optimization.

---

### AI Assistant

Ask natural language questions like:

- Which products require immediate restocking?
- Which products are out of stock?
- Summarize current inventory health.
- What are today's inventory risks?

---

# 🛠 Tech Stack

## Frontend

- Angular 20
- TypeScript
- HTML5
- CSS3
- Angular Router
- Angular HttpClient

---

## Backend

- Java 17
- Spring Boot 4
- Spring Security
- Spring Data JPA
- Hibernate
- REST API
- JWT Authentication
- Swagger/OpenAPI

---

## Database

- PostgreSQL 18
- Neon Cloud PostgreSQL

---

## AI

- Groq API
- GPT-OSS-20B

---

## Deployment

- Docker
- Render
- Vercel

---

# 🏗 Architecture

```
                    Angular Frontend
                           │
                           │ REST API
                           ▼
                 Spring Boot Backend
                           │
         ┌─────────────────┼─────────────────┐
         │                 │                 │
         ▼                 ▼                 ▼
 PostgreSQL          JWT Security       Groq LLM API
 (Neon Cloud)                           (AI Insights)
```

---

# 📂 Project Structure

```
smartstock-ai/

├── backend/
│   └── backend/
│       ├── src/
│       ├── Dockerfile
│       └── pom.xml
│
├── frontend/
│   └── smartstock-frontend/
│       ├── src/
│       ├── Dockerfile
│       └── angular.json
│
├── database/
├── docs/
├── screenshots/
└── README.md
```

---

# 🗄 Database Schema

```
User
│
├── id
├── name
├── email
├── password
└── role

Category
│
├── id
└── name

Supplier
│
├── id
├── name
├── email
└── phone

Product
│
├── id
├── name
├── sku
├── price
├── category
└── supplier

Inventory
│
├── product
├── stockLevel
└── minimumStock

StockTransaction
│
├── product
├── STOCK_IN
├── STOCK_OUT
├── quantity
└── timestamp
```

---

# 🤖 AI Workflow

```
Inventory Data
       │
       ▼

Spring Boot AI Service

       │

Creates Prompt

       │

Calls Groq API

       │

GPT-OSS-20B

       │

Returns AI Analysis

       │

Displays Dashboard Insights
```

---

# 🌐 REST API

## Authentication

```
POST /api/auth/register
POST /api/auth/login
```

## Categories

```
GET
POST
PUT
DELETE
```

## Products

```
GET
POST
PUT
DELETE
```

## Suppliers

```
GET
POST
PUT
DELETE
```

## Inventory

```
GET
POST
PUT
DELETE
```

## Transactions

```
GET
POST
```

## AI

```
GET /api/ai/dashboard
GET /api/ai/insights
POST /api/ai/chat
```

---

# ☁ Deployment

### Backend

- Docker
- Render

### Frontend

- Vercel

### Database

- Neon PostgreSQL

### AI

- Groq Cloud API

---

# 🔒 Security

- JWT Authentication
- BCrypt Password Encryption
- Spring Security
- Environment Variables
- CORS Configuration
- Protected REST APIs

---

# ⚡ Project Workflow

1. User logs into the application using JWT authentication.
2. Products, suppliers, categories, and inventory are managed through secure REST APIs.
3. Every stock movement creates a transaction history.
4. Dashboard aggregates inventory statistics in real time.
5. AI service collects inventory data from PostgreSQL.
6. Spring Boot constructs a structured prompt.
7. Prompt is sent to the Groq LLM API.
8. AI generates business insights and recommendations.
9. Results are displayed instantly on the Angular dashboard.

---

# 📈 Performance Highlights

- Secure JWT authentication with stateless REST APIs.
- Cloud-hosted PostgreSQL database using Neon.
- AI-powered inventory analysis using Groq GPT-OSS-20B.
- Dockerized backend for consistent deployments.
- Fully deployed on Render and Vercel.
- Responsive Angular frontend with RESTful integration.

---

# 🚧 Challenges Solved

- Replaced local Ollama inference with a hosted Groq LLM API while preserving the AI service interface.
- Implemented secure JWT authentication with BCrypt password hashing.
- Integrated PostgreSQL with Neon Cloud for production deployment.
- Resolved Docker deployment issues for Spring Boot and Angular.
- Configured CORS between Vercel and Render.
- Secured API keys and credentials using environment variables.
- Containerized the application for cloud-native deployment.

---

# 🚀 Future Enhancements

- Inventory demand forecasting using historical trends
- Barcode & QR code scanning
- Email notifications for low stock
- Multi-warehouse inventory management
- AI-powered sales forecasting
- PDF & Excel report generation
- WebSocket-based live inventory updates

---

# 📄 Resume Highlights

- Developed a full-stack AI-powered inventory management platform using Spring Boot 4, Angular 20, PostgreSQL, Docker, and JWT authentication, deployed on Render, Vercel, and Neon Cloud.
- Designed and implemented 20+ secure REST APIs supporting complete inventory, supplier, product, and transaction management with Spring Security and BCrypt encryption.
- Integrated Groq GPT-OSS-20B to generate AI-powered inventory insights, dashboard summaries, and natural language inventory assistance using live business data.

---

# 👨‍💻 Author

**Sannidhya Mundra**

If you found this project useful, consider giving the repository a ⭐.
