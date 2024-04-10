import { useState } from 'react'

const Home = () => {
  interface Photo {
    id: number
    url: string
    likes: number
  }

  const initialPhotos: Photo[] = [
    { id: 1, url: 'https://via.placeholder.com/500', likes: 0 },
    { id: 2, url: 'https://via.placeholder.com/600', likes: 0 },
    { id: 3, url: 'https://via.placeholder.com/400', likes: 0 },
    { id: 4, url: 'https://via.placeholder.com/700', likes: 0 },
    { id: 5, url: 'https://via.placeholder.com/550', likes: 0 },
    { id: 6, url: 'https://via.placeholder.com/450', likes: 0 },
    { id: 7, url: 'https://via.placeholder.com/650', likes: 0 },
    { id: 8, url: 'https://via.placeholder.com/350', likes: 0 },
  ]

  const [photos, setPhotos] = useState<Photo[]>(initialPhotos)

  const handleLike = (id: number) => {
    setPhotos((prevPhotos) =>
      prevPhotos.map((photo) =>
        photo.id === id ? { ...photo, likes: photo.likes + 1 } : photo
      )
    )
  }
  return (
    <div className="container mx-auto">
      <h1 className="my-8 text-3xl font-bold">Photos</h1>
      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4">
        {photos.map((photo) => (
          <div key={photo.id} className="relative">
            <img
              src={photo.url}
              alt={`Photo ${photo.id}`}
              className="mb-2 h-auto w-full"
            />
            <button
              className="absolute right-2 top-2 rounded bg-blue-500 px-2 py-1 font-bold text-white hover:bg-blue-700"
              onClick={() => handleLike(photo.id)}
            >
              Like
            </button>
            <span className="absolute bottom-2 left-2 font-bold text-white">
              Likes: {photo.likes}
            </span>
          </div>
        ))}
      </div>
    </div>
  )
}

export default Home
