import { useEffect, useState } from 'react'
import Photo from '../models/Photo.ts'
import { axiosInstance } from '../api/axios.ts'
import { useParams } from 'react-router-dom'

interface PictureProps {
  id: string

  [key: string]: string | undefined
}

const Picture = () => {
  const { id } = useParams<PictureProps>()
  const [photo, setPhoto] = useState<Photo>()

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axiosInstance.get(`/image/${id}`)
        setPhoto(response.data)
      } catch (error) {
        console.error('Error fetching data:', error)
      }
    }

    fetchData()
  }, [id])

  return (
    <div>
      {photo && (
        <>
          <h2>Picture Details</h2>
          <p>ID: {photo.id}</p>
          <img
            src={`data:image/jpeg;base64,${photo.imageData}`}
            alt={`Photo ${photo.id}`}
            className="mb-2 h-auto w-full"
          />
        </>
      )}
    </div>
  )
}

export default Picture
