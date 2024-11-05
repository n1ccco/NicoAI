import { QueryClient } from '@tanstack/react-query';
import { ErrorBoundary } from 'react-error-boundary';
import { LoaderFunctionArgs, useParams } from 'react-router-dom';

import { ContentLayout } from '@/components/layouts';
import { getInfiniteCommentsQueryOptions } from '@/features/comments/api/get-comments';
import { Comments } from '@/features/comments/components/comments';
import { getImageQueryOptions } from '@/features/images/api/get-image';
import { ImageView } from '@/features/images/components/image-view';

export const imageLoader =
  (queryClient: QueryClient) =>
  async ({ params }: LoaderFunctionArgs) => {
    const imageId = params.imageId as string;

    const imageQuery = getImageQueryOptions(imageId);
    const commentsQuery = getInfiniteCommentsQueryOptions(imageId);

    const promises = [
      queryClient.getQueryData(imageQuery.queryKey) ??
        (await queryClient.fetchQuery(imageQuery)),
      queryClient.getQueryData(commentsQuery.queryKey) ??
        (await queryClient.fetchInfiniteQuery(commentsQuery)),
    ] as const;

    const [discussion, comments] = await Promise.all(promises);

    return {
      discussion,
      comments,
    };
  };

export const ImageRoute = () => {
  const params = useParams();
  const imageId = params.imageId as string;
  return (
    <>
      <ContentLayout title="Image">
        <ImageView imageId={imageId} />
        <div className="mt-8">
          <ErrorBoundary
            fallback={
              <div>Failed to load comments. Try to refresh the page.</div>
            }
          >
            <Comments imageId={imageId} />
          </ErrorBoundary>
        </div>
      </ContentLayout>
    </>
  );
};