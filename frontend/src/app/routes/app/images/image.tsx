import { QueryClient } from '@tanstack/react-query';
import { LoaderFunctionArgs, useParams } from 'react-router-dom';

import { ContentLayout } from '@/components/layouts';
import { Spinner } from '@/components/ui/spinner';
import {
  getImageQueryOptions,
  useImage,
} from '@/features/images/api/get-image';
import { ImageView } from '@/features/images/components/image-view';

export const imageLoader =
  (queryClient: QueryClient) =>
  async ({ params }: LoaderFunctionArgs) => {
    console.log('imageLoader called');
    const imageId = params.imageId as string;

    const imageQuery = getImageQueryOptions(imageId);

    return (
      queryClient.getQueryData(imageQuery.queryKey) ??
      (await queryClient.fetchQuery(imageQuery))
    );
  };

export const ImageRoute = () => {
  const params = useParams();
  const imageId = params.imageId as string;
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
    <>
      <ContentLayout title="Image">
        <ImageView imageId={imageId} />
      </ContentLayout>
    </>
  );
};
