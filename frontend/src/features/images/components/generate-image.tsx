import { Plus } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

import { paths } from '@/config/paths';
import {
  generateImageInputSchema,
  useGenerateImage,
} from '@/features/images/api/generate-image';
import { Button } from '@/shared/components/ui/button';
import { Form, FormDrawer, Input } from '@/shared/components/ui/form';
import { useNotifications } from '@/shared/components/ui/notifications';

export const GenerateImage = () => {
  const { addNotification } = useNotifications();
  const navigate = useNavigate();
  const generateImageMutation = useGenerateImage({
    mutationConfig: {
      onSuccess: (data) => {
        addNotification({
          type: 'success',
          title: 'Image Generated',
        });
        navigate(paths.app.image.getHref(data.imageId));
      },
    },
  });

  return (
    <FormDrawer
      isDone={generateImageMutation.isSuccess}
      triggerButton={
        <Button size="sm" icon={<Plus className="size-4" />}>
          Generate Image
        </Button>
      }
      title="Generate Image"
      submitButton={
        <Button
          form="create-image"
          type="submit"
          size="sm"
          isLoading={generateImageMutation.isPending}
        >
          Submit
        </Button>
      }
    >
      <Form
        id="create-image"
        onSubmit={(values) => {
          generateImageMutation.mutate({ prompt: values });
        }}
        schema={generateImageInputSchema}
      >
        {({ register, formState }) => (
          <>
            <Input
              label="Prompt"
              placeholder="Enter prompt"
              error={formState.errors['prompt']}
              registration={register('prompt')}
            />

            <Input
              label="Negative Prompt"
              placeholder="Enter negative prompt"
              error={formState.errors['negativePrompt']}
              registration={register('negativePrompt')}
            />

            <Input
              label="Height"
              type="number"
              error={formState.errors['height']}
              registration={register('height', { valueAsNumber: true })}
            />

            <Input
              label="Width"
              type="number"
              error={formState.errors['width']}
              registration={register('width', { valueAsNumber: true })}
            />

            <Input
              label="Number of Interference Steps"
              type="number"
              error={formState.errors['numInterferenceSteps']}
              registration={register('numInterferenceSteps', {
                valueAsNumber: true,
              })}
            />

            <Input
              label="Guidance Scale"
              type="number"
              error={formState.errors['guidanceScale']}
              registration={register('guidanceScale', { valueAsNumber: true })}
            />
          </>
        )}
      </Form>
    </FormDrawer>
  );
};
