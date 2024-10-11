import { useState, useEffect } from 'react'
import Image from '@/components/Image.tsx'
import { PhotoSimplified } from '@/types/api.ts'
import { getAllImagesEffect } from '@/api/effects/images'
import Loader from '@/components/ui/Loader'

const Gallery = () => {
  const [photos, setPhotos] = useState<PhotoSimplified[]>([])
  const [loading, setLoading] = useState<boolean>(false)
  const [sortOption, setSortOption] = useState<string>('date-desc')

  useEffect(() => {
    const [sortBy, order] = sortOption.split('-')
    setLoading(true)
    getAllImagesEffect({ sortBy, order })
      .then((res) => {
        if (res.type === 'success') {
          setPhotos(res.state.photos)
        } else {
          console.error(res.state.error)
        }
      })
      .finally(() => {
        setLoading(false)
      })
  }, [sortOption])

  return (
    <div className="container mx-auto">
      <div className="mb-12 text-center">
        <h2 className="text-3xl font-bold md:text-4xl">
          User-Generated Gallery
        </h2>
        <p className="text-xl text-gray-400 md:text-2xl">
          Explore the creativity of our users
        </p>
      </div>

      <div className="mb-8 text-center">
        <label htmlFor="sortOption" className="mr-4 text-lg font-medium">
          Sort by:
        </label>
        <select
          id="sortOption"
          value={sortOption}
          onChange={(e) => setSortOption(e.target.value)}
          className="rounded border p-2 text-blue-600"
        >
          <option value="date-desc">Date (Newest first)</option>
          <option value="date-asc">Date (Oldest first)</option>
          <option value="rating-desc">Rating (Highest first)</option>
          <option value="rating-asc">Rating (Lowest first)</option>
        </select>
      </div>

      <div className="flex flex-col items-center justify-center">
        {loading ? (
          <Loader />
        ) : (
          <div className="grid w-full grid-cols-1 gap-8 md:grid-cols-3">
            {photos.map((photo) => (
              <Image key={photo.id} photo={photo} />
            ))}
          </div>
        )}
      </div>
    </div>
  )
}

export default Gallery
