import { useQuery } from '@tanstack/react-query';

import { getAllCategories } from '../api';
import { Category } from '../types';

export const useCategories = () => {
    return useQuery<Category[]>({
        queryKey: ['categories'],
        queryFn: getAllCategories,
    });
};
