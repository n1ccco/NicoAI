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

type PostCommentState = {
  comment: CommentData
}

type PostCommentResult = EffectResult<PostCommentState>

type PostCommentEffectType = (
  imageId: number,
  body: string
) => Promise<PostCommentResult>

const PostCommentErrorMessage = 'Can not post a comment'

const postCommentEffect: PostCommentEffectType = async (imageId, body) => {
  const commentPayload = {
    imageId: imageId,
    body: body
  }
  return await axiosInstance
    .post(COMMENTS, commentPayload)
    .then((response): PostCommentResult => {
      const comment: CommentData = {
        ...(response.data as CommentResponse),
        createdAt: new Date((response.data as CommentResponse).createdAt),
      }

      return {
        type: 'success',
        state: { comment },
      }
    })
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
