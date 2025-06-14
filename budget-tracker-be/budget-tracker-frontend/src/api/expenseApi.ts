import axios from 'axios';
import { Expense, ExpenseFilterParams, BASE_URL, CategoryChartData } from '../types';

const api = axios.create({
    baseURL: `${BASE_URL}expenses`,
    headers: {
        'Content-Type': 'application/json',
    },
});

export const getAllExpenses = async (params: ExpenseFilterParams): Promise<Expense[]> => {
    const response = await api.get<Expense[]>('/filter', { params });
    return response.data;
};

export const createExpense = async (expense: Partial<Expense>): Promise<Expense> => {
    const response = await api.post<Expense>('', expense);
    return response.data;
};

export const updateExpense = async ({ id, expense }: { id: number; expense: Partial<Expense> }): Promise<Expense> => {
    const response = await api.put<Expense>(`${id}`, expense);
    return response.data;
};

export const deleteExpense = async (id: number): Promise<void> => {
    await api.delete(`/${id}`);
};

export const getCategoryChartData = async (
    filters: { userId: number; startDate?: Date | null; endDate?: Date | null }
): Promise<CategoryChartData[]> => {
    const response = await api.get<CategoryChartData[]>(`/chart-data`, {
        params: filters,
    });
    return response.data;
};

export default api;
