import {
  deleteCommentEffect,
  getCommentsEffect,
  postCommentEffect,
} from '@/api/effects/comments'
import { CommentData, User } from '@/types/api'
import React, { useState, useEffect } from 'react'

interface CommentsProps {
  photoId: number
  user: User
}

const Comments: React.FC<CommentsProps> = ({ photoId, user }) => {
  const [comments, setComments] = useState<CommentData[]>([])
  const [newComment, setNewComment] = useState<string>('')

  useEffect(() => {
    getCommentsEffect(photoId).then((res) => {
      if (res.type === 'success') {
        setComments(res.state.comments)
      } else {
        console.error(res.state.error)
      }
    })
  }, [photoId])

  const handleAddComment = async () => {
    if (!newComment.trim()) return

    postCommentEffect(photoId, newComment).then((res) => {
      if (res.type === 'success') {
        const comment: CommentData = {
          id: new Date().getTime(),
          authorId: user.id,
          authorName: user.username,
          body: newComment,
          createdAt: new Date().getTime(),
        }
        setComments([...comments, comment])
        setNewComment('')
      } else {
        console.error(res.state.error)
      }
    })
  }

  const handleDeleteComment = async (commentId: number) => {
    deleteCommentEffect(commentId).then((res) => {
      if (res.type === 'success') {
        setComments(comments.filter((comment) => comment.id !== commentId))
      } else {
        console.error(res.state.error)
      }
    })
  }

  const formatDate = (dateString: number) => {
    const date = new Date(dateString)
    return `${date.toLocaleDateString()} at ${date.toLocaleTimeString()}`
  }

  return (
    <div className="comments-section mt-6 rounded-lg bg-gray-800 p-4 shadow">
      <h3 className="text-lg font-semibold text-gray-200">Comments</h3>
      <div className="mt-4 space-y-4">
        {comments.map((comment) => (
          <div
            key={comment.id}
            className="comment rounded bg-gray-200 p-3 shadow"
          >
            <div className="flex items-center justify-between">
              <h5 className="font-semibold text-gray-900">
                {comment.authorName}
              </h5>
              <span className="text-xs text-gray-500">
                {formatDate(comment.createdAt)}
              </span>
            </div>
            <p className="text-gray-700">{comment.body}</p>

            {comment.authorId === user.id && (
              <button
                className="mt-2 text-xs text-red-600 hover:text-red-800"
                onClick={() => handleDeleteComment(comment.id)}
              >
                Delete
              </button>
            )}
          </div>
        ))}
      </div>
      <div className="add-comment mt-6">
        <input
          type="text"
          value={newComment}
          onChange={(e) => setNewComment(e.target.value)}
          placeholder="Add a comment..."
          className="w-full rounded border border-gray-300 p-2 text-gray-700"
        />
        <button
          className="mt-2 rounded bg-blue-500 px-4 py-2 text-white hover:bg-blue-600"
          onClick={handleAddComment}
        >
          Comment
        </button>
      </div>
    </div>
  )
}

export default Comments
