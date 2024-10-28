import { useMutation, useQueryClient } from '@tanstack/react-query';

import { api } from '@/shared/lib/api-client';
import { MutationConfig } from '@/shared/lib/react-query';
import { ImageDetailed } from '@/shared/types/api';

export const changeImageVisibility = ({
  imageId,
}: {
  imageId: string;
}): Promise<ImageDetailed> => {
  return api.patch(`/images/${imageId}/visibility`);
};

type UseChangeImageVisibilityOptions = {
  mutationConfig?: MutationConfig<typeof changeImageVisibility>;
};

export const useChangeImageVisibility = ({
  mutationConfig,
}: UseChangeImageVisibilityOptions = {}) => {
  const queryClient = useQueryClient();

  const { onSuccess, ...restConfig } = mutationConfig || {};

  return useMutation({
    onSuccess: (...args) => {
      queryClient.invalidateQueries({ queryKey: ['images'] });
      queryClient.invalidateQueries({
        queryKey: ['imageDetails', args[1].imageId],
      });
      onSuccess?.(...args);
    },
    mutationFn: changeImageVisibility,
    ...restConfig,
  });
};
