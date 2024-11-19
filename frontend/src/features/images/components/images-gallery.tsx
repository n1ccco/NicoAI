import { ArchiveX } from 'lucide-react';
import * as React from 'react';

import { paths } from '@/config/paths';
import { useInfiniteImages } from '@/features/images/api/get-images';
import { ImageDisplay } from '@/features/images/components/image-display';
import {
  ImageCaption,
  ImageCard,
  ImageGalleryContainer,
} from '@/shared/components/ui/image-gallery';
import { Link } from '@/shared/components/ui/link';
import { Spinner } from '@/shared/components/ui/spinner';
import { LikeButton } from '@/shared/features/reactions/components/toggle-like';
import { Button } from '@/shared/components/ui/button';

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
  const imagesQuery = useInfiniteImages({
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

  const images = imagesQuery.data?.pages.flatMap((page) => page.data);

  if (!images?.length) {
    return (
      <div className="flex h-80 flex-col items-center justify-center bg-white text-gray-500">
        <ArchiveX className="size-16" />
        <h4>No Entries Found</h4>
      </div>
    );
  }

  return (
    <>
      <ImageGalleryContainer>
        {images.map((image) => (
          <ImageCard key={image.id}>
            <Link to={paths.app.image.getHref(image.id)}>
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
      {imagesQuery.hasNextPage && (
        <div className="flex items-center justify-center py-4">
          <Button onClick={() => imagesQuery.fetchNextPage()}>
            {imagesQuery.isFetchingNextPage ? <Spinner /> : 'Load More Images'}
          </Button>
        </div>
      )}
    </>
  );
};
