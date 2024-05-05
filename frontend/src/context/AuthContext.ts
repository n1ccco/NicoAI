import { createContext } from 'react'

export interface User {
  username: string
  password: string
  // Define your user object properties here
}

export interface AuthContextType {
  token: string
  user: User | null
  loginAction: (data: any) => void
  logOut: () => void
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export default AuthContext
