import { getConfiguration } from '@/configuration/Configuration.ts'

export const API_BASEURL = getConfiguration().apiBaseAddress
export const IMAGES = 'images'
export const USERS = 'users'
export const AUTHSIGNIN = 'auth/signin'
export const AUTHSIGNUP = 'auth/signup'
