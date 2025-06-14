import React from "react";
import { render, screen } from "@testing-library/react";
import "@testing-library/jest-dom";
import { ExpenseRow } from "../../components";
import { Expense } from "../../types";
import userEvent from "@testing-library/user-event";

describe("ExpenseRow Component", () => {
  const mockExpense: Expense = {
    id: 1,
    title: "Groceries",
    amount: 50.75,
    date: "2024-12-25",
    categoryId: 1,
    userId: 1,
  };

  const mockOnEdit = jest.fn();
  const mockOnDelete = jest.fn();

  beforeEach(() => {
    jest.clearAllMocks();
  });

  it("renders the expense details correctly", () => {
    render(<ExpenseRow expense={mockExpense} onEdit={mockOnEdit} onDelete={mockOnDelete} />);

    // Check if the primary text matches the expected format
    expect(screen.getByText("Groceries - $50.75")).toBeInTheDocument();

    // Check if the secondary text matches the expected date
    expect(screen.getByText("Date: 12/25/2024")).toBeInTheDocument(); // Adjust date format if needed
  });

  it("renders edit and delete buttons", () => {
    render(<ExpenseRow expense={mockExpense} onEdit={mockOnEdit} onDelete={mockOnDelete} />);

    // Check if the edit button is rendered
    expect(screen.getByRole("button", { name: /edit/i })).toBeInTheDocument();

    // Check if the delete button is rendered
    expect(screen.getByRole("button", { name: /delete/i })).toBeInTheDocument();
  });

  it("calls onEdit when the edit button is clicked", async () => {
    render(<ExpenseRow expense={mockExpense} onEdit={mockOnEdit} onDelete={mockOnDelete} />);

    // Simulate clicking the edit button
    const editButton = screen.getByRole("button", { name: /edit/i });
    await userEvent.click(editButton);

    // Check if the onEdit function was called with the correct expense
    expect(mockOnEdit).toHaveBeenCalledWith(mockExpense);
    expect(mockOnEdit).toHaveBeenCalledTimes(1);
  });

  it("calls onDelete when the delete button is clicked", async () => {
    render(<ExpenseRow expense={mockExpense} onEdit={mockOnEdit} onDelete={mockOnDelete} />);

    // Simulate clicking the delete button
    const deleteButton = screen.getByRole("button", { name: /delete/i });
    await userEvent.click(deleteButton);

    // Check if the onDelete function was called with the correct expense ID
    expect(mockOnDelete).toHaveBeenCalledWith(mockExpense.id);
    expect(mockOnDelete).toHaveBeenCalledTimes(1);
  });
});
