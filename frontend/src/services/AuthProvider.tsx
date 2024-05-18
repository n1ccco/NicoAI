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
  const [username, setUser] = useState<string>(
    localStorage.getItem('username') || ''
  )
  const [userId, setUserId] = useState<number>(
    parseInt(localStorage.getItem('userId') || '0')
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
        setUserId(res.userId)
        localStorage.setItem('userId', res.userId)
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
    setUser('')
    setUserId(0)
    setToken('')
    localStorage.removeItem('authtoken')
    navigate(CLIENT_BASEURL)
  }

  const authContextValue: AuthContextType = {
    token,
    username,
    userId,
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
