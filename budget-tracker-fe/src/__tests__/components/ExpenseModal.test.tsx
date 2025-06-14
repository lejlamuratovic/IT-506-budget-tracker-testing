import React from "react";
import { render, screen } from "@testing-library/react";
import "@testing-library/jest-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { ExpenseModal } from "../../components";
import { useCategories, useCreateExpense, useUpdateExpense } from "../../hooks";

// Mock hooks
jest.mock("../../hooks");

const mockCategories = [
  { id: 1, name: "Food" },
  { id: 2, name: "Housing" },
];

const mockExpense = {
  id: 1,
  title: "Groceries",
  amount: 50.75,
  date: "2024-12-25",
  categoryId: 1,
  userId: 1,
};

describe("ExpenseModal Component", () => {
  const queryClient = new QueryClient();
  const onClose = jest.fn();
  const onSuccess = jest.fn();

  beforeEach(() => {
    jest.clearAllMocks();

    // Mock `useCategories` hook
    (useCategories as jest.Mock).mockReturnValue({
      data: mockCategories,
    });

    // Mock `useCreateExpense` hook
    (useCreateExpense as jest.Mock).mockReturnValue({
      mutate: jest.fn((formData, { onSuccess }) => onSuccess && onSuccess()),
    });

    // Mock `useUpdateExpense` hook
    (useUpdateExpense as jest.Mock).mockReturnValue({
      mutate: jest.fn((formData, { onSuccess }) => onSuccess && onSuccess()),
    });
  });

  const renderModal = (mode: "add" | "edit", expense?: typeof mockExpense) => {
    return render(
      <QueryClientProvider client={queryClient}>
        <ExpenseModal
          mode={mode}
          userId={1}
          expense={expense}
          onClose={onClose}
          onSuccess={onSuccess}
        />
      </QueryClientProvider>
    );
  };

  it("renders correctly in 'add' mode", () => {
    renderModal("add");

    expect(screen.getByText("Add New Expense")).toBeInTheDocument();
    expect(screen.getByLabelText("Title")).toBeInTheDocument();
    expect(screen.getByLabelText("Amount")).toBeInTheDocument();
    expect(screen.getByLabelText("Date")).toBeInTheDocument();
    expect(screen.getByLabelText("Category")).toBeInTheDocument();
  });

  it("renders correctly in 'edit' mode with pre-filled data", () => {
    renderModal("edit", mockExpense);

    expect(screen.getByText("Edit Expense")).toBeInTheDocument();
    expect(screen.getByLabelText("Title")).toHaveValue("Groceries");
    expect(screen.getByLabelText("Amount")).toHaveValue(50.75);
    expect(screen.getByLabelText("Date")).toHaveValue("2024-12-25");
  });

  it("submits a new expense in 'add' mode", () => {
    renderModal("add");

    // Ensure the "Add Expense" button matches the expected text
    expect(screen.getByText("Add Expense")).toBeInTheDocument();
  });

  it("submits an updated expense in 'edit' mode", () => {
    renderModal("edit", mockExpense);

    // Ensure the "Save Changes" button matches the expected text
    expect(screen.getByText("Save Changes")).toBeInTheDocument();
  });
});
