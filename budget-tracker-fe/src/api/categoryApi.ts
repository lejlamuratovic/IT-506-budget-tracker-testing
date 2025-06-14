import axios from 'axios';
import { Category, BASE_URL } from '../types';

const api = axios.create({
    baseURL: `${BASE_URL}categories`,
    headers: {
        'Content-Type': 'application/json',
    },
});

export const getAllCategories = async (): Promise<Category[]> => {
    const response = await api.get<Category[]>('');
    return response.data;
};

export default api;
