import React from "react";
import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import "@testing-library/jest-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { ChartOverview } from "../../components";
import { useCategoryChartData } from "../../hooks";

// Mock hooks
jest.mock("../../hooks");

const mockChartData = [
  { categoryName: "Food", expenseCount: 5 },
  { categoryName: "Housing", expenseCount: 2 },
];

const queryClient = new QueryClient();

global.ResizeObserver = class {
  observe() {}
  unobserve() {}
  disconnect() {}
};

describe("ChartOverview Component", () => {
  beforeEach(() => {
    jest.clearAllMocks();

    // Mock `useCategoryChartData` hook
    (useCategoryChartData as jest.Mock).mockReturnValue({
      data: mockChartData,
      isLoading: false,
    });
  });

  const renderComponent = () => {
    return render(
      <QueryClientProvider client={queryClient}>
        <ChartOverview userId={1} />
      </QueryClientProvider>
    );
  };

  it("renders the ChartOverview component with correct elements", () => {
    renderComponent();

    // Check title
    expect(screen.getByText("Expense Chart Overview")).toBeInTheDocument();

    // Check filter inputs
    expect(screen.getByLabelText("Start Date")).toBeInTheDocument();
    expect(screen.getByLabelText("End Date")).toBeInTheDocument();

    // Check Apply Filters and Clear Filters buttons
    expect(screen.getByRole("button", { name: /apply filters/i })).toBeInTheDocument();
    expect(screen.getByRole("button", { name: /clear filters/i })).toBeInTheDocument();

    // Check chart and summary
    expect(screen.getByText("Food")).toBeInTheDocument();
    expect(screen.getByText("5 expenses")).toBeInTheDocument();
    expect(screen.getByText("Housing")).toBeInTheDocument();
    expect(screen.getByText("2 expenses")).toBeInTheDocument();
  });

  it("handles filter inputs and applies them correctly", async () => {
    renderComponent();
  
    // Get inputs by their unique labels
    const startDateInput = screen.getByLabelText("Start Date");
    const endDateInput = screen.getByLabelText("End Date");
  
    // Set filter values
    await userEvent.type(startDateInput, "2024-01-01");
    await userEvent.type(endDateInput, "2024-12-31");
  
    // Click Apply Filters
    const applyFiltersButton = screen.getByRole("button", { name: /apply filters/i });
    await userEvent.click(applyFiltersButton);
  
    // Validate interaction
    await waitFor(() => {
      expect(startDateInput).toHaveValue("2024-01-01");
      expect(endDateInput).toHaveValue("2024-12-31");
    });
  });
  

  it("clears the filters correctly", async () => {
    renderComponent();

    // Set filter values
    const startDateInput = screen.getByLabelText("Start Date");
    const endDateInput = screen.getByLabelText("End Date");
    await userEvent.type(startDateInput, "2024-01-01");
    await userEvent.type(endDateInput, "2024-12-31");

    // Click Clear Filters
    const clearFiltersButton = screen.getByRole("button", { name: /clear filters/i });
    await userEvent.click(clearFiltersButton);

    // Validate that filters are cleared
    await waitFor(() => {
      expect(startDateInput).toHaveValue("");
      expect(endDateInput).toHaveValue("");
    });
  });

  it("displays a message when no chart data is available", () => {
    (useCategoryChartData as jest.Mock).mockReturnValue({ data: [], isLoading: false });

    renderComponent();

    // Check for no data message
    expect(screen.getByText("No data available to display the chart.")).toBeInTheDocument();
  });
});
