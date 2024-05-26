import AuthContext from '@/context/AuthContext'
import { axiosInstance } from '@/api/axios'
import { AUTHSIGNIN, AUTHSIGNUP } from '@/constants/apiConstants'
import { AuthContextType } from '@/context/AuthContext'
import { AuthResponse, User } from '@/types/api'
import { SigninInput, SignupInput } from '@/types/formData'
import { useEffect, useState } from 'react'

interface AuthProviderProps {
  children: React.ReactNode
}

function useAuthProvider(): AuthContextType {
  const [user, setUser] = useState<User | null>(null)
  const [token, setToken] = useState<string>('')

  useEffect(() => {
    const storedUser = localStorage.getItem('user')
    if (storedUser) {
      setUser(JSON.parse(storedUser))
    }
    const storedToken = localStorage.getItem('jwt')
    if (storedToken) {
      setToken(storedToken)
    }
  }, [])

  async function loginAction(data: SigninInput) {
    return await axiosInstance
      .post(AUTHSIGNIN, data)
      .then((response): AuthResponse => response.data)
      .then((auth) => {
        localStorage.setItem('jwt', auth.jwt)
        localStorage.setItem('user', JSON.stringify(auth.user))
        setUser(auth.user)
        setToken(auth.jwt)
      })
      .catch((error) => {
        throw new Error(error)
      })
  }

  async function registerAction(data: SignupInput) {
    try {
      await axiosInstance.post(AUTHSIGNUP, data)
    } catch (error) {
      throw new Error('Username is already used')
    }
  }

  function logOut() {
    localStorage.removeItem('jwt')
    localStorage.removeItem('user')
    setUser(null)
    setToken('')
  }

  return {
    token,
    user,
    loginAction,
    registerAction,
    logOut,
  }
}

function AuthProvider({ children }: AuthProviderProps) {
  const authContextValue = useAuthProvider()
  return (
    <AuthContext.Provider value={authContextValue}>
      {children}
    </AuthContext.Provider>
  )
}

export default AuthProvider
