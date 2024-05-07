import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { axiosInstance } from '../api/axios'
import AuthContext, { AuthContextType } from '../context/AuthContext'
import { AUTHSIGNIN, AUTHSIGNUP } from '../constants/apiConstants'
import { CLIENT_BASEURL, SIGNIN } from '../constants/routeContants'
import { User } from '../models/User'

const AuthProvider: React.FC<{ children: React.ReactNode }> = ({
  children,
}) => {
  const [user, setUser] = useState<string | null>(
    localStorage.getItem('username') || ''
  )
  const [token, setToken] = useState<string>(
    localStorage.getItem('authtoken') || ''
  )
  const navigate = useNavigate()

  const loginAction = async (data: User) => {
    try {
      const response = await axiosInstance.post(AUTHSIGNIN, data)
      const res = response.data
      console.log(res)
      if (res) {
        setUser(res.username)
        localStorage.setItem('username', res.username)
        setToken(res.token)
        localStorage.setItem('authtoken', res.token)
        navigate(CLIENT_BASEURL)
        return
      }
      throw new Error(res.message)
    } catch (err) {
      console.error(err)
    }
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
    setUser(null)
    setToken('')
    localStorage.removeItem('site')
    navigate(SIGNIN)
  }

  const authContextValue: AuthContextType = {
    token,
    user,
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
