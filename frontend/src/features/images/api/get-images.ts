import { infiniteQueryOptions, useInfiniteQuery } from '@tanstack/react-query';

import { api } from '@/shared/lib/api-client';
import { QueryConfig } from '@/shared/lib/react-query';
import { ImageSimplified, Meta } from '@/shared/types/api';

export const getImages = ({
  sortBy = 'date',
  sortDirection = 'asc',
  userId,
  page = 1,
}: {
  sortBy: 'date' | 'likes';
  sortDirection: 'asc' | 'desc';
  userId?: string;
  page?: number;
}): Promise<{ data: ImageSimplified[]; meta: Meta }> => {
  return api.get(`/images`, {
    params: {
      sortBy: sortBy,
      sortDirection: sortDirection,
      userId,
      page,
    },
  });
};

export const getInfiniteImagesQueryOptions = ({
  sortBy = 'date',
  sortDirection = 'asc',
  userId,
}: {
  sortBy: 'date' | 'likes';
  sortDirection: 'asc' | 'desc';
  userId?: string;
}) => {
  return infiniteQueryOptions({
    queryKey: ['images', sortDirection, sortBy, userId],
    queryFn: ({ pageParam = 1 }) =>
      getImages({ sortBy, sortDirection, userId, page: pageParam as number }),
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
  queryConfig?: QueryConfig<typeof getImages>;
};

export const useInfiniteImages = ({
  sortBy,
  sortDirection,
  userId,
}: UseImageOptions) => {
  return useInfiniteQuery({
    ...getInfiniteImagesQueryOptions({ sortBy, sortDirection, userId }),
  });
};
