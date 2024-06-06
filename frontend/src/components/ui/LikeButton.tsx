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
      className="absolute right-5 top-5 flex transform items-center space-x-2 rounded-full p-2 transition hover:scale-110"
    >
      <svg
        xmlns="http://www.w3.org/2000/svg"
        className={`h-8 w-8  ${liked ? 'text-red-500' : 'text-red-100'}`}
        viewBox="0 0 20 20"
        fill="currentColor"
      >
        <path
          className="border-2 border-solid border-black"
          d="M3.172 5.172a4 4 0 015.656 0L10 6.343l1.172-1.171a4 4 0 115.656 5.656l-6.829 6.829a.75.75 0 01-1.06 0l-6.829-6.83a4 4 0 010-5.655z"
        />
      </svg>
    </button>
  )
}

export default LikeButton
