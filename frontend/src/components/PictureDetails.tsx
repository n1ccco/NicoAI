import { Photo } from '@/types/api'

type PictureDetailsProps = {
  image: Photo
}

function PictureDetails({ image }: PictureDetailsProps) {
  return (
    <div className="comments-section rounded-lg bg-gray-800 p-4 shadow">
      <h2 className="mb-2 text-xl font-semibold">Picture Details</h2>
      <p className="mb-2 text-gray-600">ID: {image.id}</p>
      <p className="mb-2 text-gray-600">Prompt: {image.promptData.prompt}</p>
      <p className="mb-2 text-gray-600">
        Negative Prompt: {image.promptData.negativePrompt}
      </p>
      <p className="mb-2 text-gray-600">Width: {image.promptData.width} px</p>
      <p className="mb-2 text-gray-600">Height: {image.promptData.height} px</p>
      <p className="mb-2 text-gray-600">
        Number interference steps: {image.promptData.numInterferenceSteps}
      </p>
      <p className="mb-2 text-gray-600">
        Guidance scale: {image.promptData.guidanceScale}
      </p>
    </div>
  )
}

export default PictureDetails
