import { Link } from 'react-router-dom'
import { BASE, CREATE } from '../constants/routeContants.ts'

interface HeaderProps {
  name?: string
}

function Header({ name }: HeaderProps) {
  return (
    <nav className="bg-blue-500 p-4">
      <div className="container mx-auto flex items-center justify-between">
        <ul className="flex">
          <li className="mr-6">
            <Link to={BASE} className="text-white hover:text-gray-200">
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
        <div className="text-white">
          <button className="rounded border border-white bg-transparent px-4 py-2 font-semibold text-white hover:border-transparent hover:bg-white hover:text-blue-500">
            User Name
          </button>
        </div>
      </div>
    </nav>
  )
}

export default Header
