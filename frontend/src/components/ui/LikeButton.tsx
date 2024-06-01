import { likeImageEffect } from '@/api/effects/images'
import { useState } from 'react'

type LikeButtonProps = {
  initialLiked: boolean
  photoId: number
  // likeImage: (id: number, likePayload: { like: boolean }) => Promise<void>
}

function LikeButton({ initialLiked, photoId }: LikeButtonProps) {
  const [liked, setLiked] = useState(initialLiked)

  const handleLike = async () => {
    likeImageEffect(photoId, { like: !liked }).then((res) => {
      if (res.type === 'success') {
        setLiked(!liked)
      } else {
        console.error(res.state.error)
      }
    })
  }

  return (
    <button
      onClick={handleLike}
      className={`rounded-lg px-4 py-2 text-white ${liked ? 'bg-red-500' : 'bg-blue-500'}`}
    >
      {liked ? 'Unlike' : 'Like'}
    </button>
  )
}

export default LikeButton
