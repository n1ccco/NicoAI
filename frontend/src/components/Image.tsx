import { Link } from 'react-router-dom'
import { IMAGES } from '../constants/routeContants.ts'
import { Photo } from '@/types/api.ts'
import LikeButton from '@/components/ui/LikeButton.tsx' // Adjust the import path if necessary
import { likeImage } from '@/services/ImageProvider.ts' // Ensure this is imported here

interface ImageProps {
  photo: Photo
}

function Image({ photo }: ImageProps) {
  return (
    <div key={photo.id} className="rounded-lg bg-gray-800 p-4 shadow-lg">
      <Link to={`/${IMAGES}/${photo.id}`}>
        <img
          src={`data:image/jpeg;base64,${photo.imageData}`}
          alt={`Photo ${photo.id}`}
          className="mb-4 h-64 w-full rounded-lg object-cover"
        />
      </Link>
      <div className="flex items-center justify-between">
        <LikeButton
          initialLiked={photo.isLiked}
          photoId={photo.id}
          likeImage={likeImage}
        />
        <button className="rounded-lg bg-gray-700 px-4 py-2 text-white">
          Comment
        </button>
      </div>
    </div>
  )
}

export default Image
