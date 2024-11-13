import Axios from 'axios';

import { env } from '@/config/env';
import {
  authRequestInterceptor,
  handleResponseError,
} from '@/shared/lib/auth/interceptors';

export const api = Axios.create({
  baseURL: env.API_URL,
});

api.interceptors.request.use(authRequestInterceptor);
api.interceptors.response.use(
  (response) => response.data,
  (error) => handleResponseError(error),
);
