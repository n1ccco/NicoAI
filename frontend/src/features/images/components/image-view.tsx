import { Link } from '@/components/ui/link';
import { Spinner } from '@/components/ui/spinner';
import { useImage } from '@/features/images/api/get-image';
import { ChangeImageVisibility } from '@/features/images/components/change-image-visibility';
import { DeleteImage } from '@/features/images/components/delete-image';
import { ImageDisplay } from '@/features/images/components/image-display';
import { ImagePrompt } from '@/features/images/components/image-prompt';
import { LikeButton } from '@/features/reactions/components/toggle-like';
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
    <div className="rounded-lg bg-white p-6 shadow-md">
      <div className="flex flex-col items-start md:flex-row">
        <div className="mb-4 md:mb-0">
          <ImageDisplay imageId={image.id} />
        </div>
        <ImagePrompt imageId={imageId} />
      </div>
      <div className="mt-2 flex flex-row justify-between">
        <span>
          By{' '}
          <Link to={`/app/users/${image.authorId}/images`}>
            {image.authorName}
          </Link>
        </span>
        <Authorization
          policyCheck={POLICIES['image:delete'](user.data as User, image)}
        >
          <div className="flex items-center justify-between space-x-2">
            <ChangeImageVisibility
              imageId={imageId}
              isVisible={image.isPublic}
            />
            <DeleteImage id={imageId} />
          </div>
        </Authorization>
        <LikeButton
          entityId={image.id}
          entityType="images"
          liked={image.isLiked}
          likeCount={image.countLikes}
        />
      </div>
    </div>
  );
};
