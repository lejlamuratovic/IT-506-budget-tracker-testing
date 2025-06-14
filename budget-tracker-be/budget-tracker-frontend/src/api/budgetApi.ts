import axios from 'axios';
import { Budget, BASE_URL } from '../types';

const api = axios.create({
    baseURL: `${BASE_URL}budgets`,
    headers: {
        'Content-Type': 'application/json',
    },
});

export const getUserBudget = async (userId: number, month: number, year: number): Promise<Budget | null> => {
    const response = await api.get<Budget | null>(`/user`, {
        params: { userId, month, year },
    });
    return response.data;
};

export const createBudget = async (budget: Partial<Budget>): Promise<Budget> => {
    const response = await api.post<Budget>('', budget);
    return response.data;
};

export const updateBudget = async ({ budgetId, budget }: { budgetId: number; budget: Partial<Budget> }): Promise<Budget> => {
    const response = await api.put<Budget>(`${budgetId}`, budget);
    return response.data;
};

export const deleteBudget = async (budgetId: number): Promise<void> => {
    await api.delete(`${budgetId}`);
};

export default api;
