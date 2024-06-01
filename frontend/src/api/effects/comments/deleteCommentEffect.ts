import { axiosInstance } from '@/api/axios'
import type { EffectResult } from '../types'
import { COMMENTS } from '@/constants/apiConstants'

const StateEmpty = {}

type DeleteCommentEffectType = (commentId: number) => Promise<EffectResult>

const DeleteCommentErrorMessage = 'Can not not delete the comment'

const deleteCommentEffect: DeleteCommentEffectType = async (commentId) => {
  return await axiosInstance
    .delete(`${COMMENTS}/${commentId}`)
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
          error: DeleteCommentErrorMessage,
        },
      }
    })
}

export default deleteCommentEffect
