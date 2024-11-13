import { Button } from '@/shared/components/ui/button';
import { Like } from '@/shared/components/ui/like';
import { useNotifications } from '@/shared/components/ui/notifications';
import { useToggleLike } from '@/shared/features/reactions/api/toggle-like';

type LikeButtonProps = {
  entityId: string;
  entityType: string;
  liked: boolean;
  likeCount: number;
};

export const LikeButton = ({
  entityId,
  entityType,
  liked,
  likeCount,
}: LikeButtonProps) => {
  const { addNotification } = useNotifications();
  const toggleLikeMutation = useToggleLike({
    mutationConfig: {
      onSuccess: () => {
        addNotification({
          type: 'success',
          title: !liked ? 'Liked!' : 'Like removed',
        });
      },
    },
  });

  return (
    <Button
      size="sm"
      onClick={() => toggleLikeMutation.mutate({ entityType, entityId })}
      isLoading={toggleLikeMutation.isPending}
      icon={<Like liked={liked} size={24} />}
    >
      {likeCount}
    </Button>
  );
};
