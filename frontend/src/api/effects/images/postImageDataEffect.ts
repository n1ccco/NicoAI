import { axiosInstance } from '@/api/axios'
import { IMAGES } from '@/constants/apiConstants'
import type { EffectResult } from '../types'
import { PromptInput } from '@/types/formData'

type PromptState = {
  imageId: number
}

type PostImageDataResult = EffectResult<PromptState>

type PostImageDataEffectType = (
  promptData: PromptInput
) => Promise<PostImageDataResult>

const PostImageDataErrorMessage = 'Can not generate an image'

const postImageDataEffect: PostImageDataEffectType = async (promptData) => {
  const queryParams = new URLSearchParams({
    prompt: promptData.prompt,
    negativePrompt: promptData.negativePrompt,
    height: promptData.height.toString(),
    width: promptData.width.toString(),
    numInterferenceSteps: promptData.numInterferenceSteps.toString(),
    guidanceScale: promptData.guidanceScale.toString(),
  })

  return await axiosInstance
    .post(`${IMAGES}?${queryParams.toString()}`)
    .then((response): PostImageDataResult => {
      const { imageId } = response.data
      return {
        type: 'success',
        state: { imageId },
      }
    })
    .catch((_err) => {
      return {
        type: 'failed',
        state: {
          error: PostImageDataErrorMessage,
        },
      }
    })
}

export default postImageDataEffect
