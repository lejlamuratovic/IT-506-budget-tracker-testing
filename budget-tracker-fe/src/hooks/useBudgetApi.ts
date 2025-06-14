import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';

import { getUserBudget, createBudget, updateBudget } from '../api';
import { Budget } from '../types';

export const useUserBudget = (userId: number, month: number, year: number) => {
  return useQuery<Budget | null>({
    queryKey: ['budget', userId, month, year],
    queryFn: () => getUserBudget(userId, month, year),
  });
};

export const useCreateBudget = () => {
  const queryClient = useQueryClient();

  return useMutation<Budget, Error, Partial<Budget>>({
    mutationFn: createBudget,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['budget'] }); // Invalidate queries with 'budget' key
    },
  });
};

export const useUpdateBudget = () => {
  const queryClient = useQueryClient();

  return useMutation<Budget, Error, { budgetId: number; budget: Partial<Budget> }>({
    mutationFn: updateBudget,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['budget'] }); // Invalidate queries with 'budget' key
    },
  });
};
