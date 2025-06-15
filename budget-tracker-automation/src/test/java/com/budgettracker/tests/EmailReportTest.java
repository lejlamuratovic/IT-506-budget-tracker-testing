package com.budgettracker.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmailReportTest extends BaseTest {

    @Test
    @DisplayName("User can trigger Send Report and see success banner")
    void sendReportSuccess() {
        // Make sure a budget exists so the report endpoint works
        ensureBudgetExists();

        // Navigate to Budgets section (ensure we are on the right tab)
        openBudgetsSection();

        // Click the Send Report button
        driver.findElement(By.xpath("//button[contains(.,'Send Report')]"))
              .click();

        // Wait for success toast/banner
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(),'Report sent successfully')]")));

        assertTrue(driver.findElements(By.xpath("//*[contains(text(),'Report sent successfully')]"))
                .stream().anyMatch(e -> e.isDisplayed()),
                "Success message should appear after sending report");
    }
} 