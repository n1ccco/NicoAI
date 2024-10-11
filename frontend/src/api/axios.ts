import axios from 'axios'
import { API_BASEURL, API_TIMEOUT } from '@/constants/apiConstants.ts'

const initialConfig = {
  baseURL: API_BASEURL,
  timeout: API_TIMEOUT,
}

const axiosInstance = axios.create({
  ...initialConfig,
})

const axiosInstanceWithCredentials = axios.create({
  ...axiosInstance.defaults,
  withCredentials: true,
})

export { axiosInstance, axiosInstanceWithCredentials }
