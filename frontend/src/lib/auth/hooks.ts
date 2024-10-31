import { api } from '@/lib/api-client';
import { apiWithCredentials } from '@/lib/api-credentials';
import { LoginInput, RegisterInput } from '@/lib/auth/types';
import { AuthResponse, JwtRefreshResponse, User } from '@/types/api';

export const getUser = (): Promise<User> => {
  return api.get('/auth/me');
};

export const refreshToken = (): Promise<JwtRefreshResponse> => {
  return apiWithCredentials.post('/auth/refresh');
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
