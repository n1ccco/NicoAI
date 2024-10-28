import { queryOptions, useQuery } from '@tanstack/react-query';

import { api } from '@/shared/lib/api-client';
import { QueryConfig } from '@/shared/lib/react-query';
import { Prompt } from '@/shared/types/api';

export const getImagePrompt = ({
  imageId,
}: {
  imageId: string;
}): Promise<Prompt> => {
  return api.get(`/images/${imageId}/prompt`);
};

export const getImagePromptQueryOptions = (imageId: string) => {
  return queryOptions({
    queryKey: ['imagePrompt', imageId],
    queryFn: () => getImagePrompt({ imageId }),
  });
};

type UseImagePromptOptions = {
  imageId: string;
  queryConfig?: QueryConfig<typeof getImagePromptQueryOptions>;
};

export const useImagePrompt = ({
  imageId,
  queryConfig,
}: UseImagePromptOptions) => {
  return useQuery({
    ...getImagePromptQueryOptions(imageId),
    ...queryConfig,
  });
};
