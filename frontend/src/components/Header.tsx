import { Link } from 'react-router-dom'
import { CLIENT_BASEURL, CREATE, SIGNIN } from '../constants/routeContants.ts'
import { useAuth } from '../hooks/useAuth.ts'
import UserDropdownMenu from './UserDropdownMenu.tsx'

interface HeaderProps {
  name?: string
}

function Header({ name }: HeaderProps) {
  const username: string | null = useAuth().username
  const userId: number = useAuth().userId

  return (
    <nav className="bg-blue-500 p-4">
      <div className="container mx-auto flex items-center justify-between">
        <ul className="flex">
          <li className="mr-6">
            <Link
              to={CLIENT_BASEURL}
              className="text-white hover:text-gray-200"
            >
              {name}
            </Link>
          </li>
          <li className="mr-6">
            <Link to={CREATE} className="text-white hover:text-gray-200">
              Create
            </Link>
          </li>
          {/*          <li className="mr-6">
            <Link to="/blogs" className="text-white hover:text-gray-200">
              Blogs
            </Link>
          </li>
          <li>
            <Link to="/contact" className="text-white hover:text-gray-200">
              Contact
            </Link>
          </li>*/}
        </ul>
        {username && userId ? (
          <UserDropdownMenu username={username} userId={userId} />
        ) : (
          <Link to={SIGNIN} className="text-white hover:text-gray-200">
            Login
          </Link>
        )}
      </div>
    </nav>
  )
}

export default Header
