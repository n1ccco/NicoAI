import { HttpResponse, http } from 'msw';

import { env } from '@/config/env';

import { db, persistDb } from '../db';
import {
  requireAuth,
  requireAdmin,
  sanitizeUser,
  networkDelay,
} from '../utils';

type DiscussionBody = {
  title: string;
  body: string;
};

export const discussionsHandlers = [
  http.get(`${env.API_URL}/discussions`, async ({ cookies, request }) => {
    await networkDelay();

    try {
      const { error } = requireAuth(cookies);
      if (error) {
        return HttpResponse.json({ message: error }, { status: 401 });
      }

      const url = new URL(request.url);

      const page = Number(url.searchParams.get('page') || 1);

      const total = db.discussion.count();

      const totalPages = Math.ceil(total / 10);

      const result = db.discussion
        .findMany({
          where: {},
          take: 10,
          skip: 10 * (page - 1),
        })
        .map(({ authorId, ...discussion }) => {
          const author = db.user.findFirst({
            where: {
              id: {
                equals: authorId,
              },
            },
          });
          return {
            ...discussion,
            author: author ? sanitizeUser(author) : {},
          };
        });
      return HttpResponse.json({
        data: result,
        meta: {
          page,
          total,
          totalPages,
        },
      });
    } catch (error: any) {
      return HttpResponse.json(
        { message: error?.message || 'Server Error' },
        { status: 500 },
      );
    }
  }),

  http.get(
    `${env.API_URL}/discussions/:discussionId`,
    async ({ params, cookies }) => {
      await networkDelay();

      try {
        const { error } = requireAuth(cookies);
        if (error) {
          return HttpResponse.json({ message: error }, { status: 401 });
        }
        const discussionId = params.discussionId as string;
        const discussion = db.discussion.findFirst({
          where: {
            id: {
              equals: discussionId,
            },
          },
        });

        if (!discussion) {
          return HttpResponse.json(
            { message: 'Discussion not found' },
            { status: 404 },
          );
        }

        const author = db.user.findFirst({
          where: {
            id: {
              equals: discussion.authorId,
            },
          },
        });

        const result = {
          ...discussion,
          author: author ? sanitizeUser(author) : {},
        };

        return HttpResponse.json({ data: result });
      } catch (error: any) {
        return HttpResponse.json(
          { message: error?.message || 'Server Error' },
          { status: 500 },
        );
      }
    },
  ),

  http.post(`${env.API_URL}/discussions`, async ({ request, cookies }) => {
    await networkDelay();

    try {
      const { user, error } = requireAuth(cookies);
      if (error) {
        return HttpResponse.json({ message: error }, { status: 401 });
      }
      const data = (await request.json()) as DiscussionBody;
      const result = db.discussion.create({
        authorId: user?.id,
        ...data,
      });
      await persistDb('discussion');
      return HttpResponse.json(result);
    } catch (error: any) {
      return HttpResponse.json(
        { message: error?.message || 'Server Error' },
        { status: 500 },
      );
    }
  }),

  http.patch(
    `${env.API_URL}/discussions/:discussionId`,
    async ({ request, params, cookies }) => {
      await networkDelay();

      try {
        const { user, error } = requireAuth(cookies);
        if (error) {
          return HttpResponse.json({ message: error }, { status: 401 });
        }
        const data = (await request.json()) as DiscussionBody;
        const discussionId = params.discussionId as string;
        let result = db.discussion.update({
          where: {
            id: {
              equals: discussionId,
            },
            authorId: {
              equals: user?.id,
            },
          },
          data,
        });

        if (!result && user?.role === 'ADMIN') {
          result = db.discussion.update({
            where: {
              id: {
                equals: discussionId,
              },
            },
            data,
          });
        }
        await persistDb('discussion');
        return HttpResponse.json(result);
      } catch (error: any) {
        return HttpResponse.json(
          { message: error?.message || 'Server Error' },
          { status: 500 },
        );
      }
    },
  ),

  http.delete(
    `${env.API_URL}/discussions/:discussionId`,
    async ({ cookies, params }) => {
      await networkDelay();

      try {
        const { user, error } = requireAuth(cookies);
        if (error) {
          return HttpResponse.json({ message: error }, { status: 401 });
        }
        const discussionId = params.discussionId as string;
        requireAdmin(user);
        const result = db.discussion.delete({
          where: {
            id: {
              equals: discussionId,
            },
          },
        });
        await persistDb('discussion');
        return HttpResponse.json(result);
      } catch (error: any) {
        return HttpResponse.json(
          { message: error?.message || 'Server Error' },
          { status: 500 },
        );
      }
    },
  ),
];
