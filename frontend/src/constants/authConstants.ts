import { User } from '@/types/api'

export const AUTH_TOKEN_FIELD = 'jwt'
export const AUTH_USER_FIELD = 'user'

export const AUTH_TOKEN_NULL_VALUE = ''
export const AUTH_USER_NULL_VALUE: User = {
  id: -1,
  username: 'NULL_VALUE',
}
