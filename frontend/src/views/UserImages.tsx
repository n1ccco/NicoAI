import { useState, useEffect } from 'react'
import Image from '@/components/Image.tsx'
import { useParams } from 'react-router-dom'
import { Photo } from '@/types/api.ts'
import { getUserImagesEffect, getUsernameEffect } from '@/api/effects/user'
import { useAuth } from '@/hooks/useAuth'

const UserImages = () => {
  const { id } = useParams<{ id: string }>()
  const [photos, setPhotos] = useState<Photo[]>([])
  const [username, setUsername] = useState<String>('')

  const { state: authState } = useAuth()

  useEffect(() => {
    getUserImagesEffect(Number(id)).then((res) => {
      if (res.type === 'success') {
        setPhotos(res.state.images)
      } else {
        console.error(res.state.error)
      }
    })
    getUsernameEffect(Number(id)).then((res) => {
      if (res.type === 'success') {
        setUsername(res.state.username)
      } else {
        console.error(res.state.error)
      }
    })
  }, [id])

  return (
    <div className="container mx-auto">
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
  )
}

export default UserImages
