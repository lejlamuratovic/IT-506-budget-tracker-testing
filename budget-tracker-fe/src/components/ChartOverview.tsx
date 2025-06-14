import React, { useState } from "react";
import {
  Box,
  Typography,
  Paper,
  Grid,
  TextField,
  Button,
  Card,
  CardContent,
} from "@mui/material";
import { PieChart, Pie, Tooltip, Cell, ResponsiveContainer } from "recharts";

import { useCategoryChartData } from "../hooks";
import { CategoryExpenseModal } from "../components"; // Import the modal
import { CustomAlert, Loading } from ".";
import { Expense } from "../types";

const COLORS = ["#0088FE", "#00C49F", "#FFBB28", "#FF8042", "#A569BD", "#F5B041"];

interface ChartOverviewProps {
  userId: number;
}

const ChartOverview: React.FC<ChartOverviewProps> = ({ userId }) => {
  const [filters, setFilters] = useState({
    startDate: null as Date | null,
    endDate: null as Date | null,
  });
  const [alert, setAlert] = useState<{ type: "error" | "success"; message: string } | null>(null);

  const [selectedCategory, setSelectedCategory] = useState<{
    name: string;
    expenses: Expense[];
  } | null>(null);

  const { data: chartData = [], isLoading } = useCategoryChartData({
    ...filters,
    userId,
  });

  const handleFilterChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    setFilters((prev) => ({
      ...prev,
      [name]: value ? new Date(value) : null,
    }));
  };

  const handleClearFilters = () => {
    setFilters({ startDate: null, endDate: null });
  };

  const handleCategoryClick = (categoryName: string, expenses: Expense[]) => {
    setSelectedCategory({ name: categoryName, expenses });
  };

  const handleCloseModal = () => {
    setSelectedCategory(null);
  };

  if (isLoading) {
    return <Loading />;
  }

  return (
    <Paper sx={{ padding: "1rem" }} data-testid="chart-overview-container">
      <Typography
        variant="h5"
        gutterBottom
        textAlign="start"
        mb={4}
        mt={1}
        aria-label="Expense Chart Overview"
      >
        Expense Chart Overview
      </Typography>

      {alert && (
        <CustomAlert
          type={alert.type}
          message={alert.message}
          onClose={() => setAlert(null)}
        />
      )}

      <Box sx={{ marginBottom: "1rem" }} data-testid="filters-container">
        <Grid container spacing={2}>
          <Grid item xs={12} sm={6} md={3}>
            <TextField
              id="start-date"
              label="Start Date"
              type="date"
              name="startDate"
              fullWidth
              size="small"
              InputLabelProps={{ shrink: true }}
              value={filters.startDate ? filters.startDate.toISOString().split("T")[0] : ""}
              onChange={handleFilterChange}
            />
          </Grid>
          <Grid item xs={12} sm={6} md={3}>
            <TextField
              id="end-date"
              label="End Date"
              type="date"
              name="endDate"
              fullWidth
              size="small"
              InputLabelProps={{ shrink: true }}
              value={filters.endDate ? filters.endDate.toISOString().split("T")[0] : ""}
              onChange={handleFilterChange}
            />
          </Grid>
          <Grid
            item
            xs={12}
            sm={6}
            md={6}
            container
            gap={2}
            justifyContent="start"
            alignItems="start"
          >
            <Button
              variant="contained"
              color="primary"
              onClick={() => {}}
              aria-label="Apply Filters"
            >
              Apply Filters
            </Button>
            <Button
              variant="outlined"
              color="secondary"
              onClick={handleClearFilters}
              aria-label="Clear Filters"
            >
              Clear Filters
            </Button>
          </Grid>
        </Grid>
      </Box>

      {chartData.length === 0 ? (
        <Typography align="center" aria-label="No data message">
          No data available to display the chart.
        </Typography>
      ) : (
        <Grid container spacing={4} alignItems="center">
          <Grid item xs={12} md={6}>
            <ResponsiveContainer width="100%" height={500}>
              <PieChart aria-label="Expense Chart">
                <Pie
                  data={chartData}
                  dataKey="expenseCount"
                  nameKey="categoryName"
                  cx="50%"
                  cy="50%"
                  outerRadius={100}
                  fill="#8884d8"
                  label
                  onClick={(data, index) =>
                    handleCategoryClick(chartData[index].categoryName, chartData[index].expenses)
                  }
                >
                  {chartData.map((_, index) => (
                    <Cell
                      key={`cell-${index}`}
                      fill={COLORS[index % COLORS.length]}
                      aria-label={`Pie Slice for ${chartData[index].categoryName}`}
                    />
                  ))}
                </Pie>
                <Tooltip />
              </PieChart>
            </ResponsiveContainer>
          </Grid>
          <Grid item xs={12} md={4}>
            <Box data-testid="summary-container">
              <Typography variant="h6" gutterBottom aria-label="Chart Summary">
                Summary
              </Typography>
              {chartData.map((item, index) => (
                <Card
                  key={index}
                  elevation={3}
                  sx={{
                    marginBottom: "1rem",
                    backgroundColor: COLORS[index % COLORS.length],
                    color: "white",
                  }}
                  onClick={() => handleCategoryClick(item.categoryName, item.expenses)}
                  aria-label={`Summary Card for ${item.categoryName}`}
                >
                  <CardContent>
                    <Typography variant="subtitle1">
                      <strong>{item.categoryName}</strong>
                    </Typography>
                    <Typography>
                      {item.expenseCount} {item.expenseCount > 1 ? "expenses" : "expense"}
                    </Typography>
                  </CardContent>
                </Card>
              ))}
            </Box>
          </Grid>
        </Grid>
      )}

      {selectedCategory && (
        <CategoryExpenseModal
          open={!!selectedCategory}
          onClose={handleCloseModal}
          categoryName={selectedCategory.name}
          expenses={selectedCategory.expenses}
        />
      )}
    </Paper>
  );
};

export default ChartOverview;
