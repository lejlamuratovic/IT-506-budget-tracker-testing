package com.budgettracker.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.Keys;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
