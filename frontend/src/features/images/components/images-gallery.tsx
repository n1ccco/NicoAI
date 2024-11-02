import {
  ImageCaption,
  ImageCard,
  ImageGalleryContainer,
} from '@/components/ui/image-gallery';
import { Link } from '@/components/ui/link';
import { Spinner } from '@/components/ui/spinner';
import { useImages } from '@/features/images/api/get-images';
import { ImageDisplay } from '@/features/images/components/image-display';

export type ImagesListProps = {
  sortBy: 'date' | 'likes';
  sortDirection: 'asc' | 'desc';
};

export const ImagesGallery = ({ sortBy, sortDirection }: ImagesListProps) => {
  const imagesQuery = useImages({
    sortBy,
    sortDirection,
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
    caption: image.isLiked
      ? `${image.countLikes} Likes`
      : `${image.countLikes} Likes`,
  }));

  return (
    <ImageGalleryContainer>
      {imageEntries.map((image) => (
        <ImageCard key={image.id}>
          <Link to={`./${image.id}`}>
            <ImageDisplay imageId={image.id} />
          </Link>
          {image.caption && <ImageCaption>{image.caption}</ImageCaption>}
        </ImageCard>
      ))}
    </ImageGalleryContainer>
  );
};
