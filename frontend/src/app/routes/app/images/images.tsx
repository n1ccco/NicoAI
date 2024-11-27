import { QueryClient } from '@tanstack/react-query';
import { useState } from 'react';
import {
  LoaderFunctionArgs,
  useParams,
  useSearchParams,
} from 'react-router-dom';

import { getInfiniteImagesQueryOptions } from '@/features/images/api/get-images';
import { GenerateImage } from '@/features/images/components/generate-image';
import { ImagesGallery } from '@/features/images/components/images-gallery';
import { ContentLayout } from '@/shared/components/layouts';
import { SearchBar } from '@/shared/components/ui/search';
import { SortMenu } from '@/shared/components/ui/sort-select';

export const imagesLoader =
  (queryClient: QueryClient) =>
  async ({ request, params }: LoaderFunctionArgs) => {
    const url = new URL(request.url);

    const sortBy =
      url.searchParams.get('sortBy') === 'likes' ? 'likes' : 'date';
    const sortDirection =
      url.searchParams.get('sortDirection') === 'desc' ? 'desc' : 'asc';
    const userId = params.userId || undefined;

    const imagesQuery = getInfiniteImagesQueryOptions({
      sortBy,
      sortDirection,
      userId,
    });

    return (
      queryClient.getQueryData(imagesQuery.queryKey) ??
      (await queryClient.fetchInfiniteQuery(imagesQuery))
    );
  };

export const ImagesRoute = () => {
  const [searchParams] = useSearchParams();
  const [keyword, setKeyword] = useState('');
  const sortBy = searchParams.get('sortBy') === 'likes' ? 'likes' : 'date';
  const sortDirection =
    searchParams.get('sortDirection') === 'desc' ? 'desc' : 'asc';
  const { userId } = useParams();
  const handleSearch = (newKeyword: string) => {
    setKeyword(newKeyword);
  };

  return (
    <ContentLayout title="Images">
      <div className="flex justify-between">
        <SortMenu defaultSortBy={sortBy} defaultSortDirection={sortDirection} />
        {!userId && <SearchBar onSearch={handleSearch} />}
        <GenerateImage />
      </div>
      <div className="mt-4">
        <ImagesGallery
          sortBy={sortBy}
          sortDirection={sortDirection}
          userId={userId}
          keyword={keyword}
        />
      </div>
    </ContentLayout>
  );
};
