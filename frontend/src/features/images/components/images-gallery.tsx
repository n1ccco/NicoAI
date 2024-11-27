import { ArchiveX } from 'lucide-react';
import * as React from 'react';

import { paths } from '@/config/paths';
import { useInfiniteImages } from '@/features/images/api/get-images';
import { useInfiniteSearchImages } from '@/features/images/api/search-images';
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
  keyword: string;
};

export const ImagesGallery = ({
  sortBy,
  sortDirection,
  userId,
  keyword,
}: ImagesListProps) => {
  //todo: fix conditional hook
  const imagesQuery = keyword
    ? useInfiniteSearchImages(keyword)
    : useInfiniteImages({ sortBy, sortDirection, userId });

  const observerRef = React.useRef<HTMLDivElement | null>(null);

  React.useEffect(() => {
    const observer = new IntersectionObserver(
      (entries) => {
        if (entries[0].isIntersecting && imagesQuery.hasNextPage) {
          imagesQuery.fetchNextPage();
        }
      },
      {
        root: null,
        rootMargin: '100px',
        threshold: 1.0,
      },
    );

    if (observerRef.current) {
      observer.observe(observerRef.current);
    }

    return () => {
      if (observerRef.current) {
        observer.unobserve(observerRef.current);
      }
    };
  }, [imagesQuery.hasNextPage, imagesQuery.fetchNextPage]);

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
      <div ref={observerRef} className="flex items-center justify-center py-4">
        {imagesQuery.isFetchingNextPage && <Spinner />}
      </div>
    </>
  );
};
