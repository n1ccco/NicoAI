import { Trash } from 'lucide-react';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

import { useDeleteImage } from '@/features/images/api/delete-image';
import { Button } from '@/shared/components/ui/button';
import { ConfirmationDialog } from '@/shared/components/ui/dialog';
import { useNotifications } from '@/shared/components/ui/notifications';

type DeleteImageProps = {
  id: string;
};

export const DeleteImage = ({ id }: DeleteImageProps) => {
  const { addNotification } = useNotifications();
  const navigate = useNavigate();
  const [isDone, setIsDone] = useState(false);
  const deleteImageMutation = useDeleteImage({
    mutationConfig: {
      onSuccess: () => {
        addNotification({
          type: 'success',
          title: 'Image Deleted',
        });
        setIsDone(true);
        navigate('/app/images');
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
          Delete image
        </Button>
      }
      isDone={isDone}
    />
  );
};
