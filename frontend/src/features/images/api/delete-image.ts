import { useMutation, useQueryClient } from '@tanstack/react-query';

import { api } from '@/lib/api-client';
import { MutationConfig } from '@/lib/react-query';
import { ImageDetailed } from '@/types/api';

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
