import { Link } from 'react-router-dom'
import { GALLERY } from '@/constants/routeContants'

const Home = () => {
  return (
    <div className="flex min-h-screen flex-col bg-gray-900 font-sans text-gray-200">
      <section
        id="home"
        className="flex flex-col items-center justify-center py-20 text-center"
      >
        <h1 className="mb-4 text-4xl font-bold md:text-6xl">
          Welcome to NicoAI
        </h1>
        <p className="mb-8 text-xl md:text-2xl">
          Generate amazing images with AI. Start with exploring our gallery!
        </p>
        <Link
          className="rounded-full bg-blue-500 px-6 py-3 text-white transition duration-300 hover:bg-blue-600"
          to={GALLERY}
        >
          Get Started
        </Link>
      </section>

      <section id="features" className="px-4 py-10 md:px-20">
        <div className="mb-12 text-center">
          <h2 className="text-3xl font-bold md:text-4xl">Features</h2>
          <p className="text-xl text-gray-400 md:text-2xl">
            Discover the power of AI image generation
          </p>
        </div>
        <div className="flex flex-wrap justify-center gap-8">
          <div className="w-full rounded-lg bg-gray-800 p-6 shadow-lg md:w-1/3">
            <h3 className="mb-4 text-2xl font-bold">AI Image Generation</h3>
            <p>Create unique images from your prompts with our powerful AI.</p>
          </div>
          <div className="w-full rounded-lg bg-gray-800 p-6 shadow-lg md:w-1/3">
            <h3 className="mb-4 text-2xl font-bold">User Interaction</h3>
            <p>Like and comment on your favorite generated images.</p>
          </div>
          <div className="w-full rounded-lg bg-gray-800 p-6 shadow-lg md:w-1/3">
            <h3 className="mb-4 text-2xl font-bold">Gallery</h3>
            <p>Browse all user-generated images in our interactive gallery.</p>
          </div>
        </div>
      </section>
    </div>
  )
}

export default Home
