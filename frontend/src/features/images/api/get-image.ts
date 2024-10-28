import { queryOptions, useQuery } from '@tanstack/react-query';

import { api } from '@/shared/lib/api-client';
import { QueryConfig } from '@/shared/lib/react-query';
import { ImageDetailed } from '@/shared/types/api';

export const getImage = ({
  imageId,
}: {
  imageId: string;
}): Promise<ImageDetailed> => {
  return api.get(`/images/${imageId}`);
};

export const getImageQueryOptions = (imageId: string) => {
  return queryOptions({
    queryKey: ['imageDetails', imageId],
    queryFn: () => getImage({ imageId }),
  });
};

type UseImageOptions = {
  imageId: string;
  queryConfig?: QueryConfig<typeof getImageQueryOptions>;
};

export const useImage = ({ imageId, queryConfig }: UseImageOptions) => {
  return useQuery({
    ...getImageQueryOptions(imageId),
    ...queryConfig,
  });
};
