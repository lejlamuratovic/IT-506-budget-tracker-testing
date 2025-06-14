import React from "react";
import { mount } from "cypress/react18";
import DashboardPage from "../../pages/DashboardPage";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { MemoryRouter } from "react-router-dom";

// Mock Components
const MockExpenseOverview = () => <div>Mock Expense Overview</div>;
const MockChartOverview = () => <div>Mock Chart Overview</div>;
const MockBudgetOverview = () => <div>Mock Budget Overview</div>;

describe("DashboardPage Component", () => {
  let queryClient: QueryClient;

  beforeEach(() => {
    // Create a new QueryClient for each test
    queryClient = new QueryClient();

    // Mock localStorage
    localStorage.setItem("userId", "1");
    localStorage.setItem("userEmail", "test@example.com");

    // Mount the DashboardPage with mock components
    mount(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <DashboardPage
            ExpenseOverview={MockExpenseOverview}
            ChartOverview={MockChartOverview}
            BudgetOverview={MockBudgetOverview}
          />
        </MemoryRouter>
      </QueryClientProvider>
    );
  });

  it("renders the dashboard page correctly", () => {
    // Validate the welcome message and buttons
    cy.contains("Welcome, test@example.com").should("be.visible");
    cy.contains("View Expenses").should("be.visible");
    cy.contains("Mock Expense Overview").should("be.visible");
  });

  it("switches to the charts section when 'View Charts' is clicked", () => {
    // Click the "View Charts" button
    cy.contains("View Charts").click();

    // Validate that the charts section is rendered
    cy.contains("Mock Chart Overview").should("be.visible");
    cy.contains("Mock Expense Overview").should("not.exist");
  });

  it("switches to the budgets section when 'View Budgets' is clicked", () => {
    // Click the "View Budgets" button
    cy.contains("View Budgets").click();

    // Validate that the budgets section is rendered
    cy.contains("Mock Budget Overview").should("be.visible");
    cy.contains("Mock Expense Overview").should("not.exist");
  });

  it("switches back to the expenses section when 'View Expenses' is clicked", () => {
    // Click the "View Charts" button first
    cy.contains("View Charts").click();
    cy.contains("Mock Chart Overview").should("be.visible");

    // Click the "View Expenses" button
    cy.contains("View Expenses").click();
    cy.contains("Mock Expense Overview").should("be.visible");
    cy.contains("Mock Chart Overview").should("not.exist");
  });
});
