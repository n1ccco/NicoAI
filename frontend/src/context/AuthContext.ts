import { User } from '@/types/api'
import { SigninInput, SignupInput } from '@/types/formData'
import { createContext } from 'react'

export interface AuthContextType {
  token: string
  user: User | null
  loginAction: (data: SigninInput) => void
  registerAction: (data: SignupInput) => void
  logOut: () => void
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export default AuthContext
