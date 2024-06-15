import { axiosInstance } from '@/api/axios'
import { IMAGES } from '@/constants/apiConstants'
import type { EffectResult } from '../types'

const StateEmpty = {}

type LikeImageEffectType = (
  id: number,
  likePayload: { like: boolean }
) => Promise<EffectResult>

const LikeImageErrorMessage = 'Can not like an image'

const LikeImageEffect: LikeImageEffectType = async (id, likePayload) => {
  const action = likePayload.like ? 'like' : 'dislike'

  return await axiosInstance
    .patch(`${IMAGES}/${id}`, { action })
    .then((_res): EffectResult => {
      return {
        type: 'success',
        state: StateEmpty,
      }
    })
    .catch((_err) => {
      return {
        type: 'failed',
        state: {
          error: LikeImageErrorMessage,
        },
      }
    })
}

export default LikeImageEffect
