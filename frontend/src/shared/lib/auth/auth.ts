import { configureAuth } from 'react-query-auth';

import {
  getUser,
  loginWithUsernameAndPassword,
  logout,
  registerWithUsernameAndPassword,
} from '@/shared/lib/auth/auth-api';
import { LoginInput, RegisterInput } from '@/shared/lib/auth/types';
import { TokenManagementEntity } from '@/shared/lib/store/auth/token';

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
