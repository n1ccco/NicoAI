import { Plus } from 'lucide-react';

import { Button } from '@/components/ui/button';
import { Form, FormDrawer, Textarea } from '@/components/ui/form';
import { useNotifications } from '@/components/ui/notifications';

import {
  useCreateComment,
  createCommentInputSchema,
} from '../api/create-comment';

type CreateCommentProps = {
  imageId: string;
};

export const CreateComment = ({ imageId }: CreateCommentProps) => {
  const { addNotification } = useNotifications();
  const createCommentMutation = useCreateComment({
    imageId,
    mutationConfig: {
      onSuccess: () => {
        addNotification({
          type: 'success',
          title: 'Comment Created',
        });
      },
    },
  });

  return (
    <FormDrawer
      isDone={createCommentMutation.isSuccess}
      triggerButton={
        <Button size="sm" icon={<Plus className="size-4" />}>
          Create Comment
        </Button>
      }
      title="Create Comment"
      submitButton={
        <Button
          isLoading={createCommentMutation.isPending}
          form="create-comment"
          type="submit"
          size="sm"
          disabled={createCommentMutation.isPending}
        >
          Submit
        </Button>
      }
    >
      <Form
        id="create-comment"
        onSubmit={(values) => {
          createCommentMutation.mutate({
            data: values,
          });
        }}
        schema={createCommentInputSchema}
        options={{
          defaultValues: {
            body: '',
            imageId: imageId,
          },
        }}
      >
        {({ register, formState }) => (
          <Textarea
            label="Body"
            error={formState.errors['body']}
            registration={register('body')}
          />
        )}
      </Form>
    </FormDrawer>
  );
};
