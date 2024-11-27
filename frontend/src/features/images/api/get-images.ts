import { infiniteQueryOptions, useInfiniteQuery } from '@tanstack/react-query';

import { api } from '@/shared/lib/api-client';
import { QueryConfig } from '@/shared/lib/react-query';
import { ImageSimplified, Meta } from '@/shared/types/api';

export const getImages = ({
  sortBy = 'date',
  sortDirection = 'asc',
  userId,
  page = 1,
  keyword,
}: {
  sortBy: 'date' | 'likes';
  sortDirection: 'asc' | 'desc';
  userId?: string;
  page?: number;
  keyword?: string;
}): Promise<{ data: ImageSimplified[]; meta: Meta }> => {
  return api.get(`/images`, {
    params: {
      sortBy: sortBy,
      sortDirection: sortDirection,
      userId,
      page,
      keyword,
    },
  });
};

export const getInfiniteImagesQueryOptions = ({
  sortBy = 'date',
  sortDirection = 'asc',
  userId,
  keyword,
}: {
  sortBy: 'date' | 'likes';
  sortDirection: 'asc' | 'desc';
  userId?: string;
  keyword?: string;
}) => {
  return infiniteQueryOptions({
    queryKey: ['images', sortDirection, sortBy, userId, keyword],
    queryFn: ({ pageParam = 1 }) =>
      getImages({
        sortBy,
        sortDirection,
        userId,
        page: pageParam as number,
        keyword,
      }),
    getNextPageParam: (lastPage) => {
      if (lastPage?.meta?.page === lastPage?.meta?.totalPages) return undefined;
      return lastPage.meta.page + 1;
    },
    initialPageParam: 1,
  });
};

type UseImageOptions = {
  sortBy: 'date' | 'likes';
  sortDirection: 'asc' | 'desc';
  userId?: string;
  page?: number;
  keyword?: string;
  queryConfig?: QueryConfig<typeof getImages>;
};

export const useInfiniteImages = ({
  sortBy,
  sortDirection,
  userId,
  keyword,
}: UseImageOptions) => {
  return useInfiniteQuery({
    ...getInfiniteImagesQueryOptions({
      sortBy,
      sortDirection,
      userId,
      keyword,
    }),
  });
};
