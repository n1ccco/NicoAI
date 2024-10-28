import { useMutation, useQueryClient } from '@tanstack/react-query';

import { api } from '@/shared/lib/api-client';
import { MutationConfig } from '@/shared/lib/react-query';
import { ImageDetailed } from '@/shared/types/api';

export const deleteImage = ({
  imageId,
}: {
  imageId: string;
}): Promise<{ data: ImageDetailed }> => {
  return api.delete(`/images/${imageId}`);
};

type UseDeleteImageOptions = {
  mutationConfig?: MutationConfig<typeof deleteImage>;
};

export const useDeleteImage = ({
  mutationConfig,
}: UseDeleteImageOptions = {}) => {
  const queryClient = useQueryClient();

  const { onSuccess, ...restConfig } = mutationConfig || {};

  return useMutation({
    onSuccess: (...args) => {
      queryClient.invalidateQueries({
        queryKey: ['images'],
      });
      onSuccess?.(...args);
    },
    ...restConfig,
    mutationFn: deleteImage,
  });
};
