import { axiosInstance } from '@/api/axios'
import { IMAGES } from '@/constants/apiConstants'
import type { EffectResult } from '../types'
import { Photo } from '@/types/api'

type GetImageState = {
  photo: Photo
}

type GetImageResult = EffectResult<GetImageState>

type GetImageEffectType = (id: number) => Promise<GetImageResult>

const GetImageErrorMessage = 'Can not get an image'

const getImageEffect: GetImageEffectType = async (id) => {
  return await axiosInstance
    .get(`${IMAGES}/${id}`)
    .then((response): GetImageResult => {
      const photo: Photo = response.data
      return {
        type: 'success',
        state: { photo },
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

export default getImageEffect
