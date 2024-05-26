import axios from 'axios'
import { API_BASEURL } from '@/constants/apiConstants.ts'

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
        // Assuming your auth object has a token property
        config.headers.Authorization = `Bearer ${token}` // Set the Authorization header
      } catch (error) {
        console.error('Error parsing auth from localStorage', error)
        // Optionally, you might want to clear invalid auth data:
        // localStorage.removeItem('auth');
      }
    }
    return config // Return the config object with or without the Authorization header
  },
  (error) => {
    return Promise.reject(error) // Handle request error
  }
)

export { axiosInstance }
