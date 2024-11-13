import { queryOptions, useQuery } from '@tanstack/react-query';

import { api } from '@/shared/lib/api-client';
import { QueryConfig } from '@/shared/lib/react-query';
import { ImageBlob } from '@/shared/types/api';

export const getImageBlob = ({
  imageId,
}: {
  imageId: string;
}): Promise<ImageBlob> => {
  return api.get(`/images/${imageId}/blob`);
};

export const getImageBlobQueryOptions = (imageId: string) => {
  return queryOptions({
    queryKey: ['imageBlobs', imageId],
    queryFn: () => getImageBlob({ imageId }),
  });
};

type UseImageBlobOptions = {
  imageId: string;
  queryConfig?: QueryConfig<typeof getImageBlobQueryOptions>;
};

export const useImageBlob = ({ imageId, queryConfig }: UseImageBlobOptions) => {
  return useQuery({
    ...getImageBlobQueryOptions(imageId),
    ...queryConfig,
  });
};
