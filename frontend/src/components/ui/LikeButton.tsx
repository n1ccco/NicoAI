import { useState } from 'react'

interface LikeButtonProps {
  initialLiked: boolean
  photoId: number
  likeImage: (id: number, likePayload: { like: boolean }) => Promise<void>
}

function LikeButton({ initialLiked, photoId, likeImage }: LikeButtonProps) {
  const [liked, setLiked] = useState(initialLiked)

  const handleLike = async () => {
    setLiked(!liked)
    await likeImage(photoId, { like: !liked })
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
