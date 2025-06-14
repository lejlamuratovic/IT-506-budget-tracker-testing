import { useMutation } from '@tanstack/react-query';

import { getLoginUser, postLoginUser } from '../api';
import { User } from '../types';

export const useLogin = () => {
    return useMutation<User, Error, string>({
        mutationFn: async (email: string) => {
            const user = await getLoginUser(email);
            if (!user) {
                return postLoginUser(email);
            }
            return user;
        }
    });
};
