export const paths = {
  home: {
    path: '/',
    getHref: () => '/',
  },
  auth: {
    register: {
      path: '/auth/register',
      getHref: (redirectTo?: string | null | undefined) =>
        `/auth/register${redirectTo ? `?redirectTo=${encodeURIComponent(redirectTo)}` : ''}`,
    },
    login: {
      path: '/auth/login',
      getHref: (redirectTo?: string | null | undefined) =>
        `/auth/login${redirectTo ? `?redirectTo=${encodeURIComponent(redirectTo)}` : ''}`,
    },
  },
  app: {
    root: {
      path: '/app',
      getHref: () => '/app',
    },
    dashboard: {
      path: '',
      getHref: () => '/app',
    },
    images: {
      path: 'images',
      getHref: () => '/app/images',
    },
    image: {
      path: 'images/:imageId',
      getHref: (id: string) => `/app/images/${id}`,
    },
    users: {
      path: 'users',
      getHref: () => '/app/users',
    },
    user: {
      path: 'users/:userId',
      getHref: (id: string) => `/app/users/${id}`,
    },
    profile: {
      path: 'profile',
      getHref: () => '/app/profile',
    },
  },
} as const;
