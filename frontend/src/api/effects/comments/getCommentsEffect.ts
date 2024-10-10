import { axiosInstance } from '@/api/axios'
import { COMMENTS } from '@/constants/apiConstants'
import type { EffectResult } from '../types'
import { CommentData } from '@/types/api'

type CommentResponse = {
  id: number
  authorId: number
  body: string
  authorName: string
  createdAt: string
}

type CommentsState = {
  comments: CommentData[]
}

type GetCommentsResult = EffectResult<CommentsState>

type GetCommentsEffectType = (imageId: number) => Promise<GetCommentsResult>

const GetCommentsErrorMessage = 'Can not fetch comments'

const getCommentsEffect: GetCommentsEffectType = async (imageId) => {
  return await axiosInstance
    .get(COMMENTS, {params: {imageId}})
    .then((response): GetCommentsResult => {
      const comments: CommentData[] = (response.data as CommentResponse[]).map(
        (comment) => ({
          ...comment,
          createdAt: new Date(comment.createdAt), // Преобразование строки в Date
        })
      )
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
