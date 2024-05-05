import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { axiosInstance } from '../api/axios'
import AuthContext, { AuthContextType, User } from '../context/AuthContext'

const AuthProvider: React.FC<{ children: React.ReactNode }> = ({
  children,
}) => {
  const [user, setUser] = useState<User | null>(null)
  const [token, setToken] = useState<string>(localStorage.getItem('site') || '')
  const navigate = useNavigate()

  const loginAction = async (data: any) => {
    try {
      const response = await axiosInstance.post('auth/signin', data)
      const res = response.data
      console.log(res)
      if (res) {
        setUser(res.username)
        setToken(res.token)
        localStorage.setItem('site', res.token)
        navigate('/')
        return
      }
      throw new Error(res.message)
    } catch (err) {
      console.error(err)
    }
  }

  const logOut = () => {
    setUser(null)
    setToken('')
    localStorage.removeItem('site')
    navigate('/signin')
  }

  const authContextValue: AuthContextType = {
    token,
    user,
    loginAction,
    logOut,
  }

  return (
    <AuthContext.Provider value={authContextValue}>
      {children}
    </AuthContext.Provider>
  )
}

export default AuthProvider
