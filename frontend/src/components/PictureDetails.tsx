import { Photo } from '@/types/api'

type PictureDetailsProps = {
  image: Photo
}

function PictureDetails({ image }: PictureDetailsProps) {
  return (
    <div className="mx-auto max-w-md overflow-hidden rounded-lg bg-gray-800 p-6 shadow-md">
      <h2 className="mb-4 text-2xl font-bold text-white">Picture Details</h2>
      <div className="mb-2">
        <p className="text-sm font-medium text-gray-400">ID:</p>
        <p className="text-base text-gray-300">{image.id}</p>
      </div>
      <div className="mb-2">
        <p className="text-sm font-medium text-gray-400">Prompt:</p>
        <p className="text-base text-gray-300">{image.promptData.prompt}</p>
      </div>
      <div className="mb-2">
        <p className="text-sm font-medium text-gray-400">Negative Prompt:</p>
        <p className="text-base text-gray-300">
          {image.promptData.negativePrompt}
        </p>
      </div>
      <div className="mb-2">
        <p className="text-sm font-medium text-gray-400">Width:</p>
        <p className="text-base text-gray-300">{image.promptData.width} px</p>
      </div>
      <div className="mb-2">
        <p className="text-sm font-medium text-gray-400">Height:</p>
        <p className="text-base text-gray-300">{image.promptData.height} px</p>
      </div>
      <div className="mb-2">
        <p className="text-sm font-medium text-gray-400">
          Number Interference Steps:
        </p>
        <p className="text-base text-gray-300">
          {image.promptData.numInterferenceSteps}
        </p>
      </div>
      <div>
        <p className="text-sm font-medium text-gray-400">Guidance Scale:</p>
        <p className="text-base text-gray-300">
          {image.promptData.guidanceScale}
        </p>
      </div>
    </div>
  )
}

export default PictureDetails
