import { InternalAxiosRequestConfig } from 'axios';

import { paths } from '@/config/paths';
import { useNotifications } from '@/shared/components/ui/notifications';
import { addAuthToken } from '@/shared/lib/auth/token-utils';
import { TokenManagementEntity } from '@/shared/lib/store/auth/token';

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
    const redirectTo =
      searchParams.get('redirectTo') || window.location.pathname;
    if (
      !window.location.pathname.includes(paths.auth.login.getHref(redirectTo))
    ) {
      window.location.href = paths.auth.login.getHref(redirectTo);
    }
    TokenManagementEntity.clear();
  }

  return Promise.reject(error);
};
