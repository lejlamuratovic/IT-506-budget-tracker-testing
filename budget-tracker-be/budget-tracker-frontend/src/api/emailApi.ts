import axios from 'axios';
import { BASE_URL, EmailRequest } from '../types';

const api = axios.create({
    baseURL: `${BASE_URL}emails`,
    headers: {
        'Content-Type': 'application/json',
    },
});

export const sendUserReportEmail = async (emailRequest: EmailRequest): Promise<string> => {
    const response = await api.post<string>('/send-report', emailRequest);
    return response.data;
};

export default api;
