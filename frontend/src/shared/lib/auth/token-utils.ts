import { InternalAxiosRequestConfig } from 'axios';
import { jwtDecode } from 'jwt-decode';

import { useNotifications } from '@/shared/components/ui/notifications';
import { apiWithCredentials } from '@/shared/lib/api-credentials';
import { TokenManagementEntity } from '@/shared/lib/store/auth/token';
import { JwtRefreshResponse } from '@/shared/types/api';

export const refreshToken = (): Promise<JwtRefreshResponse> => {
  return apiWithCredentials.post('/auth/refresh');
};

export const refreshTokenFn = async (): Promise<string> => {
  const response = await refreshToken();
  TokenManagementEntity.patcher(response.token);
  return response.token;
};

const isTokenExpired = (token: string) => {
  if (!token) return true;

  try {
    const { exp } = jwtDecode(token);
    if (!exp) return true;
    const currentTime = Date.now() / 1000;
    return exp < currentTime;
  } catch {
    return true;
  }
};

export const addAuthToken = async (config: InternalAxiosRequestConfig) => {
  let token = TokenManagementEntity.selector();
  if (token) {
    if (isTokenExpired(token)) {
      try {
        token = await refreshTokenFn();
      } catch {
        await apiWithCredentials.post('/auth/logout');
        useNotifications.getState().addNotification({
          type: 'error',
          title: 'Session expired',
          message: 'Your session has expired. Please log in to continue.',
        });
        return;
      }
    }
    config.headers.Authorization = `Bearer ${token}`;
  }
};
