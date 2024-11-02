import { QueryClient } from '@tanstack/react-query';
import { LoaderFunctionArgs, useSearchParams } from 'react-router-dom';

import { ContentLayout } from '@/components/layouts';
import { getImagesQueryOptions } from '@/features/images/api/get-images';
import { ImagesGallery } from '@/features/images/components/images-gallery';

export const imagesLoader =
  (queryClient: QueryClient) =>
  async ({ request }: LoaderFunctionArgs) => {
    const url = new URL(request.url);

    const sortBy =
      url.searchParams.get('sortBy') === 'likes' ? 'likes' : 'date';
    const sortDirection =
      url.searchParams.get('sortDirection') === 'desc' ? 'desc' : 'asc';

    const query = getImagesQueryOptions({ sortBy }, { sortDirection });

    return (
      queryClient.getQueryData(query.queryKey) ??
      (await queryClient.fetchQuery(query))
    );
  };

export const ImagesRoute = () => {
  const [searchParams] = useSearchParams();
  const sortBy = searchParams.get('sortBy') === 'likes' ? 'likes' : 'date';
  const sortDirection =
    searchParams.get('sortDirection') === 'desc' ? 'desc' : 'asc';
  return (
    <ContentLayout title="Images">
      <div className="mt-4">
        <ImagesGallery sortBy={sortBy} sortDirection={sortDirection} />
      </div>
    </ContentLayout>
  );
};
