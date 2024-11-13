import { QueryClient, useQueryClient } from '@tanstack/react-query';
import { useMemo } from 'react';
import {
  createBrowserRouter,
  LoaderFunctionArgs,
  RouterProvider,
} from 'react-router-dom';

import { ProtectedRoute } from '@/shared/lib/auth/protected-route';

import { AppRoot } from './routes/app/root';

export const createAppRouter = (queryClient: QueryClient) =>
  createBrowserRouter([
    {
      path: '/',
      lazy: async () => {
        const { LandingRoute } = await import('./routes/landing');
        return { Component: LandingRoute };
      },
    },
    {
      path: '/auth/register',
      lazy: async () => {
        const { RegisterRoute } = await import('./routes/auth/register');
        return { Component: RegisterRoute };
      },
    },
    {
      path: '/auth/login',
      lazy: async () => {
        const { LoginRoute } = await import('./routes/auth/login');
        return { Component: LoginRoute };
      },
    },
    {
      path: '/app',
      element: (
        <ProtectedRoute>
          <AppRoot />
        </ProtectedRoute>
      ),
      children: [
        {
          path: 'discussions',
          lazy: async () => {
            const { DiscussionsRoute } = await import(
              './routes/app/discussions/discussions'
            );
            return { Component: DiscussionsRoute };
          },
          loader: async (args: LoaderFunctionArgs) => {
            const { discussionsLoader } = await import(
              './routes/app/discussions/discussions'
            );
            return discussionsLoader(queryClient)(args);
          },
        },
        {
          path: 'discussions/:discussionId',
          lazy: async () => {
            const { DiscussionRoute } = await import(
              './routes/app/discussions/discussion'
            );
            return { Component: DiscussionRoute };
          },

          loader: async (args: LoaderFunctionArgs) => {
            const { discussionLoader } = await import(
              './routes/app/discussions/discussion'
            );
            return discussionLoader(queryClient)(args);
          },
        },
        {
          path: 'users',
          lazy: async () => {
            const { UsersRoute } = await import('./routes/app/users');
            return { Component: UsersRoute };
          },

          loader: async () => {
            const { usersLoader } = await import('./routes/app/users');
            return usersLoader(queryClient)();
          },
        },
        {
          path: 'profile',
          lazy: async () => {
            const { ProfileRoute } = await import('./routes/app/profile');
            return { Component: ProfileRoute };
          },
        },
        {
          path: 'images',
          lazy: async () => {
            const { ImagesRoute } = await import('./routes/app/images/images');
            return { Component: ImagesRoute };
          },
          loader: async (args: LoaderFunctionArgs) => {
            const { imagesLoader } = await import('./routes/app/images/images');
            return imagesLoader(queryClient)(args);
          },
        },
        {
          path: 'users/:userId/images',
          lazy: async () => {
            const { ImagesRoute } = await import('./routes/app/images/images');
            return { Component: ImagesRoute };
          },
          loader: async (args: LoaderFunctionArgs) => {
            const { imagesLoader } = await import('./routes/app/images/images');
            return imagesLoader(queryClient)(args);
          },
        },
        {
          path: 'images/:imageId',
          lazy: async () => {
            const { ImageRoute } = await import('./routes/app/images/image');
            return { Component: ImageRoute };
          },
          loader: async (args: LoaderFunctionArgs) => {
            const { imageLoader } = await import('./routes/app/images/image');
            return imageLoader(queryClient)(args);
          },
        },
        {
          path: '',
          lazy: async () => {
            const { DashboardRoute } = await import('./routes/app/dashboard');
            return { Component: DashboardRoute };
          },
        },
      ],
    },
    {
      path: '*',
      lazy: async () => {
        const { NotFoundRoute } = await import('./routes/not-found');
        return { Component: NotFoundRoute };
      },
    },
  ]);

export const AppRouter = () => {
  const queryClient = useQueryClient();

  const router = useMemo(() => createAppRouter(queryClient), [queryClient]);

  return <RouterProvider router={router} />;
};
