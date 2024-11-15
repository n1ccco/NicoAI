import { HttpResponse, http } from 'msw';

import { env } from '@/config/env';
import { imageBlob } from '@/testing/assets/image-blob';

import { db, persistDb } from '../db';
import {
  requireAuth,
  requireAdmin,
  sanitizeUser,
  networkDelay,
} from '../utils';

type Prompt = {
  id: string;
  prompt: string;
  negativePrompt: string;
  height: string;
  width: string;
  numInterferenceSteps: string;
  guidanceScale: string;
};

export const imagesHandler = [
  http.get(`${env.API_URL}/images`, async ({ request }) => {
    await networkDelay();
    try {
      const { error } = requireAuth(request);
      if (error) {
        return HttpResponse.json({ message: error }, { status: 401 });
      }

      const result = db.image.getAll().map(({ authorId, ...image }) => {
        const author = db.user.findFirst({
          where: {
            id: {
              equals: authorId,
            },
          },
        });
        return {
          ...image,
          author: author ? sanitizeUser(author) : {},
        };
      });
      return HttpResponse.json(result);
    } catch (error: any) {
      return HttpResponse.json(
        { message: error?.message || 'Server Error' },
        { status: 500 },
      );
    }
  }),

  http.get(`${env.API_URL}/images/:imageId`, async ({ params, request }) => {
    await networkDelay();

    try {
      const { error } = requireAuth(request);
      if (error) {
        return HttpResponse.json({ message: error }, { status: 401 });
      }
      const imageId = params.imageId as string;
      const image = db.image.findFirst({
        where: {
          id: {
            equals: imageId,
          },
        },
      });

      if (!image) {
        return HttpResponse.json(
          { message: 'Image not found' },
          { status: 404 },
        );
      }

      const author = db.user.findFirst({
        where: {
          id: {
            equals: image.authorId,
          },
        },
      });

      const result = {
        ...image,
        authorName: author?.username || null,
        authorId: author?.id || null,
      };

      return HttpResponse.json(result);
    } catch (error: any) {
      return HttpResponse.json(
        { message: error?.message || 'Server Error' },
        { status: 500 },
      );
    }
  }),

  http.get(
    `${env.API_URL}/images/:imageId/prompt`,
    async ({ params, request }) => {
      await networkDelay();

      try {
        const { error } = requireAuth(request);
        if (error) {
          return HttpResponse.json({ message: error }, { status: 401 });
        }
        const imageId = params.imageId as string;
        const prompt = db.prompt.findFirst({
          where: {
            imageId: {
              equals: imageId,
            },
          },
        });

        if (!prompt) {
          return HttpResponse.json(
            { message: 'Prompt not found' },
            { status: 404 },
          );
        }

        const result = {
          ...prompt,
        };

        return HttpResponse.json(result);
      } catch (error: any) {
        return HttpResponse.json(
          { message: error?.message || 'Server Error' },
          { status: 500 },
        );
      }
    },
  ),

  http.get(`${env.API_URL}/images/:imageId/blob`, async ({ request }) => {
    await networkDelay();

    try {
      const { error } = requireAuth(request);
      if (error) {
        return HttpResponse.json({ message: error }, { status: 401 });
      }

      const result = {
        imageBlob: imageBlob,
      };

      return HttpResponse.json(result);
    } catch (error: any) {
      return HttpResponse.json(
        { message: error?.message || 'Server Error' },
        { status: 500 },
      );
    }
  }),

  http.post(`${env.API_URL}/images`, async ({ request }) => {
    await networkDelay();

    try {
      const { user, error } = requireAuth(request);
      if (error) {
        return HttpResponse.json({ message: error }, { status: 401 });
      }
      const data = (await request.json()) as Prompt;
      const imageResult = db.image.create({
        authorId: user?.id,
        countLikes: 0,
        isLiked: false,
      });
      await persistDb('image');
      db.prompt.create({
        ...data,
        imageId: imageResult.id,
      });
      await persistDb('prompt');
      return HttpResponse.json({ imageId: imageResult.id });
    } catch (error: any) {
      return HttpResponse.json(
        { message: error?.message || 'Server Error' },
        { status: 500 },
      );
    }
  }),

  http.delete(`${env.API_URL}/images/:imageId`, async ({ request, params }) => {
    await networkDelay();

    try {
      const { user, error } = requireAuth(request);
      if (error) {
        return HttpResponse.json({ message: error }, { status: 401 });
      }
      const imageId = params.imageId as string;

      const image = db.image.findFirst({
        where: {
          id: {
            equals: imageId,
          },
        },
      });

      if (!image) {
        return HttpResponse.json(
          { message: 'Image not found' },
          { status: 404 },
        );
      }

      if (user?.id !== image.authorId) {
        requireAdmin(user);
      }

      const result = db.image.delete({
        where: {
          id: {
            equals: imageId,
          },
        },
      });
      await persistDb('image');
      return HttpResponse.json(result);
    } catch (error: any) {
      return HttpResponse.json(
        { message: error?.message || 'Server Error' },
        { status: 500 },
      );
    }
  }),
];
