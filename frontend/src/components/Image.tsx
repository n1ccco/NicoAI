import { Link } from 'react-router-dom'
import { IMAGES } from '../constants/routeContants.ts'
import { Photo } from '@/types/api.ts'
import LikeButton from '@/components/ui/LikeButton.tsx'

interface ImageProps {
  photo: Photo
}

function Image({ photo }: ImageProps) {
  return (
    <div
      key={photo.id}
      className="relative rounded-lg bg-gray-800 p-2 shadow-lg"
    >
      <Link to={`/${IMAGES}/${photo.id}`}>
        <img
          src={`data:image/jpeg;base64,${photo.imageData}`}
          alt={`Photo ${photo.id}`}
          className="h-64 w-full rounded-lg object-cover"
        />
      </Link>
      <LikeButton
        initialLiked={photo.isLiked}
        photoId={photo.id}
        countLikes={photo.countLikes}
      />
    </div>
  )
}

export default Image
