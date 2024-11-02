import { QueryClient } from '@tanstack/react-query';
import { LoaderFunctionArgs, useParams } from 'react-router-dom';

import { ContentLayout } from '@/components/layouts';
import { getImageQueryOptions } from '@/features/images/api/get-image';
import { ImageView } from '@/features/images/components/image-view';

export const imageLoader =
  (queryClient: QueryClient) =>
  async ({ params }: LoaderFunctionArgs) => {
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
  return (
    <>
      <ContentLayout title="Image">
        <ImageView imageId={imageId} />
      </ContentLayout>
    </>
  );
};
