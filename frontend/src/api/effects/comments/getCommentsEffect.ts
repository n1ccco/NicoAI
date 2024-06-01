import { axiosInstance } from '@/api/axios'
import { IMAGES } from '@/constants/apiConstants'
import type { EffectResult } from '../types'
import { CommentData } from '@/types/api'

type CommentsState = {
  comments: CommentData[]
}

type GetCommentsResult = EffectResult<CommentsState>

type GetCommentsEffectType = (imageId: number) => Promise<GetCommentsResult>

const GetCommentsErrorMessage = 'Can not fetch comments'

const getCommentsEffect: GetCommentsEffectType = async (imageId) => {
  return await axiosInstance
    .get(`${IMAGES}/${imageId}/comments`)
    .then((response): GetCommentsResult => {
      const comments = response.data as CommentData[]
      return {
        type: 'success',
        state: {
          comments,
        },
      }
    })
    .catch((_error) => {
      return {
        type: 'failed',
        state: {
          error: GetCommentsErrorMessage,
        },
      }
    })
}

export default getCommentsEffect
