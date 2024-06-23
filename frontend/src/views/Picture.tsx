import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { useAuth } from '@/hooks/useAuth.ts'
import Toggle from '@/components/ui/Toggle.tsx'
import { Photo, User } from '@/types/api.ts'
import Comments from '@/components/Comments'
import {
  changeImagePrivacyEffect,
  deleteImageEffect,
  getImageEffect,
} from '@/api/effects/images'
import PictureDetails from '@/components/PictureDetails'
import LikeButton from '@/components/ui/LikeButton'
import { getCurrentUserEffect } from '@/api/effects/user'
import Loader from '@/components/ui/Loader'
import { GALLERY } from '@/constants/routeContants'

const Picture = () => {
  const { id } = useParams<{ id: string }>()
  const [photo, setPhoto] = useState<Photo>()
  const [loading, setLoading] = useState<boolean>(false)
  const { state: authState } = useAuth()
  const [user, setUser] = useState<User | null>(null)
  const navigate = useNavigate()

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
    if (authState.type === 'authenticated') {
      getCurrentUserEffect().then((res) => {
        if (res.type === 'success') {
          setUser(res.state.user)
        } else {
          console.error(res.state.error)
        }
      })
    }
  }, [])

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

  const deleteImage = async () => {
    if (photo) {
      deleteImageEffect(Number(id)).then((res) => {
        if (res.type === 'success') {
          navigate(`/${GALLERY}`)
        } else {
          console.error(res.state.error)
        }
      })
    }
  }

  return (
    <div>
      {loading ? (
        <Loader />
      ) : (
        <div>
          {user && photo && (
            <div className="flex justify-between">
              <PictureDetails image={photo} />
              <div className="relative mx-auto max-w-md self-start overflow-hidden rounded-lg bg-gray-800 p-6 shadow-md">
                <img
                  src={`data:image/jpeg;base64,${photo.imageData}`}
                  alt={`Photo ${photo.id}`}
                  className="mb-4 h-auto w-full rounded-lg shadow-md"
                />
                <LikeButton
                  initialLiked={photo.isLiked}
                  photoId={photo.id}
                  countLikes={photo.countLikes}
                />
                {photo.authorId === user.id && (
                  <div className="flex items-center justify-between">
                    <Toggle
                      initialState={photo.isPublic}
                      onToggle={handleToggle}
                      toggleName="Make public"
                    />
                    <a
                      onClick={deleteImage}
                      className="inline-block cursor-pointer rounded bg-red-600 px-4 py-2 text-white transition duration-150 ease-in-out hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-red-600 focus:ring-opacity-50"
                    >
                      Delete
                    </a>
                  </div>
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
