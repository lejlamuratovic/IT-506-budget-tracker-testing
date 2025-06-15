package com.budgettracker.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BudgetTest extends BaseTest {

    @Test
    @DisplayName("Remaining budget decreases by expense amount")
    void remainingBudgetUpdatesAfterExpense() {
        // Ensure we have a budget for the current month
        ensureBudgetExists();

        // Read remaining budget BEFORE adding expense
        openBudgetsSection();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(),'Monthly Budget Overview')]")));
        double beforeRemaining = extractDollar(
                driver.findElement(By.cssSelector("[aria-label='Remaining budget amount']")).getText());

        // Add a new expense of $25
        openExpensesSection();
        addExpense("RemainTest", "25", 0);

        // Read remaining budget AFTER adding expense
        openBudgetsSection();
        wait.until(driver -> {
            double after = extractDollar(
                    driver.findElement(By.cssSelector("[aria-label='Remaining budget amount']")).getText());
            return after <= beforeRemaining - 25;
        });
        double afterRemaining = extractDollar(
                driver.findElement(By.cssSelector("[aria-label='Remaining budget amount']")).getText());

        assertEquals(beforeRemaining - 25, afterRemaining, 0.01,
                "Remaining budget should decrease by the expense amount");
    }

    // local utility (can later move to base)
    private double extractDollar(String txt) {
        return Double.parseDouble(txt.replace("$", "").trim());
    }
} 