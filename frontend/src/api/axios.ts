import axios from 'axios'
import { API_BASEURL } from '../constants/apiConstants.ts'

const axiosInstance = axios.create({
  baseURL: API_BASEURL,
  timeout: 200000,
  headers: {
    Accept: 'application/json',
  },
})

axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('site')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

export { axiosInstance }
