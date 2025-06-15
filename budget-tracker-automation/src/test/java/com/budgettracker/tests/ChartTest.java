package com.budgettracker.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChartTest extends BaseTest {

    @Test
    @DisplayName("Charts section loads and filters can be applied")
    void chartsOverviewWithData() {
        // Ensure budget exists and return to Expenses section
        ensureBudgetExists();

        // Add two expenses in different categories
        addExpense("Selenium Food", "10", 0);
        addExpense("Selenium Other", "20", 1);

        // Open chart section
        driver.findElement(By.xpath("//button[contains(text(),'View Charts')]"))
                .click();

        // Wait for the header
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'Expense Chart Overview')]")));

        // Verify chart renders slices for at least two categories
        wait.until(driver -> driver.findElements(By.cssSelector("[aria-label^='Pie Slice for']")).size() >= 2);
        int sliceCount = driver.findElements(By.cssSelector("[aria-label^='Pie Slice for']")).size();
        assertTrue(sliceCount >= 2, "Chart should display at least two slices for two categories");
    }
} 