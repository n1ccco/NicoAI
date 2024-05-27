import axios from 'axios'
import { API_BASEURL } from '@/constants/apiConstants.ts'
import { useNavigate } from 'react-router-dom'
import { SIGNIN } from '@/constants/routeContants'

const axiosInstance = axios.create({
  baseURL: API_BASEURL,
  timeout: 200000,
  headers: {
    Accept: 'application/json',
  },
})

axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('jwt')
    if (token) {
      try {
        config.headers.Authorization = `Bearer ${token}`
      } catch (error) {
        console.error('Error parsing auth from localStorage', error)
      }
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      const navigate = useNavigate()
      navigate(SIGNIN)
    }
    return Promise.reject(error)
  }
)

export { axiosInstance }
