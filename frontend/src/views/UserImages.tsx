import { useState, useEffect } from 'react'
import Image from '@/components/Image.tsx'
import { useParams } from 'react-router-dom'
import { Photo } from '@/types/api.ts'
import { getUserImagesEffect, getUsernameEffect } from '@/api/effects/user'
import { useAuth } from '@/hooks/useAuth'
import Loader from '@/components/ui/Loader'

const UserImages = () => {
  const { id } = useParams<{ id: string }>()
  const [photos, setPhotos] = useState<Photo[]>([])
  const [username, setUsername] = useState<String>('')
  const [loading, setLoading] = useState<boolean>(false)

  const { state: authState } = useAuth()

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true)

      try {
        const [imagesResult, usernameResult] = await Promise.all([
          getUserImagesEffect(Number(id)),
          getUsernameEffect(Number(id)),
        ])

        if (imagesResult.type === 'success') {
          setPhotos(imagesResult.state.images)
        } else {
          console.error(imagesResult.state.error)
        }

        if (usernameResult.type === 'success') {
          setUsername(usernameResult.state.username)
        } else {
          console.error(usernameResult.state.error)
        }
      } finally {
        setLoading(false)
      }
    }

    fetchData()
  }, [id])

  return (
    <div className="container mx-auto">
      <div className="flex flex-col items-center justify-center">
        {loading ? (
          <Loader />
        ) : (
          <div>
            {authState.type === 'authenticated' &&
            authState.state.user.id === Number(id)
              ? 'Your images'
              : 'Images of user: ' + username}
            <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4">
              {photos.map((photo) => (
                <Image key={photo.id} photo={photo} />
              ))}
            </div>
          </div>
        )}
      </div>
    </div>
  )
}

export default UserImages
