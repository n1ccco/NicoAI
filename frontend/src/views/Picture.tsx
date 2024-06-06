import { useEffect, useState } from 'react'
import { Navigate, useParams } from 'react-router-dom'
import { useAuth } from '@/hooks/useAuth.ts'
import Toggle from '@/components/ui/Toggle.tsx'
import { Photo, User } from '@/types/api.ts'
import { SIGNIN } from '@/constants/routeContants'
import Comments from '@/components/Comments'
import { changeImagePrivacyEffect, getImageEffect } from '@/api/effects/images'
import PictureDetails from '@/components/PictureDetails'
import LikeButton from '@/components/ui/LikeButton'
import { getCurrentUserEffect } from '@/api/effects/user'
import Loader from '@/components/ui/Loader'

const Picture = () => {
  const { id } = useParams<{ id: string }>()
  const [photo, setPhoto] = useState<Photo>()
  const { state: authStateType } = useAuth()
  const [loading, setLoading] = useState<boolean>(false)

  useEffect(() => {
    setLoading(true)
    getImageEffect(Number(id))
      .then((res) => {
        if (res.type === 'success') {
          const photo = res.state.photo
          setPhoto(photo)
        } else {
          console.error(res.state.error)
        }
      })
      .finally(() => {
        setLoading(false)
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

  const { state: authState } = useAuth()

  const [user, setUser] = useState<User | null>(null)

  useEffect(() => {
    if (authState.type === 'authenticated') {
      getCurrentUserEffect().then((res) => {
        if (res.type === 'success') {
          setUser(res.state.user)
        } else {
          console.error(res.state.error)
        }
      })
    }
  }, [authState.type])

  return (
    <div>
      {loading ? (
        <Loader />
      ) : (
        <div>
          {user && photo && (
            <div className="flex justify-between">
              <PictureDetails image={photo} />
              <div className="mx-auto max-w-md overflow-hidden rounded-lg bg-gray-800 p-6 shadow-md">
                <img
                  src={`data:image/jpeg;base64,${photo.imageData}`}
                  alt={`Photo ${photo.id}`}
                  className="mb-4 h-auto w-full rounded-lg shadow-md"
                />
                <LikeButton initialLiked={photo.isLiked} photoId={photo.id} />
                {photo.authorId === user.id && (
                  <Toggle
                    initialState={photo.isPublic}
                    onToggle={handleToggle}
                    toggleName="Make public"
                  />
                )}
              </div>
              <Comments photoId={photo.id} user={user} />
            </div>
          )}
        </div>
      )}
    </div>
  )
}

export default Picture
