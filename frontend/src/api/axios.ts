import axios from 'axios'
import { BASE_URL } from '../constants/urlConstants.ts'

const axiosInstance = axios.create({
  baseURL: BASE_URL,
  timeout: 200000,
  headers: {
    Accept: 'application/json',
  },
})

export { axiosInstance }
