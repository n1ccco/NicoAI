import { api } from '@/lib/api-client';
import { LoginInput, RegisterInput } from '@/lib/auth/types';
import { AuthResponse, User } from '@/types/api';

export const getUser = (): Promise<User> => {
  return api.get('/auth/me');
};

export const logout = (): Promise<void> => {
  return api.post('/auth/logout');
};

export const loginWithUsernameAndPassword = (
  data: LoginInput,
): Promise<AuthResponse> => {
  return api.post('/auth/login', data);
};

export const registerWithUsernameAndPassword = (
  data: RegisterInput,
): Promise<AuthResponse> => {
  return api.post('/auth/register', data);
};
