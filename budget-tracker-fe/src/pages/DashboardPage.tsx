import React, { useState } from "react";
import { Box, Typography, Grid, Button, Paper } from "@mui/material";
import { ExpenseOverview as DefaultExpenseOverview, ChartOverview as DefaultChartOverview, BudgetOverview as DefaultBudgetOverview } from "../components/index";

type DashboardPageProps = {
  ExpenseOverview?: React.ComponentType<{ userId: number }>;
  ChartOverview?: React.ComponentType<{ userId: number }>;
  BudgetOverview?: React.ComponentType<{ userId: number }>;
};

const DashboardPage: React.FC<DashboardPageProps> = ({
  ExpenseOverview = DefaultExpenseOverview,
  ChartOverview = DefaultChartOverview,
  BudgetOverview = DefaultBudgetOverview,
}) => {
  const email = localStorage.getItem("userEmail");
  const userId = localStorage.getItem("userId");
  const [activeSection, setActiveSection] = useState<"expenses" | "charts" | "budgets">("expenses");

  return (
    <Box sx={{ padding: "2rem" }}>
      <Typography variant="h5" gutterBottom>
        Welcome, {email}
      </Typography>

      {/* Section Selector */}
      <Grid container spacing={3}>
        {/* Expense Overview */}
        <Grid item xs={12} md={4}>
          <Paper sx={{ padding: "1rem" }} elevation={3}>
            <Typography variant="h6" gutterBottom>
              Expenses
            </Typography>
            <Button
              variant="contained"
              color="primary"
              fullWidth
              onClick={() => setActiveSection("expenses")}
            >
              View Expenses
            </Button>
          </Paper>
        </Grid>

        {/* Chart Overview */}
        <Grid item xs={12} md={4}>
          <Paper sx={{ padding: "1rem" }} elevation={3}>
            <Typography variant="h6" gutterBottom>
              Charts
            </Typography>
            <Button
              variant="contained"
              color="secondary"
              fullWidth
              onClick={() => setActiveSection("charts")}
            >
              View Charts
            </Button>
          </Paper>
        </Grid>

        {/* Budget Overview */}
        <Grid item xs={12} md={4}>
          <Paper sx={{ padding: "1rem" }} elevation={3}>
            <Typography variant="h6" gutterBottom>
              Budgets
            </Typography>
            <Button
              variant="contained"
              color="success"
              fullWidth
              onClick={() => setActiveSection("budgets")}
            >
              View Budgets
            </Button>
          </Paper>
        </Grid>
      </Grid>

      {/* Render Active Section */}
      <Box sx={{ marginTop: "2rem" }}>
        {activeSection === "expenses" && ExpenseOverview && <ExpenseOverview userId={parseInt(userId!)} />}
        {activeSection === "charts" && ChartOverview && <ChartOverview userId={parseInt(userId!)} />}
        {activeSection === "budgets" && BudgetOverview && <BudgetOverview userId={parseInt(userId!)} />}
      </Box>
    </Box>
  );
};

export default DashboardPage;
