import { ArchiveX } from 'lucide-react';
import * as React from 'react';

import { useImages } from '@/features/images/api/get-images';
import { ImageDisplay } from '@/features/images/components/image-display';
import {
  ImageCaption,
  ImageCard,
  ImageGalleryContainer,
} from '@/shared/components/ui/image-gallery';
import { Link } from '@/shared/components/ui/link';
import { Spinner } from '@/shared/components/ui/spinner';
import { LikeButton } from '@/shared/features/reactions/components/toggle-like';

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
  if (!images.length) {
    return (
      <div className="flex h-80 flex-col items-center justify-center bg-white text-gray-500">
        <ArchiveX className="size-16" />
        <h4>No Entries Found</h4>
      </div>
    );
  }

  return (
    <ImageGalleryContainer>
      {images.map((image) => (
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
