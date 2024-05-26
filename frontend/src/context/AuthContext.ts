import { AuthResponse, User } from '@/types/api'
import { createContext } from 'react'

export interface AuthContextType {
  getAuth: () => AuthResponse | null
  loginAction: (data: User) => void
  registerAction: (data: User) => void
  logOut: () => void
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export default AuthContext
