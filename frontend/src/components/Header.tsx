import { Link } from 'react-router-dom'
import {
  CLIENT_BASEURL,
  CREATE,
  GALLERY,
  SIGNIN,
} from '../constants/routeContants.ts'
import { useAuth } from '../hooks/useAuth.ts'
import UserDropdownMenu from './UserDropdownMenu.tsx'

interface HeaderProps {
  name?: string
}

function Header({ name }: HeaderProps) {
  const username: string | undefined = useAuth().getAuth()?.user.username
  const userId: number | undefined = useAuth().getAuth()?.user.id

  return (
    <header className="bg-gray-800 py-4">
      <div className="container mx-auto flex items-center justify-between px-4 md:px-20">
        <Link to={CLIENT_BASEURL} className="text-3xl font-bold">
          {name}
        </Link>

        <nav>
          <Link
            to={CLIENT_BASEURL}
            className="px-3 text-gray-400 hover:text-white"
          >
            Home
          </Link>
          <Link to={CREATE} className="px-3 text-gray-400 hover:text-white">
            Generate
          </Link>
          <Link to={GALLERY} className="px-3 text-gray-400 hover:text-white">
            Gallery
          </Link>
          {username && userId ? (
            <UserDropdownMenu username={username} userId={userId} />
          ) : (
            <Link to={SIGNIN} className="px-3 text-gray-400 hover:text-white">
              Login
            </Link>
          )}
        </nav>
      </div>
    </header>
  )
}

export default Header
