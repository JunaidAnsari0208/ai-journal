
### Instructions

Click the "Copy" button in the corner of the code block below and paste the entire content into your `README.md` file on GitHub.

````markdown
# 🧠 AI Journal

**A full-stack journaling app that uses Google Gemini to analyze your mood and provide weekly insights.**

<p align="center">
  <img src="https://img.shields.io/badge/Next.js-000000?style=for-the-badge&logo=nextdotjs&logoColor=white" alt="Next.js"/>
  <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="Spring Boot"/>
  <img src="https://img.shields.io/badge/Google_Gemini-8E77F0?style=for-the-badge&logo=google-gemini&logoColor=white" alt="Google Gemini"/>
  <img src="https://img.shields.io/badge/Vercel-black?style=for-the-badge&logo=vercel&logoColor=white" alt="Vercel"/>
</p>

---

## 🚀 Live Demo

**Try the app live: [https://ai-journal-liard.vercel.app](https://ai-journal-liard.vercel.app)**

---

## ✨ Features

-   **✍️ Simple Journaling**: A clean, distraction-free editor for your daily entries.
-   **🤖 AI-Powered Analysis**: Uses **Google Gemini 1.5 Flash** for sentiment analysis on each entry.
-   **📊 Mood History**: Visualizes your mood trends over time on a personal dashboard.
-   **📬 Weekly Email Summaries**: Get AI-generated weekly mood reports sent to your inbox.
-   **🔐 Secure Authentication**: JWT-based user login to keep your journal private.
-   **🌓 Dark Mode**: A modern UI with a comfortable dark mode option.

## 🛠️ Tech Stack

| Category         | Technology                               |
| ---------------- | ---------------------------------------- |
| **Frontend** | Next.js, TypeScript, Tailwind CSS        |
| **Backend** | Spring Boot, Java, REST APIs             |
| **AI Integration** | Google Gemini 2.5 Flash API              |
| **Authentication** | JSON Web Tokens (JWT)                    |
| **Deployment** | Vercel (Frontend), Railway(Backend) |



## ⚙️ Getting Started (Local Setup)

This repository contains two main folders: `/frontend` and `/backend`.

### Prerequisites

-   Node.js v18+
-   Java JDK 17+
-   Maven or Gradle

### 1. Backend Setup

```bash
# Go to the backend directory
cd backend

# Create your application.yml or .properties file for environment variables
# (See the section below for required variables)

# Run the Spring Boot application
mvn spring-boot:run
````

### 2\. Frontend Setup

```bash
# Go to the frontend directory
cd frontend

# Install dependencies
npm install

# Create a .env.local file for environment variables
# (See the section below for required variables)

# Start the development server
npm run dev
```

The app should now be running at `http://localhost:3000`.

## 🔐 Environment Variables

You need to create the following configuration files. **Do not commit these files to Git.**

#### ▶️ Frontend (`/frontend/.env.local`)

```
NEXT_PUBLIC_API_URL=http://localhost:8080
```

#### ▶️ Backend (`/backend/src/main/resources/application.properties`)

```properties
# Server Port
server.port=8080

# Database Connection
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password

# JWT Secret for Authentication
jwt.secret.key=your-very-strong-and-secret-jwt-key

# Google Gemini API Key
gemini.api.key=your-google-gemini-api-key

# Email Configuration (for weekly reports)
spring.mail.host=smtp.example.com
spring.mail.port=587
spring.mail.username=your-email@example.com
spring.mail.password=your-email-app-password
```

```
```
