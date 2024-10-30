import Axios, { InternalAxiosRequestConfig } from 'axios';

import { useNotifications } from '@/components/ui/notifications';
import { env } from '@/config/env';
import { TokenManagementEntity } from '@/lib/store/auth/token';

const addAuthToken = (config: InternalAxiosRequestConfig) => {
  const token = TokenManagementEntity.selector();
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
};

const authRequestInterceptor = (config: InternalAxiosRequestConfig) => {
  if (config.headers) {
    config.headers.Accept = 'application/json';
  }
  addAuthToken(config);
  return config;
};

const handleResponseError = (error: any) => {
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

export const api = Axios.create({
  baseURL: env.API_URL,
});

export const apiWithCredentials = Axios.create({
  ...api.defaults,
  withCredentials: true,
});

api.interceptors.request.use(authRequestInterceptor);
api.interceptors.response.use(
  (response) => response.data,
  (error) => handleResponseError(error),
);
