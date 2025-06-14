export const BASE_URL = import.meta.env.VITE_BASE_URL;

// User Types
export type User = {
    id: number;
    email: string;
}

// Budget Types
export type Budget = {
    id: number;
    amount: number;
    remaining: number;
    month: number;
    year: number;
    userId: number;
}

// Expense Types
export type Expense = {
    id?: number;
    title: string;
    amount: number;
    date: string; // Use ISO format for dates
    categoryId: number | undefined;
    userId: number;
}

export type Category = {
    id: number;
    name: string;
}

// Updated Chart Types
export type CategoryChartData = {
    categoryName: string;
    totalAmount: number; // Add total amount
    expenseCount: number;
    expenses: Expense[]; // Add list of expenses
};

export type ExpenseFilterParams = {
    userId: number;
    startDate?: Date | null;
    endDate?: Date | null;
    minAmount?: number | null;
    maxAmount?: number | null;
    categoryId?: number | null;
    month?: number | null;
    year?: number | null;
}

export type EmailRequest = {
    email: string;
    userId: number;
    month: number;
    year: number;
}
