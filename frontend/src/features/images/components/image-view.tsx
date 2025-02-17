import { paths } from '@/config/paths';
import { useImage } from '@/features/images/api/get-image';
import { ChangeImageVisibility } from '@/features/images/components/change-image-visibility';
import { DeleteImage } from '@/features/images/components/delete-image';
import { ImageDisplay } from '@/features/images/components/image-display';
import { ImagePrompt } from '@/features/images/components/image-prompt';
import { Link } from '@/shared/components/ui/link';
import { Spinner } from '@/shared/components/ui/spinner';
import { LikeButton } from '@/shared/features/reactions/components/toggle-like';
import { useUser } from '@/shared/lib/auth/auth';
import { Authorization, POLICIES } from '@/shared/lib/auth/authorization';
import { User } from '@/shared/types/api';

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
      <div className="mt-2 flex flex-col justify-between space-y-1.5 lg:flex-row lg:space-y-0">
        <span>
          By{' '}
          <Link to={paths.app.user.getHref(image.authorId)}>
            {image.authorName}
          </Link>
        </span>
        <Authorization
          policyCheck={POLICIES['image:delete'](user.data as User, image)}
        >
          <ChangeImageVisibility imageId={imageId} isVisible={image.isPublic} />
          <DeleteImage id={imageId} />
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
