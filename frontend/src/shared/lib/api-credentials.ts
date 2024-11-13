import Axios from 'axios';

import { env } from '@/config/env';

export const apiWithCredentials = Axios.create({
  baseURL: env.API_URL,
  withCredentials: true,
});
apiWithCredentials.interceptors.response.use((response) => response.data);
