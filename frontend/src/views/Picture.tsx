import { useEffect, useState } from 'react'
import { Navigate, useParams } from 'react-router-dom'
import { useAuth } from '@/hooks/useAuth.ts'
import Toggle from '@/components/ui/Toggle.tsx'
import { changeImagePrivacy, getImage } from '@/services/ImageService'
import { Photo } from '@/types/api.ts'
import { SIGNIN } from '@/constants/routeContants'
import Comments from '@/components/Comments'

const Picture = () => {
  const { id } = useParams<{ id: string }>()
  const [photo, setPhoto] = useState<Photo>()
  const { state: authStateType } = useAuth()

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
    if (photo) {
      try {
        // Toggle the isPublic state and update the server
        await changeImagePrivacy(Number(id), {
          isPublic: !photo.isPublic,
        })

        // Update the local state with the new isPublic value
        setPhoto((prevPhoto) =>
          prevPhoto
            ? { ...prevPhoto, isPublic: !prevPhoto.isPublic }
            : prevPhoto
        )
      } catch (error) {
        console.error('Error updating photo privacy:', error)
      }
    }
  }

  if (authStateType.type === 'not-authenticated') {
    return <Navigate to={'/' + SIGNIN} />
  }

  const user = authStateType.state.user
  return (
    <div>
      {photo && (
        <div className="flex justify-between">
          <div className="mx-auto max-w-md overflow-hidden rounded-lg bg-gray-800 shadow-md">
            <div className="p-6">
              <h2 className="mb-2 text-xl font-semibold">Picture Details</h2>
              <p className="mb-2 text-gray-600">ID: {photo.id}</p>
              <img
                src={`data:image/jpeg;base64,${photo.imageData}`}
                alt={`Photo ${photo.id}`}
                className="mb-4 h-auto w-full rounded-lg shadow-md"
              />
              {photo.authorId === user.id && (
                <Toggle
                  initialState={photo.isPublic}
                  onToggle={handleToggle}
                  toggleName="Make public"
                />
              )}
            </div>
          </div>
          <Comments photoId={photo.id} user={user} />
        </div>
      )}
    </div>
  )
}

export default Picture
