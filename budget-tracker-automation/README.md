# Budget Tracker Automation

System (black-box) test suite for the Budget Tracker web application found at <https://budget-tracker-fe-lh5k.onrender.com/>.  Tests are written in **Java 17**, powered by **Selenium WebDriver**, **JUnit 5**, and **WebDriverManager**.

## Prerequisites

* Java 17 SDK installed and `$JAVA_HOME` configured.
* Maven 3.8+ on your `PATH`.
* A Chromium-based browser (e.g., Chrome or Edge) or Firefox. WebDriverManager will download the matching driver automatically.

## Folder structure

```
budget-tracker-automation/
 ├── pom.xml               # Maven build file with dependencies
 └── src
     └── test
         └── java
             └── com/budgettracker/tests
                 └── BudgetTrackerTest.java  # First sample test
```

## Running the tests

```bash
cd budget-tracker-automation
mvn test
```

Maven will:
1. Download all dependencies the first time it is run.
2. Use WebDriverManager to fetch the appropriate driver binary.
3. Launch the tests in headless mode (default) and output results to the console.

## Extending the suite

* Put additional helper classes under `src/test/java/com/budgettracker/`.
* Name new test classes `*Test.java` so the Maven Surefire plugin will pick them up automatically.
* Use CSS/XPath selectors that are resilient to style/layout changes.

## Notes

* These are pure black-box tests. They interact **only** through the browser; no internal APIs or source code are referenced.
* The login step accepts any email; the suite currently uses `selenium@example.com`.
* If you prefer headed (non-headless) execution, remove or adjust the `--headless=new` argument in the `BudgetTrackerTest` setup. 