package com.budgettracker.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DashboardNavigationTest extends BaseTest {

    @Test
    @DisplayName("User can switch between Expenses, Charts, and Budgets sections on the dashboard")
    void switchSections() {
        // Initially, after login, the Expenses section should be displayed
        assertTrue(driver.findElements(By.xpath("//*[contains(text(),'Expense Overview')]"))
                .stream().anyMatch(e -> e.isDisplayed()), "Expense Overview should be visible after login");

        // Click "View Charts" and verify the chart section appears
        driver.findElement(By.xpath("//button[contains(text(),'View Charts')]"))
              .click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'Expense Chart Overview')]")));

        // Click "View Budgets" and verify budget section appears
        driver.findElement(By.xpath("//button[contains(text(),'View Budgets')]"))
              .click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'Monthly Budget Overview')]")));

        // Switch back to Expenses
        driver.findElement(By.xpath("//button[contains(text(),'View Expenses')]"))
              .click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'Expense Overview')]")));
    }
} 