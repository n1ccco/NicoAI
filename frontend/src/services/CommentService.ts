import { axiosInstance } from '@/api/axios'
import { IMAGES } from '@/constants/apiConstants'
import { CommentData } from '@/types/api'

export async function postComment(
  imageId: number,
  body: string
): Promise<void> {
  try {
    await axiosInstance.post(`${IMAGES}/${imageId}/comments`, { body })
  } catch (error) {
    console.error('Error posting comment:', error)
    throw error
  }
}

export async function getComments(imageId: number): Promise<CommentData[]> {
  try {
    const response = await axiosInstance.get(`${IMAGES}/${imageId}/comments`)
    const comments: CommentData[] = response.data as CommentData[]
    return comments
  } catch (error) {
    console.error('Error loading comments:', error)
    throw error
  }
}
