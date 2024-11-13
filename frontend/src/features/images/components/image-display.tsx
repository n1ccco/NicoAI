import { useImageBlob } from '@/features/images/api/get-image-blob';
import { Spinner } from '@/shared/components/ui/spinner';

export const ImageDisplay = ({ imageId }: { imageId: string }) => {
  const imageQuery = useImageBlob({
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
    <img
      src={`data:image/jpeg;base64,${image.imageBlob}`}
      alt={`${imageId}`}
      className="size-full rounded-lg object-cover shadow-md"
    />
  );
};
