import { getConfiguration } from '@/configuration/Configuration.ts'

export const API_BASEURL = getConfiguration().apiBaseAddress
export const API_TIMEOUT = 200000
export const IMAGES = 'images'
export const USERS = 'users'
export const COMMENTS = 'comments'
export const AUTHSIGNIN = 'auth/signin'
export const AUTHSIGNUP = 'auth/signup'
export const AUTHME = 'auth/me'
export const REFRESHTOKEN = 'auth/refresh'
