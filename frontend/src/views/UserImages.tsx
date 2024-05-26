import { useState, useEffect } from 'react'
import { axiosInstance } from '@/api/axios.ts'
import { IMAGES, USERS } from '@/constants/apiConstants.ts'
import Image from '@/components/Image.tsx'
import { useParams } from 'react-router-dom'
import { Photo } from '@/types/api.ts'

const UserImages = () => {
  const { id } = useParams<{ id: string }>()
  const [photos, setPhotos] = useState<Photo[]>([])

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axiosInstance.get(`${USERS}/${id}/${IMAGES}`)
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

export default UserImages
