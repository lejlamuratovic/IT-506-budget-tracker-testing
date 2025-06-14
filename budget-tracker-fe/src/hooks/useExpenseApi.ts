import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';

import { getAllExpenses, createExpense, updateExpense, deleteExpense, getCategoryChartData } from '../api';
import { CategoryChartData, Expense, ExpenseFilterParams } from '../types';


export const useExpenses = (params: ExpenseFilterParams) => {
    return useQuery<Expense[]>({
        queryKey: ['expenses', params],
        queryFn: () => getAllExpenses(params),
    });
};

export const useCreateExpense = () => {
    const queryClient = useQueryClient();

    return useMutation<Expense, Error, Partial<Expense>>({
        mutationFn: createExpense,
        onSuccess: () => {
            // Invalidate the 'expenses' query to refresh the expense list
            queryClient.invalidateQueries({ queryKey: ['expenses'] });
        },
    });
};

export const useUpdateExpense = () => {
    const queryClient = useQueryClient();

    return useMutation<Expense, Error, { id: number; expense: Partial<Expense> }>({
        mutationFn: updateExpense,
        onSuccess: () => {
            // Invalidate the 'expenses' query to refresh the expense list
            queryClient.invalidateQueries({ queryKey: ['expenses'] });
        },
    });
};

export const useDeleteExpense = () => {
    const queryClient = useQueryClient();
    
    return useMutation<void, Error, number>({
        mutationFn: deleteExpense,
        onSuccess: () => {
            // Invalidate the 'expenses' query to refresh the expense list
            queryClient.invalidateQueries({ queryKey: ['expenses'] });
        },
    });
};

export const useCategoryChartData = (filters: { userId: number; startDate?: Date | null; endDate?: Date | null }) => {
    return useQuery<CategoryChartData[]>({
        queryKey: ['categoryChartData', filters],
        queryFn: () => getCategoryChartData(filters),
    });
};
