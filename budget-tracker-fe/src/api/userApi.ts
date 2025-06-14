import axios from 'axios';
import { User, BASE_URL } from '../types';

const api = axios.create({
    baseURL: `${BASE_URL}users`,
    headers: {
        'Content-Type': 'application/json',
    },
});

export const getLoginUser = async (email: string): Promise<User | null> => {
    try {
        const response = await api.get<User>(`/login?email=${email}`);
        return response.data;
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    } catch (error: any) {
        if (error.response?.status === 404) {
            return null;
        }
        throw error;
    }
};

export const postLoginUser = async (email: string): Promise<User> => {
    const response = await api.post<User>('/login', email, {
        headers: { 'Content-Type': 'text/plain' },
    });
    return response.data;
};

export default api;
