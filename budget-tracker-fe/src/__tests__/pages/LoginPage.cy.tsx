import React from "react";
import { mount } from "cypress/react18";
import LoginPage from "../../pages/LoginPage";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { MemoryRouter } from "react-router-dom";

describe("LoginPage Component", () => {
  let queryClient: QueryClient;

  beforeEach(() => {
    // Create a new QueryClient for each test
    queryClient = new QueryClient();

    // Intercept API calls for login
    cy.intercept("GET", "**/users/login?email=test@example.com", {
      statusCode: 200,
      body: { id: 1, email: "test@example.com" },
    }).as("getLoginUser");

    cy.intercept("POST", "**/users/login", {
      statusCode: 200,
      body: { id: 2, email: "newuser@example.com" },
    }).as("postLoginUser");

    // Mount the LoginPage component with QueryClientProvider and MemoryRouter
    mount(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <LoginPage />
        </MemoryRouter>
      </QueryClientProvider>
    );
  });

  it("renders the login form correctly", () => {
    // Validate that the login form renders correctly
    cy.contains("Login").should("be.visible");
    cy.get('input[type="email"]').should("be.visible");
    cy.get('button').contains("Log In").should("be.visible");
  });

  it("handles successful login for existing user", () => {
    // Enter an existing user's email and log in
    cy.get('input[type="email"]').type("test@example.com");
    cy.get('button').contains("Log In").click();

    // Wait for the GET request and validate the response
    cy.wait("@getLoginUser").then(() => {
      // Verify localStorage is updated
      expect(localStorage.getItem("userId")).to.equal("1");
      expect(localStorage.getItem("userEmail")).to.equal("test@example.com");
    });
  });

  it("handles successful login for new user", () => {
    // Enter a new user's email and log in
    cy.get('input[type="email"]').type("newuser@example.com");
    cy.get('button').contains("Log In").click();

    // Wait for the POST request and validate the response
    cy.wait("@postLoginUser").then(() => {
      // Verify localStorage is updated
      expect(localStorage.getItem("userId")).to.equal("2");
      expect(localStorage.getItem("userEmail")).to.equal("newuser@example.com");
    });
  });

  it("displays error message for failed login", () => {
    // Intercept an invalid login attempt
    cy.intercept("GET", "**/users/login?email=invalid@example.com", {
      statusCode: 500,
      body: { message: "User not found" },
    }).as("getInvalidLoginUser");
  
    // Enter an invalid email and attempt login
    cy.get('input[type="email"]').type("invalid@example.com");
    cy.get('button').contains("Log In").click();
  
    // Wait for the GET request and validate the error message
    cy.wait("@getInvalidLoginUser").then(() => {
      cy.contains("Failed to log in. Please try again.", { timeout: 10000 }).should("be.visible");
    });
  });  
});
