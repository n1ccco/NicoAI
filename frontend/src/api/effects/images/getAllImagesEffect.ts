import { axiosInstance } from '@/api/axios'
import { IMAGES } from '@/constants/apiConstants'
import type { EffectResult } from '../types'
import { PhotoSimplified } from '@/types/api'

type GetAllImagesState = {
  photos: PhotoSimplified[]
}

type GetAllImagesResult = EffectResult<GetAllImagesState>

type GetAllImagesEffectType = ({ sortBy, order }: { sortBy: string, order: string }) => Promise<GetAllImagesResult>

const GetAllImagesErrorMessage = 'Can not get images'

const getAllImagesEffect: GetAllImagesEffectType = async ({ sortBy, order }) => {
  return await axiosInstance
    .get(IMAGES, {params: {sortBy, order}})
    .then((response): GetAllImagesResult => {
      const photos: PhotoSimplified[] = response.data
      return {
        type: 'success',
        state: { photos },
      }
    })
    .catch((_err) => {
      return {
        type: 'failed',
        state: {
          error: GetAllImagesErrorMessage,
        },
      }
    })
}

export default getAllImagesEffect
