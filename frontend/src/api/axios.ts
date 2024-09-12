import axios from 'axios'
import { API_BASEURL, API_TIMEOUT } from '@/constants/apiConstants.ts'

const axiosInstance = axios.create({
  baseURL: API_BASEURL,
  timeout: API_TIMEOUT,
  //Controversional
  headers: {
    Accept: 'application/json',
  },
  //TODO Shouldn't be here
  withCredentials: true,
})

export { axiosInstance }
