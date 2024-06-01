import { useState, useEffect } from 'react'
import Image from '@/components/Image.tsx'
import { useParams } from 'react-router-dom'
import { Photo } from '@/types/api.ts'
import { getUserImagesEffect } from '@/api/effects/user'

const UserImages = () => {
  const { id } = useParams<{ id: string }>()
  const [photos, setPhotos] = useState<Photo[]>([])

  useEffect(() => {
    getUserImagesEffect(Number(id)).then((res) => {
      if (res.type === 'success') {
        setPhotos(res.state.images)
      } else {
        console.error(res.state.error)
      }
    })
  }, [id])

  return (
    <div className="container mx-auto">
      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4">
        {photos.map((photo) => (
          <Image key={photo.id} photo={photo} />
        ))}
      </div>
    </div>
  )
}

export default UserImages
