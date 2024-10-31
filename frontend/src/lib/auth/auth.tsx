import { configureAuth } from 'react-query-auth';

import {
  getUser,
  loginWithUsernameAndPassword,
  logout,
  refreshToken,
  registerWithUsernameAndPassword,
} from '@/lib/auth/hooks';
import { LoginInput, RegisterInput } from '@/lib/auth/types';
import { TokenManagementEntity } from '@/lib/store/auth/token';

export const refreshTokenFn = async (): Promise<string> => {
  const response = await refreshToken();
  TokenManagementEntity.patcher(response.token);
  return response.token;
};

const authConfig = {
  userFn: async () => {
    const token = TokenManagementEntity.selector();
    if (!token) return null;
    return getUser();
  },
  loginFn: async (data: LoginInput) => {
    const response = await loginWithUsernameAndPassword(data);
    TokenManagementEntity.patcher(response.jwt);
    return response.user;
  },
  registerFn: async (data: RegisterInput) => {
    const response = await registerWithUsernameAndPassword(data);
    return response.user;
  },
  logoutFn: async () => {
    await logout();
    TokenManagementEntity.clear();
  },
};

export const { useUser, useLogin, useLogout, useRegister, AuthLoader } =
  configureAuth(authConfig);
