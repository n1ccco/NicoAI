import { axiosInstance } from '@/api/axios'
import type { EffectResult } from '../types'
import { Photo } from '@/types/api'
import { IMAGES, USERS } from '@/constants/apiConstants'

type GetUserImagesState = {
  images: Photo[]
}

type GetUserImagesResult = EffectResult<GetUserImagesState>

type GetUserImagesEffectType = (userId: number) => Promise<GetUserImagesResult>

const GetUserImagesErrorMessage = 'Can not get user images'

const getUserImagesEffect: GetUserImagesEffectType = async (userId) => {
  return await axiosInstance
    .get(`${USERS}/${userId}/${IMAGES}`)
    .then((response): GetUserImagesResult => {
      const images: Photo[] = response.data
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
