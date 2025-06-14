import React from "react";
import { Modal, Box, Typography, List, ListItem, ListItemText, Divider } from "@mui/material";
import { Expense } from "../types";

interface CategoryExpenseModalProps {
  open: boolean;
  onClose: () => void;
  categoryName: string;
  expenses: Expense[];
}

const CategoryExpenseModal: React.FC<CategoryExpenseModalProps> = ({
  open,
  onClose,
  categoryName,
  expenses,
}) => {
  return (
    <Modal open={open} onClose={onClose} aria-labelledby="category-expense-modal-title">
      <Box
        sx={{
          position: "absolute",
          top: "50%",
          left: "50%",
          transform: "translate(-50%, -50%)",
          width: "80%",
          maxWidth: "600px",
          bgcolor: "background.paper",
          borderRadius: "8px",
          boxShadow: 24,
          p: 4,
          maxHeight: "90%",
          overflowY: "auto",
        }}
      >
        {/* Modal Header */}
        <Box
          sx={{
            textAlign: "center",
            //mb: 2,
            //borderBottom: "1px solid #ddd",
            //pb: 2,
          }}
        >
          <Typography
            id="category-expense-modal-title"
            variant="h5"
            sx={{ fontWeight: "bold", color: "#4A4A4A" }}
          >
            {categoryName}
          </Typography>
          <Typography variant="subtitle1" sx={{ color: "#888" }}>
            {expenses.length} {expenses.length > 1 ? "expenses" : "expense"} listed
          </Typography>
        </Box>

        {/* Expense List */}
        {expenses.length === 0 ? (
          <Typography align="center" sx={{ color: "#888" }}>
            No expenses available for this category.
          </Typography>
        ) : (
          <List>
            {expenses.map((expense, index) => (
              <React.Fragment key={expense.id}>
                <ListItem>
                  <ListItemText
                    primary={expense.title}
                    secondary={`Amount: $${expense.amount.toFixed(
                      2
                    )} | Date: ${new Date(expense.date).toLocaleDateString()}`}
                  />
                </ListItem>
                {index < expenses.length - 1 && <Divider />}
              </React.Fragment>
            ))}
          </List>
        )}
      </Box>
    </Modal>
  );
};

export default CategoryExpenseModal;
