import { infiniteQueryOptions, useInfiniteQuery } from '@tanstack/react-query';

import { api } from '@/shared/lib/api-client';
import { ImageSimplified, Meta } from '@/shared/types/api';

export const searchImages = ({
  keyword,
  page = 1,
}: {
  keyword: string;
  page?: number;
}): Promise<{ data: ImageSimplified[]; meta: Meta }> => {
  return api.get(`/images/search`, { params: { keyword, page } });
};

export const getInfiniteSearchImagesQueryOptions = (keyword: string) => {
  return infiniteQueryOptions({
    queryKey: ['search', keyword],
    queryFn: ({ pageParam = 1 }) =>
      searchImages({ keyword, page: pageParam as number }),
    getNextPageParam: (lastPage) => {
      if (lastPage?.meta?.page === lastPage?.meta?.totalPages) return undefined;
      return lastPage.meta.page + 1;
    },
    initialPageParam: 1,
    enabled: !!keyword,
  });
};

export const useInfiniteSearchImages = (keyword: string) => {
  return useInfiniteQuery({
    ...getInfiniteSearchImagesQueryOptions(keyword),
  });
};
