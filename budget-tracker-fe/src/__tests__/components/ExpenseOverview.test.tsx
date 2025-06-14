import React from "react";
import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import "@testing-library/jest-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { ExpenseOverview } from "../../components";
import { useExpenses, useCategories, useDeleteExpense } from "../../hooks";

// Mock hooks
jest.mock("../../hooks");

const mockExpenses = [
  { id: 1, title: "Groceries", amount: 50.75, date: "2024-12-25", categoryId: 1, userId: 1 },
  { id: 2, title: "Rent", amount: 1200, date: "2024-12-01", categoryId: 2, userId: 1 },
];

const mockCategories = [
  { id: 1, name: "Food" },
  { id: 2, name: "Housing" },
];

const queryClient = new QueryClient();

describe("ExpenseOverview Component", () => {
  beforeEach(() => {
    jest.clearAllMocks();

    // Mock `useExpenses` hook
    (useExpenses as jest.Mock).mockReturnValue({
      data: mockExpenses,
      isLoading: false,
    });

    // Mock `useCategories` hook
    (useCategories as jest.Mock).mockReturnValue({
      data: mockCategories,
    });

    // Mock `useDeleteExpense` hook
    const mutateMock = jest.fn();
    (useDeleteExpense as jest.Mock).mockReturnValue({
      mutate: mutateMock,
    });
  });

  const renderComponent = () => {
    return render(
      <QueryClientProvider client={queryClient}>
        <ExpenseOverview userId={1} />
      </QueryClientProvider>
    );
  };

  it("renders the ExpenseOverview component with correct elements", () => {
    renderComponent();

    // Check title and Add button
    expect(screen.getByText("Expense Overview")).toBeInTheDocument();
    expect(screen.getByRole("button", { name: /add expense/i })).toBeInTheDocument();

    // Check filter inputs
    expect(screen.getByLabelText("filter by start date")).toBeInTheDocument();
    expect(screen.getByLabelText("filter by end date")).toBeInTheDocument();
    expect(screen.getByLabelText("filter by minimum amount")).toBeInTheDocument();
    expect(screen.getByLabelText("filter by maximum amount")).toBeInTheDocument();
    expect(screen.getByLabelText("filter by category")).toBeInTheDocument();
    expect(screen.getByLabelText("filter by month")).toBeInTheDocument();
    expect(screen.getByLabelText("filter by year")).toBeInTheDocument();

    // Check for expense rows
    expect(screen.getByText("Groceries - $50.75")).toBeInTheDocument();
    expect(screen.getByText("Rent - $1200")).toBeInTheDocument();
  });

  it("displays the correct number of expense rows", () => {
    renderComponent();

    const expenseRows = screen.getAllByRole("listitem");
    expect(expenseRows).toHaveLength(mockExpenses.length);
  });

  it("opens the modal for adding a new expense", async () => {
    renderComponent();

    // Click the Add button
    const addButton = screen.getByRole("button", { name: /add expense/i });
    await userEvent.click(addButton);

    // Check that the modal is opened
    expect(screen.getByText("Add New Expense")).toBeInTheDocument();
  });

  it("opens the modal for editing an expense", async () => {
    renderComponent();

    // Click the Edit button of the first expense
    const editButtons = screen.getAllByRole("button", { name: /edit/i });
    await userEvent.click(editButtons[0]);

    // Check that the modal is opened with correct pre-filled data
    expect(screen.getByText("Edit Expense")).toBeInTheDocument();
    expect(screen.getByDisplayValue("Groceries")).toBeInTheDocument();
    expect(screen.getByDisplayValue("50.75")).toBeInTheDocument();
    expect(screen.getByDisplayValue("2024-12-25")).toBeInTheDocument();
  });

  it("filters the expenses correctly", async () => {
    renderComponent();

    // Set a filter value
    const minAmountInput = screen.getByLabelText("filter by minimum amount");
    await userEvent.type(minAmountInput, "100");

    const applyFiltersButton = screen.getByRole("button", { name: /apply filters/i });
    await userEvent.click(applyFiltersButton);

    // Wait for the filtered results
    await waitFor(() => {
      expect(screen.getByText("Rent - $1200")).toBeInTheDocument();
    });
  });

  it("clears the filters correctly", async () => {
    renderComponent();

    // Set and clear a filter value
    const minAmountInput = screen.getByLabelText("filter by minimum amount");
    await userEvent.type(minAmountInput, "100");

    const clearFiltersButton = screen.getByRole("button", { name: /clear filters/i });
    await userEvent.click(clearFiltersButton);

    // Wait for the cleared results
    await waitFor(() => {
      expect(screen.getByText("Groceries - $50.75")).toBeInTheDocument();
      expect(screen.getByText("Rent - $1200")).toBeInTheDocument();
    });
  });

  it("handles the delete expense action", async () => {
    const mutateMock = jest.fn((id, { onSuccess }) => onSuccess());
    (useDeleteExpense as jest.Mock).mockReturnValue({ mutate: mutateMock });

    renderComponent();

    // Click the Delete button of the first expense
    const deleteButtons = screen.getAllByRole("button", { name: /delete/i });
    await userEvent.click(deleteButtons[0]);

    // Verify that the delete API was called
    expect(mutateMock).toHaveBeenCalledWith(1, expect.anything());

    // Check for the success alert
    await waitFor(() => {
      expect(screen.getByText("Expense deleted successfully!")).toBeInTheDocument();
    });
  });

  it("displays a message when no expenses are found", () => {
    (useExpenses as jest.Mock).mockReturnValue({ data: [], isLoading: false });

    renderComponent();

    // Check for the no expenses message
    expect(screen.getByText("No expenses found.")).toBeInTheDocument();
  });
});
