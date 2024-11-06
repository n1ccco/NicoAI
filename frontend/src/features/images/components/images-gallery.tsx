import {
  ImageCaption,
  ImageCard,
  ImageGalleryContainer,
} from '@/components/ui/image-gallery';
import { Link } from '@/components/ui/link';
import { Spinner } from '@/components/ui/spinner';
import { useImages } from '@/features/images/api/get-images';
import { ImageDisplay } from '@/features/images/components/image-display';
import { LikeButton } from '@/features/reactions/components/toggle-like';

export type ImagesListProps = {
  sortBy: 'date' | 'likes';
  sortDirection: 'asc' | 'desc';
  userId?: string;
};

export const ImagesGallery = ({
  sortBy,
  sortDirection,
  userId,
}: ImagesListProps) => {
  const imagesQuery = useImages({
    sortBy,
    sortDirection,
    userId,
  });

  if (imagesQuery.isLoading) {
    return (
      <div className="flex h-48 w-full items-center justify-center">
        <Spinner size="lg" />
      </div>
    );
  }

  const images = imagesQuery.data;

  if (!images) return null;

  const imageEntries = images.map((image) => ({
    id: image.id,
    src: ``,
    alt: `Image ${image.id}`,
    isLiked: image.isLiked,
    countLikes: image.countLikes,
  }));

  return (
    <ImageGalleryContainer>
      {imageEntries.map((image) => (
        <ImageCard key={image.id}>
          <Link to={`/app/images/${image.id}`}>
            <ImageDisplay imageId={image.id} />
          </Link>

          <ImageCaption>
            <LikeButton
              entityId={image.id}
              entityType="images"
              liked={image.isLiked}
              likeCount={image.countLikes}
            />
          </ImageCaption>
        </ImageCard>
      ))}
    </ImageGalleryContainer>
  );
};
