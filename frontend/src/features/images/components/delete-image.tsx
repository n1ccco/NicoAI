import { Trash } from 'lucide-react';

import { Button } from '@/components/ui/button';
import { ConfirmationDialog } from '@/components/ui/dialog';
import { useNotifications } from '@/components/ui/notifications';
import { useDeleteImage } from '@/features/images/api/delete-image';

type DeleteImageProps = {
  id: string;
};

export const DeleteImage = ({ id }: DeleteImageProps) => {
  const { addNotification } = useNotifications();
  const deleteImageMutation = useDeleteImage({
    mutationConfig: {
      onSuccess: () => {
        addNotification({
          type: 'success',
          title: 'Image Deleted',
        });
      },
    },
  });

  return (
    <ConfirmationDialog
      icon="danger"
      title="Delete Image"
      body="Are you sure you want to delete this image?"
      triggerButton={
        <Button variant="destructive" icon={<Trash className="size-4" />}>
          Delete Image
        </Button>
      }
      confirmButton={
        <Button
          isLoading={deleteImageMutation.isPending}
          type="button"
          variant="destructive"
          onClick={() => deleteImageMutation.mutate({ imageId: id })}
        >
          Delete Comment
        </Button>
      }
    />
  );
};
