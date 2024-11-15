import { http, HttpResponse } from 'msw';

import { env } from '@/config/env';

import { db, persistDb } from '../db';
import {
  networkDelay,
  requireAdmin,
  requireAuth,
  sanitizeUser,
} from '../utils';

type ProfileBody = {
  username: string;
};

export const usersHandlers = [
  http.get(`${env.API_URL}/users`, async ({ request }) => {
    await networkDelay();

    try {
      const { error } = requireAuth(request);
      if (error) {
        return HttpResponse.json({ message: error }, { status: 401 });
      }
      const result = db.user.findMany({}).map(sanitizeUser);

      return HttpResponse.json({ data: result });
    } catch (error: any) {
      return HttpResponse.json(
        { message: error?.message || 'Server Error' },
        { status: 500 },
      );
    }
  }),

  http.patch(`${env.API_URL}/users/profile`, async ({ request, cookies }) => {
    await networkDelay();

    try {
      const { user, error } = requireAuth(cookies);
      if (error) {
        return HttpResponse.json({ message: error }, { status: 401 });
      }
      const data = (await request.json()) as ProfileBody;
      const result = db.user.update({
        where: {
          id: {
            equals: user?.id,
          },
        },
        data,
      });
      await persistDb('user');
      return HttpResponse.json(result);
    } catch (error: any) {
      return HttpResponse.json(
        { message: error?.message || 'Server Error' },
        { status: 500 },
      );
    }
  }),

  http.delete(`${env.API_URL}/users/:userId`, async ({ cookies, params }) => {
    await networkDelay();

    try {
      const { user, error } = requireAuth(cookies);
      if (error) {
        return HttpResponse.json({ message: error }, { status: 401 });
      }
      const userId = params.userId as string;
      requireAdmin(user);
      const result = db.user.delete({
        where: {
          id: {
            equals: userId,
          },
        },
      });
      await persistDb('user');
      return HttpResponse.json(result);
    } catch (error: any) {
      return HttpResponse.json(
        { message: error?.message || 'Server Error' },
        { status: 500 },
      );
    }
  }),
];
