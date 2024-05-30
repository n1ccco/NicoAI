import { useState, useEffect } from 'react'
import Image from '@/components/Image.tsx'
import { Photo } from '@/types/api.ts'
import { getAllImages } from '@/services/ImageService'

const Gallery = () => {
  const [photos, setPhotos] = useState<Photo[]>([])

  useEffect(() => {
    const fetchImages = async () => {
      try {
        const photos = await getAllImages()
        setPhotos(photos)
      } catch (error) {
        console.error('Error fetching images:', error)
      }
    }

    fetchImages()
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
