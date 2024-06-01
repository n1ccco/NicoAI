import { useState, useEffect } from 'react'
import Image from '@/components/Image.tsx'
import { Photo } from '@/types/api.ts'
import { getAllImagesEffect } from '@/api/effects/images'

const Gallery = () => {
  const [photos, setPhotos] = useState<Photo[]>([])

  useEffect(() => {
    getAllImagesEffect().then((res) => {
      if (res.type === 'success') {
        setPhotos(res.state.photos)
      } else {
        console.error(res.state.error)
      }
    })
  }, [])

  return (
    <div className="container mx-auto">
      <div className="mb-12 text-center">
        <h2 className="text-3xl font-bold md:text-4xl">
          User-Generated Gallery
        </h2>
        <p className="text-xl text-gray-400 md:text-2xl">
          Explore the creativity of our users
        </p>
      </div>
      <div className="grid grid-cols-1 gap-8 md:grid-cols-3">
        {photos.map((photo) => (
          <Image key={photo.id} photo={photo} />
        ))}
      </div>
    </div>
  )
}

export default Gallery
