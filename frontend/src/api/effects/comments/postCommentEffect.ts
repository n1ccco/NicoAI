import { axiosInstance } from '@/api/axios'
import { IMAGES } from '@/constants/apiConstants'
import type { EffectResult } from '../types'

const StateEmpty = {}

type PostCommentEffectType = (
  imageId: number,
  body: string
) => Promise<EffectResult>

const PostCommentErrorMessage = 'Can not post a comment'

const postCommentEffect: PostCommentEffectType = async (imageId, body) => {
  return await axiosInstance
    .post(`${IMAGES}/${imageId}/comments`, { body })
    .then(
      (_res): EffectResult => ({
        type: 'success',
        state: StateEmpty,
      })
    )
    .catch((err) => {
      console.error(err)
      return {
        type: 'failed',
        state: {
          error: PostCommentErrorMessage,
        },
      }
    })
}

export default postCommentEffect
