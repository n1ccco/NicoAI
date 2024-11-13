import { useQuery, queryOptions } from '@tanstack/react-query';

import { api } from '@/shared/lib/api-client';
import { QueryConfig } from '@/shared/lib/react-query';
import { Discussion } from '@/shared/types/api';

export const getDiscussion = ({
  discussionId,
}: {
  discussionId: string;
}): Promise<{ data: Discussion }> => {
  return api.get(`/discussions/${discussionId}`);
};

export const getDiscussionQueryOptions = (discussionId: string) => {
  return queryOptions({
    queryKey: ['discussions', discussionId],
    queryFn: () => getDiscussion({ discussionId }),
  });
};

type UseDiscussionOptions = {
  discussionId: string;
  queryConfig?: QueryConfig<typeof getDiscussionQueryOptions>;
};

export const useDiscussion = ({
  discussionId,
  queryConfig,
}: UseDiscussionOptions) => {
  return useQuery({
    ...getDiscussionQueryOptions(discussionId),
    ...queryConfig,
  });
};
