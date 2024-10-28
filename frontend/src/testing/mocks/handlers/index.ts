import { HttpResponse, http } from 'msw';

import { env } from '@/config/env';

import { networkDelay } from '../utils';

import { authHandlers } from './auth';
import { commentsHandlers } from './comments';
import { discussionsHandlers } from './discussions';
import { usersHandlers } from './users';

export const handlers = [
  ...authHandlers,
  ...commentsHandlers,
  ...discussionsHandlers,
  ...usersHandlers,
  http.get(`${env.API_URL}/healthcheck`, async () => {
    await networkDelay();
    return HttpResponse.json({ ok: true });
  }),
];
