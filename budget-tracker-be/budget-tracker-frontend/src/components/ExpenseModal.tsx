import React, { useState } from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Button,
  MenuItem,
} from "@mui/material";
import { useCategories, useCreateExpense, useUpdateExpense } from "../hooks";
import { Category, Expense } from "../types";
import ErrorAlert from "./CustomAlert";

interface ExpenseModalProps {
  mode: "add" | "edit";
  userId: number;
  expense?: Expense;
  onClose: () => void;
  onSuccess: () => void;
}

const ExpenseModal: React.FC<ExpenseModalProps> = ({
  mode,
  expense,
  onClose,
  onSuccess,
  userId,
}) => {
  const { data: categories = [] } = useCategories();
  const createExpense = useCreateExpense();
  const updateExpense = useUpdateExpense();

  const [formData, setFormData] = useState({
    title: expense?.title || "",
    amount: expense?.amount || 0,
    date: expense?.date || "",
    categoryId: expense?.categoryId || 1,
    userId: userId,
  });

  const [error, setError] = useState<string | null>(null);

  const handleInputChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: name === "categoryId" ? parseInt(value, 10) : value,
    }));
  };

  const handleSubmit = () => {
    setError(null);
    
    if (mode === "add") {
      createExpense.mutate(formData, {
        onSuccess: () => {
          onSuccess();
          onClose();
        },
        // eslint-disable-next-line @typescript-eslint/no-explicit-any
        onError: (err: any) => {
          setError(err.response?.data?.message || "An unexpected error occurred.");
        },
      });
    } else if (mode === "edit" && expense && expense.id) {
      updateExpense.mutate(
        {
          id: expense.id,
          expense: { ...formData, userId: expense.userId },
        },
        {
          onSuccess: () => {
            onSuccess();
            onClose();
          },
          // eslint-disable-next-line @typescript-eslint/no-explicit-any
          onError: (err: any) => {
            setError(err.response?.data?.message || "An unexpected error occurred.");
          },
        }
      );
    }
  };

  return (
    <Dialog open onClose={onClose} fullWidth>
      <DialogTitle>{mode === "add" ? "Add New Expense" : "Edit Expense"}</DialogTitle>
      <DialogContent>
        {error && <ErrorAlert message={error} onClose={() => setError(null)} />}
        <TextField
          label="Title"
          name="title"
          fullWidth
          margin="normal"
          value={formData.title}
          onChange={handleInputChange}
        />
        <TextField
          label="Amount"
          name="amount"
          type="number"
          fullWidth
          margin="normal"
          value={formData.amount}
          onChange={handleInputChange}
        />
        <TextField
          label="Date"
          name="date"
          type="date"
          fullWidth
          margin="normal"
          InputLabelProps={{ shrink: true }}
          value={formData.date.split("T")[0]}
          onChange={handleInputChange}
        />
        <TextField
          select
          label="Category"
          name="categoryId"
          fullWidth
          margin="normal"
          value={formData.categoryId?.toString()}
          onChange={handleInputChange}
        >
          {categories.map((category: Category) => (
            <MenuItem key={category.id} value={category.id}>
              {category.name}
            </MenuItem>
          ))}
        </TextField>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose} color="secondary">
          Cancel
        </Button>
        <Button onClick={handleSubmit} color="primary" variant="contained">
          {mode === "add" ? "Add Expense" : "Save Changes"}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default ExpenseModal;
