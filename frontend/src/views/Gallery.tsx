import { useState, useEffect } from 'react'
import { axiosInstance } from '../api/axios.ts'
import { IMAGES } from '../constants/apiConstants.ts'
import Image from '../components/Image.tsx'
import { Photo } from '@/types/api.ts'

const Gallery = () => {
  const [photos, setPhotos] = useState<Photo[]>([])

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axiosInstance.get(`${IMAGES}`)
        setPhotos(response.data)
      } catch (error) {
        console.error('Error fetching data:', error)
      }
    }

    fetchData()
  }, [])

  return (
    <div className="px-4 py-20 md:px-20">
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
    </div>
  )
}

export default Gallery
