import React from "react";
import { render, screen, act } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import "@testing-library/jest-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { BudgetOverview } from "../../components";
import {
  useUserBudget,
  useUpdateBudget,
  useCreateBudget,
  useSendUserReportEmail,
} from "../../hooks";

// Mock hooks
jest.mock("../../hooks");

const mockBudget = {
  id: 1,
  amount: 1000,
  remaining: 500,
};

const mockUserId = 1;

describe("BudgetOverview Component", () => {
  const queryClient = new QueryClient();

  beforeEach(() => {
    jest.clearAllMocks();

    // Mock `useUserBudget` hook
    (useUserBudget as jest.Mock).mockReturnValue({
      data: mockBudget,
      isLoading: false,
      isError: false,
    });

    // Mock `useUpdateBudget` hook
    (useUpdateBudget as jest.Mock).mockReturnValue({
      mutate: jest.fn((data, { onSuccess }) => onSuccess && onSuccess()),
      isPending: false,
    });

    // Mock `useCreateBudget` hook
    (useCreateBudget as jest.Mock).mockReturnValue({
      mutate: jest.fn((data, { onSuccess }) => onSuccess && onSuccess()),
      isPending: false,
    });

    // Mock `useSendUserReportEmail` hook
    (useSendUserReportEmail as jest.Mock).mockReturnValue({
      mutate: jest.fn((data, { onSuccess }) => onSuccess && onSuccess()),
      isPending: false,
    });
  });

  const renderComponent = () => {
    return render(
      <QueryClientProvider client={queryClient}>
        <BudgetOverview userId={mockUserId} />
      </QueryClientProvider>
    );
  };

  it("renders the budget overview with filters and budget details", () => {
    renderComponent();

    expect(screen.getByText("Monthly Budget Overview")).toBeInTheDocument();
    expect(screen.getByLabelText("Month")).toBeInTheDocument();
    expect(screen.getByLabelText("Year")).toBeInTheDocument();
    expect(screen.getByText("Total Budget")).toBeInTheDocument();
    expect(screen.getByText("$1000")).toBeInTheDocument();
    expect(screen.getByText("Remaining Budget")).toBeInTheDocument();
    expect(screen.getByText("$500")).toBeInTheDocument();
  });

  it("allows applying filters", async () => {
    renderComponent();
  
    const monthInput = screen.getByLabelText("Filter by month");
    const yearInput = screen.getByLabelText("Filter by year");
    const applyFiltersButton = screen.getByLabelText("Apply filters");
  
    // Clear existing value before typing
    await userEvent.clear(monthInput);
    await userEvent.type(monthInput, "5");
  
    await userEvent.clear(yearInput);
    await userEvent.type(yearInput, "2025");
  
    await userEvent.click(applyFiltersButton);
  
    expect(monthInput).toHaveValue(5); // Correct value
    expect(yearInput).toHaveValue(2025); // Correct value
  });  

  it("allows clearing filters", async () => {
    renderComponent();

    const clearFiltersButton = screen.getByText("Clear Filters");

    await userEvent.click(clearFiltersButton);

    expect(screen.getByLabelText("Month")).toHaveValue(new Date().getMonth() + 1);
    expect(screen.getByLabelText("Year")).toHaveValue(new Date().getFullYear());
  });
  
  it("renders 'Edit Budget' button and handles updating a budget", async () => {
    renderComponent();

    const editBudgetButton = screen.getByText("Edit Budget");
    await userEvent.click(editBudgetButton);

    const budgetInput = screen.getByLabelText("New Budget Amount");
    await userEvent.clear(budgetInput);
    await userEvent.type(budgetInput, "1500");

    const saveButton = screen.getByText("Update Budget");
    await act(async () => {
      await userEvent.click(saveButton);
    });

    expect(screen.queryByText("Update Budget")).not.toBeInTheDocument();
  });

  it("shows an error message when the budget fails to load", () => {
    (useUserBudget as jest.Mock).mockReturnValueOnce({
      data: null,
      isLoading: false,
      isError: true,
    });

    renderComponent();

    expect(screen.getByText("Error loading budget. Please try again.")).toBeInTheDocument();
  });
});
