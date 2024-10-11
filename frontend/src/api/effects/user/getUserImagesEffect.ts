import { axiosInstance } from '@/api/axios'
import type { EffectResult } from '../types'
import { PhotoSimplified } from '@/types/api'
import { IMAGES } from '@/constants/apiConstants'

type GetUserImagesState = {
  images: PhotoSimplified[]
}

type GetUserImagesResult = EffectResult<GetUserImagesState>

type GetUserImagesEffectType = (userId: number) => Promise<GetUserImagesResult>

const GetUserImagesErrorMessage = 'Can not get user images'

const getUserImagesEffect: GetUserImagesEffectType = async (userId) => {
  return await axiosInstance
    .get(IMAGES,{params: {userId}})
    .then((response): GetUserImagesResult => {
      const images: PhotoSimplified[] = response.data
      return {
        type: 'success',
        state: { images },
      }
    })
    .catch((_err) => {
      return {
        type: 'failed',
        state: {
          error: GetUserImagesErrorMessage,
        },
      }
    })
}

export default getUserImagesEffect
