import { axiosInstance } from '@/api/axios'
import { IMAGES } from '@/constants/apiConstants'
import type { EffectResult } from '../types'
import { PhotoBlob } from '@/types/api.ts'

type GetImageBlobState = {
  imageBlob: string
}

type GetImageBlobResult = EffectResult<GetImageBlobState>

type GetImageBlobEffectType = (id: number) => Promise<GetImageBlobResult>

const GetImageErrorMessage = 'Can not get an image blob'

const getImageBlobEffect: GetImageBlobEffectType = async (id) => {
  return await axiosInstance
    .get(`${IMAGES}/${id}/blob`)
    .then((response): GetImageBlobResult => {
      const imageBlob: PhotoBlob = response.data
      return {
        type: 'success',
        state: imageBlob,
      }
    })
    .catch((_err) => {
      return {
        type: 'failed',
        state: {
          error: GetImageErrorMessage,
        },
      }
    })
}

export default getImageBlobEffect
