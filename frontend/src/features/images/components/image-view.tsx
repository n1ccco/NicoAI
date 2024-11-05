import { Link } from '@/components/ui/link';
import { Spinner } from '@/components/ui/spinner';
import { useImage } from '@/features/images/api/get-image';
import { DeleteImage } from '@/features/images/components/delete-image';
import { ImageDisplay } from '@/features/images/components/image-display';
import { ImagePrompt } from '@/features/images/components/image-prompt';
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
    <div className="flex flex-col items-start rounded-lg bg-white p-6 shadow-md md:flex-row">
      <div className="mb-4 md:mb-0">
        <ImageDisplay imageId={image.id} />
        <span>
          By{' '}
          <Link to={`/app/users/${image.authorId}/images`}>
            {image.authorName}
          </Link>
        </span>
        <Authorization
          policyCheck={POLICIES['image:delete'](user.data as User, image)}
        >
          <div className="mt-3 flex items-center justify-between">
            {/*<Toggle
            initialState={image.isPublic}
            onToggle={handleToggle}
            toggleName="Make public"
          />*/}
            <DeleteImage id={imageId} />
          </div>
        </Authorization>
      </div>
      <ImagePrompt imageId={imageId} />
    </div>
  );
};
