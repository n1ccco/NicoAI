import Prompt from '../models/Prompt'
import { axiosInstance } from '../api/axios'
import { IMAGES } from '../constants/apiConstants'
import { Photo } from '@/types/api'

export async function postImageData(promptData: Prompt): Promise<number> {
  try {
    const queryParams = new URLSearchParams({
      prompt: promptData.prompt,
      negativePrompt: promptData.negativePrompt,
      height: promptData.height.toString(),
      width: promptData.width.toString(),
      numInterferenceSteps: promptData.numInterferenceSteps.toString(),
      guidanceScale: promptData.guidanceScale.toString(),
    })

    const response = await axiosInstance.post(
      `${IMAGES}?${queryParams.toString()}`
    )

    console.log(response.data)

    const { imageId } = response.data

    return imageId
  } catch (error) {
    console.error('Error posting image data:', error)
    throw error
  }
}

export async function getImage(id: number): Promise<Photo | undefined> {
  try {
    const response = await axiosInstance.get(`${IMAGES}/${id}`)
    const photo: Photo = response.data
    return photo
  } catch (error) {
    console.error('Error fetching data:', error)
  }
}

export async function changeImagePrivacy(
  id: number,
  privacyPayload: { isPublic: boolean }
) {
  try {
    if (privacyPayload.isPublic) {
      await axiosInstance.put(`${IMAGES}/${id}`, { action: 'makePublic' })
    } else {
      await axiosInstance.put(`${IMAGES}/${id}`, { action: 'makePrivate' })
    }
  } catch (error) {
    console.error('Error fetching data:', error)
  }
}
