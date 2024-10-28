import { http, HttpResponse } from 'msw';

import { env } from '@/config/env';

import { db, persistDb } from '../db';
import { networkDelay, requireAuth, sanitizeUser } from '../utils';

type CreateCommentBody = {
  body: string;
  imageId: string;
};

export const commentsHandlers = [
  http.get(`${env.API_URL}/comments`, async ({ request }) => {
    await networkDelay();

    try {
      const { error } = requireAuth(request);
      if (error) {
        return HttpResponse.json({ message: error }, { status: 401 });
      }
      const url = new URL(request.url);
      const imageId = url.searchParams.get('imageId') || '';
      const page = Number(url.searchParams.get('page') || 1);

      const total = db.comment.count({
        where: {
          imageId: {
            equals: imageId,
          },
        },
      });

      const totalPages = Math.ceil(total / 10);

      const comments = db.comment
        .findMany({
          where: {
            imageId: {
              equals: imageId,
            },
          },
          take: 10,
          skip: 10 * (page - 1),
        })
        .map(({ authorId, ...comment }) => {
          const author = db.user.findFirst({
            where: {
              id: {
                equals: authorId,
              },
            },
          });
          return {
            ...comment,
            author: author ? sanitizeUser(author) : {},
          };
        });
      return HttpResponse.json({
        data: comments,
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

  http.post(`${env.API_URL}/comments`, async ({ request }) => {
    await networkDelay();

    try {
      const { user, error } = requireAuth(request);
      if (error) {
        return HttpResponse.json({ message: error }, { status: 401 });
      }
      const data = (await request.json()) as CreateCommentBody;
      const result = db.comment.create({
        authorId: user?.id,
        ...data,
      });
      await persistDb('comment');
      return HttpResponse.json(result);
    } catch (error: any) {
      return HttpResponse.json(
        { message: error?.message || 'Server Error' },
        { status: 500 },
      );
    }
  }),

  http.delete(
    `${env.API_URL}/comments/:commentId`,
    async ({ params, request }) => {
      await networkDelay();

      try {
        const { user, error } = requireAuth(request);
        if (error) {
          return HttpResponse.json({ message: error }, { status: 401 });
        }
        const commentId = params.commentId as string;
        const result = db.comment.delete({
          where: {
            id: {
              equals: commentId,
            },
            authorId: {
              equals: user?.id,
            },
          },
        });
        await persistDb('comment');
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
