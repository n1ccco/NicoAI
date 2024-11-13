import { infiniteQueryOptions, useInfiniteQuery } from '@tanstack/react-query';

import { api } from '@/shared/lib/api-client';
import { QueryConfig } from '@/shared/lib/react-query';
import { Comment, Meta } from '@/shared/types/api';

export const getComments = ({
  imageId,
  page = 1,
}: {
  imageId: string;
  page?: number;
}): Promise<{ data: Comment[]; meta: Meta }> => {
  return api.get(`/comments`, {
    params: {
      imageId,
      page,
    },
  });
};

export const getInfiniteCommentsQueryOptions = (imageId: string) => {
  return infiniteQueryOptions({
    queryKey: ['comments', imageId],
    queryFn: ({ pageParam = 1 }) => {
      return getComments({ imageId, page: pageParam as number });
    },
    getNextPageParam: (lastPage) => {
      if (lastPage?.meta?.page === lastPage?.meta?.totalPages) return undefined;
      return lastPage.meta.page + 1;
    },
    initialPageParam: 1,
  });
};

type UseCommentsOptions = {
  imageId: string;
  page?: number;
  queryConfig?: QueryConfig<typeof getComments>;
};

export const useInfiniteComments = ({ imageId }: UseCommentsOptions) => {
  return useInfiniteQuery({
    ...getInfiniteCommentsQueryOptions(imageId),
  });
};
