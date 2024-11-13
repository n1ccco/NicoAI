import { useMutation, useQueryClient } from '@tanstack/react-query';

import { api } from '@/shared/lib/api-client';
import { MutationConfig } from '@/shared/lib/react-query';

export const toggleLike = ({
  entityType,
  entityId,
}: {
  entityType: string;
  entityId: string;
}): Promise<void> => {
  return api.post(`/${entityType}/${entityId}/like`);
};

type UseToggleLikeOptions = {
  mutationConfig?: MutationConfig<typeof toggleLike>;
};

export const useToggleLike = ({
  mutationConfig,
}: UseToggleLikeOptions = {}) => {
  const queryClient = useQueryClient();

  const { onSuccess, ...restConfig } = mutationConfig || {};

  return useMutation({
    onSuccess: (...args) => {
      queryClient.invalidateQueries({
        queryKey: ['images'],
      });
      queryClient.invalidateQueries({
        queryKey: ['imageDetails'],
      });
      onSuccess?.(...args);
    },
    mutationFn: toggleLike,
    ...restConfig,
  });
};
