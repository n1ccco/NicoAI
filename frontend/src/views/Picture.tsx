import { useEffect, useState } from 'react'
import { Navigate, useParams } from 'react-router-dom'
import { useAuth } from '@/hooks/useAuth.ts'
import Toggle from '@/components/ui/Toggle.tsx'
import { Photo } from '@/types/api.ts'
import { SIGNIN } from '@/constants/routeContants'
import Comments from '@/components/Comments'
import { changeImagePrivacyEffect, getImageEffect } from '@/api/effects/images'

const Picture = () => {
  const { id } = useParams<{ id: string }>()
  const [photo, setPhoto] = useState<Photo>()
  const { state: authStateType } = useAuth()

  useEffect(() => {
    getImageEffect(Number(id)).then((res) => {
      if (res.type === 'success') {
        const photo = res.state.photo
        setPhoto(photo)
      } else {
        console.error(res.state.error)
      }
    })
  }, [id])

  const handleToggle = async () => {
    if (photo) {
      changeImagePrivacyEffect(Number(id), { isPublic: !photo.isPublic }).then(
        (res) => {
          if (res.type === 'success') {
            setPhoto((prevPhoto) =>
              prevPhoto
                ? { ...prevPhoto, isPublic: !prevPhoto.isPublic }
                : prevPhoto
            )
          } else {
            console.error(res.state.error)
          }
        }
      )
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
