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
    const authString = localStorage.getItem('auth')
    if (authString) {
      try {
        const auth = JSON.parse(authString) // Parse the stringified auth data
        const token = auth.jwt // Assuming your auth object has a token property
        if (token) {
          config.headers.Authorization = `Bearer ${token}` // Set the Authorization header
        }
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
