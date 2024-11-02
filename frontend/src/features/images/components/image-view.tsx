import { Spinner } from '@/components/ui/spinner';
import { useImage } from '@/features/images/api/get-image';
import { DeleteImage } from '@/features/images/components/delete-image';
import { ImageDisplay } from '@/features/images/components/image-display';
import { useUser } from '@/lib/auth/auth';
import { Authorization, POLICIES } from '@/lib/auth/authorization';
import { User } from '@/types/api';

export const ImageView = ({ imageId }: { imageId: string }) => {
  const user = useUser();
  const imageQuery = useImage({
    imageId,
  });

  if (imageQuery.isLoading) {
    return (
      <div className="flex h-48 w-full items-center justify-center">
        <Spinner size="lg" />
      </div>
    );
  }

  const image = imageQuery.data;
  if (!image) return null;

  return (
    <div className="relative mx-auto max-w-md self-start overflow-hidden rounded-lg bg-gray-800 p-6 shadow-md">
      <ImageDisplay imageId={image.id} />
      <Authorization
        policyCheck={POLICIES['image:delete'](user.data as User, image)}
      >
        <div className="flex items-center justify-between">
          {/*<Toggle
            initialState={image.isPublic}
            onToggle={handleToggle}
            toggleName="Make public"
          />*/}
          <DeleteImage id={imageId} />
        </div>
      </Authorization>
    </div>
  );
};
