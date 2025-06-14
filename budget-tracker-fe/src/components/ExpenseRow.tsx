import React from "react";
import { ListItem, ListItemText, IconButton } from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import EditIcon from "@mui/icons-material/Edit";

import { Expense } from "../types";

interface ExpenseRowProps {
  expense: Expense;
  onEdit: (expense: Expense) => void;
  onDelete: (expenseId: number) => void;
}

const ExpenseRow: React.FC<ExpenseRowProps> = ({ expense, onEdit, onDelete }) => {
  return (
    <ListItem
      secondaryAction={
        <>
          <IconButton edge="end" aria-label="edit" onClick={() => onEdit(expense!)}>
            <EditIcon color="primary" />
          </IconButton>
          {expense.id !== null && (
            <IconButton edge="end" aria-label="delete" onClick={() => onDelete(expense.id!)}>
              <DeleteIcon color="error" />
            </IconButton>
          )}
        </>
      }
    >
      <ListItemText
        primary={`${expense?.title} - $${expense?.amount}`}
        secondary={`Date: ${new Date(expense.date).toLocaleDateString()}`}
      />
    </ListItem>
  );
};

export default ExpenseRow;
