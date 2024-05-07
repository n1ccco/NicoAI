import { createContext } from 'react'
import { User } from '../models/User'

export interface AuthContextType {
  token: string
  user: string | null
  loginAction: (data: User) => void
  registerAction: (data: User) => void
  logOut: () => void
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export default AuthContext
