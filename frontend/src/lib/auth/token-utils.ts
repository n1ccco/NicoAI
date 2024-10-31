import { InternalAxiosRequestConfig } from 'axios';
import { jwtDecode } from 'jwt-decode';

import { useNotifications } from '@/components/ui/notifications';
import { apiWithCredentials } from '@/lib/api-credentials';
import { refreshTokenFn } from '@/lib/auth/auth';
import { TokenManagementEntity } from '@/lib/store/auth/token';

const isTokenExpired = (token: string) => {
  if (!token) return true;

  try {
    const { exp } = jwtDecode(token);
    if (!exp) return true;
    const currentTime = Date.now() / 1000;
    return exp < currentTime;
  } catch (_err: any) {
    return true;
  }
};

export const addAuthToken = async (config: InternalAxiosRequestConfig) => {
  let token = TokenManagementEntity.selector();
  if (token) {
    if (isTokenExpired(token)) {
      try {
        token = await refreshTokenFn();
      } catch (_err: any) {
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
