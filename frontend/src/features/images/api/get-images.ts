import { queryOptions, useQuery } from '@tanstack/react-query';

import { api } from '@/lib/api-client';
import { QueryConfig } from '@/lib/react-query';
import { ImageSimplified } from '@/types/api';

export const getImages = (
  { sortBy = 'date' }: { sortBy: 'date' | 'likes' },
  { sortDirection = 'asc' }: { sortDirection: 'asc' | 'desc' },
): Promise<ImageSimplified[]> => {
  return api.get(`/images`, {
    params: {
      sortBy: sortBy,
      order: sortDirection,
    },
  });
};

export const getImagesQueryOptions = (
  { sortBy = 'date' }: { sortBy: 'date' | 'likes' },
  { sortDirection = 'asc' }: { sortDirection: 'asc' | 'desc' },
) => {
  return queryOptions({
    queryKey: ['images', sortDirection, sortBy],
    queryFn: () => getImages({ sortBy }, { sortDirection }),
  });
};

type UseImageOptions = {
  sortBy: 'date' | 'likes';
  sortDirection: 'asc' | 'desc';
  queryConfig?: QueryConfig<typeof getImagesQueryOptions>;
};

export const useImages = ({
  sortBy,
  sortDirection,
  queryConfig,
}: UseImageOptions) => {
  return useQuery({
    ...getImagesQueryOptions({ sortBy }, { sortDirection }),
    ...queryConfig,
  });
};
