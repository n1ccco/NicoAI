import { InternalAxiosRequestConfig } from 'axios';

import { useNotifications } from '@/components/ui/notifications';
import { addAuthToken } from '@/lib/auth/token-utils';
import { TokenManagementEntity } from '@/lib/store/auth/token';

export const authRequestInterceptor = async (
  config: InternalAxiosRequestConfig,
) => {
  if (config.headers) {
    config.headers.Accept = 'application/json';
  }
  await addAuthToken(config);
  return config;
};

export const handleResponseError = (error: any) => {
  const message = error.response?.data?.message || error.message;
  useNotifications.getState().addNotification({
    type: 'error',
    title: 'Error',
    message,
  });

  if (error.response?.status === 401) {
    const searchParams = new URLSearchParams();
    const redirectTo = searchParams.get('redirectTo') || '/';
    if (!window.location.pathname.includes('/auth/login')) {
      window.location.href = `/auth/login?redirectTo=${encodeURIComponent(redirectTo)}`;
    }
    TokenManagementEntity.clear();
  }

  return Promise.reject(error);
};
