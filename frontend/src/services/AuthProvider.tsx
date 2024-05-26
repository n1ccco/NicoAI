import { useNavigate } from 'react-router-dom'
import { axiosInstance } from '../api/axios'
import AuthContext, { AuthContextType } from '../context/AuthContext'
import { AUTHSIGNIN, AUTHSIGNUP } from '../constants/apiConstants'
import { CLIENT_BASEURL, GALLERY, SIGNIN } from '../constants/routeContants'
import { AuthResponse, User } from '@/types/api'

const AuthProvider: React.FC<{ children: React.ReactNode }> = ({
  children,
}) => {
  const navigate = useNavigate()

  const getAuth = (): AuthResponse | null => {
    const auth = localStorage.getItem('auth')
    if (auth) {
      try {
        return JSON.parse(auth)
      } catch (error) {
        console.error('Error parsing data from localStorage', error)
        return null
      }
    }
    return null
  }

  const loginAction = async (data: User) => {
    return await axiosInstance
      .post(AUTHSIGNIN, data)
      .then((response) => {
        if (response.status < 200 || response.status >= 300) {
          throw new Error(response.statusText)
        }
        navigate(GALLERY)
        return response.data
      })
      .then((auth) => {
        localStorage.setItem('auth', JSON.stringify(auth))
      })
      .catch(() => {
        throw new Error('Network Error')
      })
  }

  const registerAction = async (data: User) => {
    try {
      const response = await axiosInstance.post(AUTHSIGNUP, data)
      const res = response.data
      navigate(SIGNIN)
      throw new Error(res.message)
    } catch (err) {
      console.error(err)
    }
  }

  const logOut = () => {
    localStorage.removeItem('auth')
    navigate(CLIENT_BASEURL)
  }

  const authContextValue: AuthContextType = {
    getAuth,
    loginAction,
    registerAction,
    logOut,
  }

  return (
    <AuthContext.Provider value={authContextValue}>
      {children}
    </AuthContext.Provider>
  )
}

export default AuthProvider
