import { Link } from 'react-router-dom'
import { IMAGES } from '../constants/routeContants.ts'
import { PhotoSimplified } from '@/types/api.ts'
import LikeButton from '@/components/ui/LikeButton.tsx'
import { useEffect, useState } from 'react'
import getImageBlobEffect from '@/api/effects/images/getImageBlobEffect.ts'

interface ImageProps {
  photo: PhotoSimplified
}

const Image = ({ photo }: ImageProps) => {
  const [imageBlob, setImageBlob] = useState<string>("");
  const [isLoaded, setIsLoaded] = useState<boolean>(false);

  useEffect(() => {
    getImageBlobEffect(photo.id)
      .then((res) => {
        if (res.type === "success") {
          setImageBlob(res.state.imageBlob);
        } else {
          console.error(res.state.error);
        }
      });
  }, [photo.id]);

  const handleImageLoad = () => {
    setIsLoaded(true);
  };

  return (
    <div key={photo.id} className="relative rounded-lg p-2 shadow-lg overflow-hidden">
      <div
        className={`absolute inset-0 bg-gray-800 transition-transform duration-1000 ease-in-out ${
          isLoaded ? "translate-y-full" : "translate-y-0"
        }`}
      />
      <Link to={`/${IMAGES}/${photo.id}`}>
        <img
          src={`data:image/jpeg;base64,${imageBlob}`}
          alt={`Photo ${photo.id}`}
          className="h-64 w-64 rounded-lg object-cover"
          onLoad={handleImageLoad}
        />
      </Link>
      <LikeButton
        initialLiked={photo.isLiked}
        photoId={photo.id}
        countLikes={photo.countLikes}
      />
    </div>
  );
};

export default Image
