import { useImagePrompt } from '@/features/images/api/get-image-prompt';
import { Spinner } from '@/shared/components/ui/spinner';

export const ImagePrompt = ({ imageId }: { imageId: string }) => {
  const imagePromptQuery = useImagePrompt({
    imageId,
  });

  if (imagePromptQuery.isLoading) {
    return (
      <div className="flex h-48 w-full items-center justify-center">
        <Spinner size="lg" />
      </div>
    );
  }

  const image = imagePromptQuery.data;
  if (!image) return null;

  return (
    <div className="md:ml-6 md:w-2/3">
      <h2 className="mb-4 text-xl font-semibold text-gray-800">
        Prompt Details
      </h2>
      <div className="space-y-2">
        <div className="flex justify-between">
          <span className="m-3 font-medium text-gray-700">Image Id:</span>
          <span className="text-gray-900">{imageId}</span>
        </div>
        <div className="flex justify-between">
          <span className="m-3 font-medium text-gray-700">Prompt:</span>
          <span className="text-gray-900">{image.prompt}</span>
        </div>
        <div className="flex justify-between">
          <span className="m-3 font-medium text-gray-700">
            Negative Prompt:
          </span>
          <span className="text-gray-900">{image.negativePrompt}</span>
        </div>
        <div className="flex justify-between">
          <span className="m-3 font-medium text-gray-700">Height:</span>
          <span className="text-gray-900">{image.height}</span>
        </div>
        <div className="flex justify-between">
          <span className="m-3 font-medium text-gray-700">Width:</span>
          <span className="text-gray-900">{image.width}</span>
        </div>
        <div className="flex justify-between">
          <span className="m-3 font-medium text-gray-700">
            Interference Steps:
          </span>
          <span className="text-gray-900">{image.numInterferenceSteps}</span>
        </div>
        <div className="flex justify-between">
          <span className="m-3 font-medium text-gray-700">Guidance Scale:</span>
          <span className="text-gray-900">{image.guidanceScale}</span>
        </div>
      </div>
    </div>
  );
};
