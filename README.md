# Budget Tracker

This repository contains **three independent Maven/Node projects** that together make up the Budget Tracker system:

* `budget-tracker-be` – Spring Boot REST API (Java 17)
* `budget-tracker-fe` – React + Vite single-page application (TypeScript)
* `budget-tracker-automation` – Selenium/JUnit end-to-end tests (Java 17)

---
## 1. Prerequisites

| Tool | Recommended version |
|------|---------------------|
| Java SDK | 17 (LTS) |
| Maven  | 3.9 + (`mvnw` wrapper included) |
| Node.js | ≥ 18 (comes with `npm`) |
| Git | ≥ 2.30 |
| Chrome / Edge / Firefox | Latest (for Selenium tests) |
| Docker | (optional) for MySQL in prod mode |

> 💡 All sub-projects ship with wrappers (`mvnw`, `mvnw.cmd`) so you **do not need** a global Maven install if you prefer using the wrapper.

---
## 2. Repository layout

```
.
├── budget-tracker-be/           # Spring Boot backend (port 8080 by default)
│   └── src/main/java …
├── budget-tracker-fe/           # React frontend (dev server on port 5173)
│   └── src/ …
├── budget-tracker-automation/   # Selenium/JUnit 5 E2E suite
│   └── src/test/java …
└── README.md                    # ← you are here
```

---
## 3. Backend – `budget-tracker-be`

### 3.1 Running locally

```bash
cd budget-tracker-be
./mvnw spring-boot:run   # or mvn spring-boot:run if you have Maven globally
```

* The API will be available at <http://localhost:8080>.
* The app uses **H2 in-memory DB** for tests; for local dev you can either keep H2 or point `spring.datasource.*` properties to your own MySQL instance.

### 3.2 Running the test suite **with coverage**

```bash
./mvnw clean verify
```

* JUnit/Spring tests execute.
* **JaCoCo** generates an HTML & XML coverage report at:
  * `target/site/jacoco/index.html`

Open the HTML file in your browser for a coloured overview.

---
## 4. Frontend – `budget-tracker-fe`

### 4.1 Install dependencies

```bash
cd budget-tracker-fe
npm install   # or pnpm/yarn if you prefer
```

### 4.2 Start the dev server

```bash
npm run dev   # Vite – default port 5173
```

The application will hot-reload on code changes at <http://localhost:5173>.

### 4.3 Run **unit/component tests** with coverage

```bash
npm test -- --coverage      # Jest + React Testing Library
```

* Coverage summary prints to the console.
* Full HTML report is under `coverage/lcov-report/index.html`.

---
## 5. End-to-End Selenium tests – `budget-tracker-automation`

The Selenium tests are **black-box**: they drive a real browser and interact with the application just like a user would.

### 5.1 Which environment do they hit?

* **Default:** The constant `APP_URL` in `BaseTest.java` points to the public demo front-end at
  `https://budget-tracker-fe-lh5k.onrender.com/`.  This means you can simply clone the repo and run the tests without
  launching anything locally.
* **Local development (optional):** When you are working on new features and want to test them _before_ they are deployed,
  start the backend & frontend locally (sections 3 & 4) and update the `APP_URL` constant (or parameterise it) to
  `http://localhost:5173`.

> In other words: **you don't have to run the servers for the tests to work**, but doing so gives you faster feedback on
> your in-progress changes.

### 5.2 Execute the test suite

```bash
cd budget-tracker-automation
./mvnw test      # downloads drivers via WebDriverManager & runs in headless mode
```

* Results are printed to the console.
* Screenshots/videos can be enabled via Selenium listeners if desired.

> ❓ **Headed mode:** Temporarily comment-out the `--headless=new` argument in the test setup to watch the browser interact
> with the app.
