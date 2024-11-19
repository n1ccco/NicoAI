import { ArchiveX } from 'lucide-react';

import { MDPreview } from '@/shared/components/ui/md-preview';
import { Spinner } from '@/shared/components/ui/spinner';
import { useUser } from '@/shared/lib/auth/auth';
import { Authorization, POLICIES } from '@/shared/lib/auth/authorization';
import { User } from '@/shared/types/api';
import { formatDate } from '@/shared/utils/format';

import { useInfiniteComments } from '../api/get-comments';

import { DeleteComment } from './delete-comment';
import * as React from 'react';

type CommentsListProps = {
  imageId: string;
};

export const CommentsList = ({ imageId }: CommentsListProps) => {
  const user = useUser();
  const commentsQuery = useInfiniteComments({ imageId });

  const observerRef = React.useRef<HTMLDivElement | null>(null);

  React.useEffect(() => {
    const observer = new IntersectionObserver(
      (entries) => {
        if (entries[0].isIntersecting && commentsQuery.hasNextPage) {
          commentsQuery.fetchNextPage();
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
  }, [commentsQuery.hasNextPage, commentsQuery.fetchNextPage]);

  if (commentsQuery.isLoading) {
    return (
      <div className="flex h-48 w-full items-center justify-center">
        <Spinner size="lg" />
      </div>
    );
  }

  const comments = commentsQuery.data?.pages.flatMap((page) => page.data);

  if (!comments?.length)
    return (
      <div
        role="list"
        aria-label="comments"
        className="flex h-40 flex-col items-center justify-center bg-white text-gray-500"
      >
        <ArchiveX className="size-10" />
        <h4>No Comments Found</h4>
      </div>
    );

  return (
    <>
      <ul aria-label="comments" className="flex flex-col space-y-3">
        {comments.map((comment, index) => (
          <li
            aria-label={`comment-${comment.body}-${index}`}
            key={comment.id || index}
            className="w-full bg-white p-4 shadow-sm"
          >
            <Authorization
              policyCheck={POLICIES['comment:delete'](
                user.data as User,
                comment,
              )}
            >
              <div className="flex justify-between">
                <div>
                  <span className="text-xs font-semibold">
                    {formatDate(comment.createdAt)}
                  </span>
                  {comment.author && (
                    <span className="text-xs font-bold">
                      {' '}
                      by {comment.author.username}
                    </span>
                  )}
                </div>
                <DeleteComment imageId={imageId} id={comment.id} />
              </div>
            </Authorization>

            <MDPreview value={comment.body} />
          </li>
        ))}
      </ul>
      <div ref={observerRef} className="flex items-center justify-center py-4">
        {commentsQuery.isFetchingNextPage && <Spinner />}
      </div>
    </>
  );
};
