import { useEffect, useState } from 'react'
import Photo from '../models/Photo.ts'
import { useParams } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth.ts'
import Toggle from '../components/Toggle.tsx'
import { changeImagePrivacy, getImage } from '../services/ImageProvider.ts'

const Picture = () => {
  const { id } = useParams<{ id: string }>()
  const [photo, setPhoto] = useState<Photo>()
  const userId = useAuth().userId

  useEffect(() => {
    const fetchImage = async () => {
      try {
        const photo = await getImage(Number(id))
        setPhoto(photo)
      } catch (error) {
        console.error('Error generating image:', error)
      }
    }

    fetchImage()
  }, [id])

  const handleToggle = async () => {
    try {
      // Update user settings or perform any other action based on the toggle state
      await changeImagePrivacy(Number(id), { imagePublic: !photo?.public })
    } catch (error) {
      console.error('Error updating user settings:', error)
    }
  }

  return (
    <div>
      {photo && (
        <div className="mx-auto max-w-md overflow-hidden rounded-lg bg-white shadow-md">
          <div className="p-6">
            <h2 className="mb-2 text-xl font-semibold">Picture Details</h2>
            <p className="mb-2 text-gray-600">ID: {photo.id}</p>
            <img
              src={`data:image/jpeg;base64,${photo.imageData}`}
              alt={`Photo ${photo.id}`}
              className="mb-4 h-auto w-full rounded-lg shadow-md"
            />
            {photo.authorId === userId && (
              <Toggle
                initialState={photo.public}
                onToggle={handleToggle}
                toggleName="Make public"
              />
            )}
          </div>
        </div>
      )}
    </div>
  )
}

export default Picture
