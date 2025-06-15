package com.budgettracker.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected static final String APP_URL = "https://budget-tracker-fe-lh5k.onrender.com/";

    @BeforeAll
    void initDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--headless=new", "--window-size=1920,1080");
        options.addArguments("--window-size=1920,1080");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(25));
    }

    @AfterAll
    void quitDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

    // Navigate to the login page, enter any email, and wait until the dashboard loads
    // Runs before each test to ensure we start from a clean dashboard state
    @BeforeEach
    void login() {
        driver.get(APP_URL);
        WebElement emailInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type='email']")));
        String randomEmail = "selenium+" + timestamp() + "@example.com";
        emailInput.clear();
        emailInput.sendKeys(randomEmail);
        driver.findElement(By.xpath("//button[contains(text(),'Log In')]"))
              .click();

        // Wait for dashboard welcome banner
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'Welcome')]")));
    }

    // Utility to produce a unique timestamp for titles/emails
    protected String timestamp() {
        return DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
    }

    // Navigation helpers
    protected void openBudgetsSection() {
        driver.findElement(By.xpath("//button[contains(text(),'View Budgets')]"))
              .click();
    }

    protected void openExpensesSection() {
        driver.findElement(By.xpath("//button[contains(text(),'View Expenses')]"))
              .click();
    }

    // Ensure a budget exists for the current month/year (creates one if it doesn't)
    protected void ensureBudgetExists(int amount) {
        openBudgetsSection();
        try {
            WebElement addBudgetBtn = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(text(),'Add Budget')]")));
            addBudgetBtn.click();

            WebElement amountField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input[aria-label='New budget amount']")));
            amountField.clear();
            amountField.sendKeys(String.valueOf(amount));

            driver.findElement(By.cssSelector("button[aria-label='Submit budget']"))
                  .click();

            wait.until(ExpectedConditions.invisibilityOf(amountField));
        } catch (org.openqa.selenium.TimeoutException ignored) {
            // Budget already exists, nothing to do
        }
        openExpensesSection();
    }

    protected void ensureBudgetExists() {
        ensureBudgetExists(5000);
    }

    // Adds an expense via the UI
    protected String addExpense(String baseTitle, String amount, int categoryIndex) {
        driver.findElement(By.cssSelector("[aria-label='add expense']"))
              .click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h2[contains(text(),'Add New Expense')]")));

        String title = baseTitle + " " + timestamp();
        driver.findElement(By.cssSelector("input[name='title']"))
              .sendKeys(title);

        WebElement amountInput = driver.findElement(By.cssSelector("input[name='amount']"));
        amountInput.clear();
        amountInput.sendKeys(amount);

        java.time.LocalDate today = java.time.LocalDate.now();
        String dateDigits = today.format(java.time.format.DateTimeFormatter.ofPattern("ddMMyyyy"));
        WebElement dateInput = driver.findElement(By.cssSelector("div[role='dialog'] input[name='date']"));
        dateInput.sendKeys(dateDigits);

        if (categoryIndex > 0) {
            try {
                WebElement selectToggle = driver.findElement(By.cssSelector("div[role='dialog'] div[role='button'][aria-haspopup='listbox']"));
                selectToggle.click();
                WebElement listbox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul[role='listbox']")));
                java.util.List<WebElement> items = listbox.findElements(By.cssSelector("li"));
                if (items.size() > categoryIndex) {
                    items.get(categoryIndex).click();
                } else {
                    selectToggle.sendKeys(org.openqa.selenium.Keys.ESCAPE);
                }
            } catch (org.openqa.selenium.NoSuchElementException ignored) {
                // Keep default category
            }
        }

        driver.findElement(By.xpath("//button[contains(text(),'Add Expense')]"))
              .click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(),'" + title + "')]")));

        return title;
    }

    // Add two expenses and confirm they really appeared in the list before we leave Expenses
    protected void addTwoExpenses() {
        String t1 = addExpense("BudgetTest", "100", 0);
        String t2 = addExpense("BudgetTest", "50",  0);

        assertTrue(driver.findElement(By.xpath("//*[contains(text(),'" + t1 + "')]")).isDisplayed());
        assertTrue(driver.findElement(By.xpath("//*[contains(text(),'" + t2 + "')]")).isDisplayed());
    }
} 