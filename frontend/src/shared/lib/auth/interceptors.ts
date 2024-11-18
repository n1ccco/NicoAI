import { InternalAxiosRequestConfig } from 'axios';

import { useNotifications } from '@/shared/components/ui/notifications';
import { addAuthToken } from '@/shared/lib/auth/token-utils';

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
  const message = error.response?.data || error.message;
  useNotifications.getState().addNotification({
    type: 'error',
    title: 'Error',
    message,
  });

  return Promise.reject(error);
};
