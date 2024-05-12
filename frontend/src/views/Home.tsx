import { useState, useEffect } from 'react'
import Photo from '../models/Photo.ts'
import { axiosInstance } from '../api/axios.ts'
import { IMAGES } from '../constants/apiConstants.ts'
import Image from '../components/Image.tsx'

const Home = () => {
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
    <div className="container mx-auto">
      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4">
        {photos.map((photo) => (
          <Image key={photo.id} photo={photo} />
        ))}
      </div>
    </div>
  )
}

export default Home
