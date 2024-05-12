import { Link } from 'react-router-dom'
import { IMAGES } from '../constants/routeContants.ts'
import Photo from '../models/Photo.ts'

interface ImageProps {
  photo: Photo
}

const Image: React.FC<ImageProps> = ({ photo }) => {
  return (
    <div key={photo.id} className="relative">
      <Link to={`/${IMAGES}/${photo.id}`}>
        <img
          src={`data:image/jpeg;base64,${photo.imageData}`}
          alt={`Photo ${photo.id}`}
          className="mb-2 h-auto w-full"
        />
      </Link>
    </div>
  )
}

export default Image
