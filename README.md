# FitTrack

Workout History Web is a full-stack microservices application that allows users to register, log in, and track their workout history. It consists of multiple backend services built with **Java Spring Boot**, a **PostgreSQL** database, and an **Angular** frontend, all containerized using **Docker Compose**.

---

## 📦 Project Structure


---

## 🧩 Tech Stack

- **Backend:** Java Spring Boot (Auth, User, Workout services)
- **Frontend:** Angular
- **Database:** PostgreSQL 15
- **Authentication:** JWT
- **Containerization:** Docker & Docker Compose
- **Communication:** RESTful APIs

---

## ✅ Prerequisites

Make sure you have the following installed:

- [Docker](https://www.docker.com/products/docker-desktop)
- [Docker Compose](https://docs.docker.com/compose/install/)
- [Node.js](https://nodejs.org/) (v16 or newer) – required to run the Angular frontend
- [npm](https://www.npmjs.com/get-npm) – comes with Node.js, used to install frontend dependencies
- [Angular CLI](https://angular.io/cli) – to run the Angular development server

---

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/workout-history-web.git
cd workout-history-web
```

### 2. Build and run the backend
```bash
docker-compose up --build
```

This command builds all services and runs:

- 🐘 **PostgreSQL database**
- 🔐 **AuthService** (Spring Boot)
- 👤 **UserService** (Spring Boot)
- 💪 **WorkoutService** (Spring Boot)

| **Service**       | **Description**         | **Port** |
|-------------------|-------------------------|----------|
| AuthService       | Handles login/register  | 8081     |
| UserService       | Manages user info       | 8082     |
| WorkoutService    | Manages workouts        | 8080     |
| Frontend          | Angular Web UI          | 4200     |
| PostgreSQL        | Database                | 5432     |


### 3. Build and run the Front-end
```bash
cd frontend
npm install
ng serve -o
```

Once running, the browser will automatically open to:

➡️ [http://localhost:4200](http://localhost:4200)