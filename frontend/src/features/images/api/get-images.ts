import { queryOptions, useQuery } from '@tanstack/react-query';

import { api } from '@/lib/api-client';
import { QueryConfig } from '@/lib/react-query';
import { ImageSimplified } from '@/types/api';

export const getImages = (
  { sortBy = 'date' }: { sortBy: 'date' | 'likes' },
  { sortDirection = 'asc' }: { sortDirection: 'asc' | 'desc' },
  { userId }: { userId?: string },
): Promise<ImageSimplified[]> => {
  return api.get(`/images`, {
    params: {
      sortBy: sortBy,
      order: sortDirection,
      ...(userId && { userId }),
    },
  });
};

export const getImagesQueryOptions = (
  { sortBy = 'date' }: { sortBy: 'date' | 'likes' },
  { sortDirection = 'asc' }: { sortDirection: 'asc' | 'desc' },
  { userId }: { userId?: string },
) => {
  return queryOptions({
    queryKey: ['images', sortDirection, sortBy, userId],
    queryFn: () => getImages({ sortBy }, { sortDirection }, { userId }),
  });
};

type UseImageOptions = {
  sortBy: 'date' | 'likes';
  sortDirection: 'asc' | 'desc';
  userId?: string;
  queryConfig?: QueryConfig<typeof getImagesQueryOptions>;
};

export const useImages = ({
  sortBy,
  sortDirection,
  userId,
  queryConfig,
}: UseImageOptions) => {
  return useQuery({
    ...getImagesQueryOptions({ sortBy }, { sortDirection }, { userId }),
    ...queryConfig,
  });
};
