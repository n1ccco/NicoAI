import { CommentsList } from './comments-list';
import { CreateComment } from './create-comment';

type CommentsProps = {
  imageId: string;
};

export const Comments = ({ imageId }: CommentsProps) => {
  return (
    <div>
      <div className="mb-4 flex items-center justify-between">
        <h3 className="text-xl font-bold">Comments:</h3>
        <CreateComment imageId={imageId} />
      </div>
      <CommentsList imageId={imageId} />
    </div>
  );
};
