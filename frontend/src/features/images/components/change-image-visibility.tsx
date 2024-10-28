import { Button } from '@/shared/components/ui/button';
import { useNotifications } from '@/shared/components/ui/notifications';

import { useChangeImageVisibility } from '../api/change-image-visibility';

type ChangeImageVisibilityProps = {
  imageId: string;
  isVisible: boolean;
};

export const ChangeImageVisibility = ({
  imageId,
  isVisible,
}: ChangeImageVisibilityProps) => {
  const { addNotification } = useNotifications();
  const changeImageVisibilityMutation = useChangeImageVisibility({
    mutationConfig: {
      onSuccess: () => {
        addNotification({
          type: 'success',
          title: !isVisible ? 'Image is now visible!' : 'Image is now hidden!',
        });
      },
    },
  });

  return (
    <Button
      onClick={() => changeImageVisibilityMutation.mutate({ imageId })}
      isLoading={changeImageVisibilityMutation.isPending}
      variant="outline"
    >
      {isVisible ? 'Make Hidden' : 'Make Visible'}
    </Button>
  );
};
