package com.budgettracker.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExpenseTest extends BaseTest {

    @Test
    @DisplayName("User can add and delete an expense from the dashboard")
    void addAndDeleteExpense() {
        String expenseTitle = "Selenium Expense " + timestamp();
        String expenseAmount = "12.34";
        java.time.LocalDate todayDate = java.time.LocalDate.now();
        String isoDate = todayDate.toString();
        String localDateDigits = todayDate.format(java.time.format.DateTimeFormatter.ofPattern("ddMMyyyy"));

        // Ensure budget exists
        ensureBudgetExists();

        // Add expense
        String fullTitle = addExpense("Selenium Expense", expenseAmount, 0);

        // Verify row visible   
        assertTrue(driver.findElements(By.xpath("//*[contains(text(),'" + fullTitle + "')]"))
                .stream().anyMatch(WebElement::isDisplayed), "Newly added expense should be visible");

        // Delete expense
        WebElement deleteButton = driver.findElement(
                By.xpath("//li[.//*[contains(text(),'" + fullTitle + "')]]//button[@aria-label='delete']"));
        deleteButton.click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//*[contains(text(),'" + fullTitle + "')]")));
    }


    @Test
    @DisplayName("Date filters in Expense Overview show only matching rows")
    void expenseDateFiltering() {
        ensureBudgetExists();

        // Add three expenses on different dates
        LocalDate today     = LocalDate.now();
        LocalDate twoDays   = today.minusDays(2);
        LocalDate sevenDays = today.minusDays(7);

        String tToday   = addExpenseWithDate("FilterTest", "10", today);
        String t2Days   = addExpenseWithDate("FilterTest", "20", twoDays);
        String t7Days   = addExpenseWithDate("FilterTest", "30", sevenDays);

        // Verify they all appear before filtering
        assertTrue(isRowDisplayed(tToday));
        assertTrue(isRowDisplayed(t2Days));
        assertTrue(isRowDisplayed(t7Days));

        // Apply filter: last 3 days
        WebElement startInput = driver.findElement(By.cssSelector("input[aria-label='filter by start date']"));
        WebElement endInput   = driver.findElement(By.cssSelector("input[aria-label='filter by end date']"));

        startInput.clear();
        startInput.sendKeys(twoDays.format(DateTimeFormatter.ISO_DATE));
        endInput.clear();
        endInput.sendKeys(today.format(DateTimeFormatter.ISO_DATE));

        driver.findElement(By.cssSelector("button[aria-label='apply filters']"))
              .click();

        // Wait until list updates (row for old expense disappears)
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[contains(text(),'" + t7Days + "')]")));

        // Assertions after filter
        assertTrue(isRowDisplayed(tToday));
        assertTrue(isRowDisplayed(t2Days));
        assertFalse(isRowPresent(t7Days));
    }

    // Helper methods
    private String addExpenseWithDate(String baseTitle, String amount, LocalDate date) {
        driver.findElement(By.cssSelector("[aria-label='add expense']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h2[contains(text(),'Add New Expense')]")));

        String title = baseTitle + " " + timestamp();
        driver.findElement(By.cssSelector("input[name='title']")).sendKeys(title);
        WebElement amountInput = driver.findElement(By.cssSelector("input[name='amount']"));
        amountInput.clear();
        amountInput.sendKeys(amount);
        // Date input
        String digits = date.format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        WebElement dateInput = driver.findElement(By.cssSelector("div[role='dialog'] input[name='date']"));
        dateInput.sendKeys(digits);

        driver.findElement(By.xpath("//button[contains(text(),'Add Expense')]"))
              .click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'" + title + "')]")));
        return title;
    }

    private boolean isRowDisplayed(String text) {
        return driver.findElements(By.xpath("//*[contains(text(),'" + text + "')]"))
                .stream().anyMatch(WebElement::isDisplayed);
    }

    private boolean isRowPresent(String text) {
        return !driver.findElements(By.xpath("//*[contains(text(),'" + text + "')]"))
                .isEmpty();
    }

    protected double extractDollar(String txt) {
        return Double.parseDouble(txt.replace("$", "").trim());
    }
} 