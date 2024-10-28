import { api } from '@/shared/lib/api-client';
import { LoginInput, RegisterInput } from '@/shared/lib/auth/types';
import { AuthResponse, User } from '@/shared/types/api';

export const getUser = (): Promise<User> => {
  return api.get('/auth/me');
};

export const logout = (): Promise<void> => {
  return api.post('/auth/logout');
};

export const loginWithUsernameAndPassword = (
  data: LoginInput,
): Promise<AuthResponse> => {
  return api.post('/auth/login', data, { withCredentials: true });
};

export const registerWithUsernameAndPassword = (
  data: RegisterInput,
): Promise<AuthResponse> => {
  return api.post('/auth/register', data);
};
