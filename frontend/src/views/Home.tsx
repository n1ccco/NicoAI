import { useState, useEffect } from 'react'
import Photo from '../models/Photo.ts'
import { axiosInstance } from '../api/axios.ts'
import { Link } from 'react-router-dom'
import { IMAGES } from '../constants/urlConstants.ts'
import { PICTURES } from '../constants/routeContants.ts'

const Home = () => {
  const [photos, setPhotos] = useState<Photo[]>([])

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axiosInstance.get(`${IMAGES}`) // Assuming your backend endpoint is '/images'
        setPhotos(response.data) // Assuming the response data is an array of photos
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
          <div key={photo.id} className="relative">
            <Link to={`${PICTURES}/${photo.id}`}>
              <img
                src={`data:image/jpeg;base64,${photo.imageData}`} // Assuming imageData is a base64-encoded string
                alt={`Photo ${photo.id}`}
                className="mb-2 h-auto w-full"
              />
            </Link>
          </div>
        ))}
      </div>
    </div>
  )
}

export default Home
