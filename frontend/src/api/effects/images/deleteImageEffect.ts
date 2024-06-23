import { axiosInstance } from '@/api/axios'
import { IMAGES } from '@/constants/apiConstants'
import type { EffectResult } from '../types'


type DeleteImageEffectType = (id: number) => Promise<EffectResult>

const DeleteImageErrorMessage = 'Can not delete image'

const deleteImageEffect: DeleteImageEffectType = async (id) => {
  return await axiosInstance
    .delete(`${IMAGES}/${id}`)
    .then((_res): EffectResult => {
      return {
        type: 'success',
        state: { },
      }
    })
    .catch((_err) => {
      return {
        type: 'failed',
        state: {
          error: DeleteImageErrorMessage,
        },
      }
    })
}

export default deleteImageEffect
