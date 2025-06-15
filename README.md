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

These black-box tests spin up a browser, interact with the deployed **frontend** and hit the **backend** behind the scenes.

### 5.1 Before you run them

1. **Start** the backend (`localhost:8080`) and frontend (`localhost:5173`) *in two separate terminals* as shown above.
2. Ensure the site is reachable in your browser.

> The tests assume the default local ports.  If you run on different ports, you can pass JVM system properties: `-Dfrontend.url=http://localhost:3000 -Dbackend.url=http://localhost:9000` (adapt your own custom code accordingly).

### 5.2 Execute the test suite

```bash
cd budget-tracker-automation
./mvnw test        # downloads drivers via WebDriverManager & runs in headless mode
```

* Results appear in the console.
* Screenshots/videos can be added via Selenium listeners if desired.

> ❓ **Headed mode**: Temporarily comment-out the `--headless=new` argument in the test setup to watch the browser.

---
## 6. All-in-one quickstart (macOS/Linux)

```bash
# from repo root
# 1) Backend
(cd budget-tracker-be && ./mvnw spring-boot:run) &
# 2) Frontend
(cd budget-tracker-fe && npm install && npm run dev) &
# give servers a few seconds to start … then
# 3) Selenium tests
(cd budget-tracker-automation && ./mvnw test)
```

Stop the processes with `Ctrl+C` when you are done.

---
## 7. Troubleshooting

| Problem | Fix |
|---------|------|
| `port 8080 already in use` | Stop the existing service or edit `server.port` in `application.properties`. |
| JaCoCo report missing | Make sure `./mvnw clean verify` ran **after** the plugin was configured (see `pom.xml`). |
| Jest cannot find tests | Ensure test files end with `.test.ts(x)` or `.spec.ts(x)` and reside in a `__tests__` folder. |
| Selenium tests fail instantly | Verify the backend & frontend are up and running and that your browser is supported. |

---
### Happy hacking! 🎉
