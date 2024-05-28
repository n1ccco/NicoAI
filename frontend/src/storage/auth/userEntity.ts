import {
  AUTH_USER_FIELD,
  AUTH_USER_NULL_VALUE,
} from '@/constants/authConstants'
import type { User } from '@/types/api'

const patchUserToStorage = (user: User) =>
  localStorage.setItem('user', JSON.stringify(user))

const selectUserFromStorage: StorageSelector<User> = () => {
  const serializedUserData = localStorage.getItem(AUTH_USER_FIELD)
  if (!serializedUserData) {
    return AUTH_USER_NULL_VALUE
  }
  return JSON.parse(serializedUserData)
}

const clearUserFromStorage = () => localStorage.removeItem(AUTH_USER_FIELD)

export const UserManagementEntity: StorageManagementEntity<
  User,
  typeof AUTH_USER_FIELD
> = {
  key: AUTH_USER_FIELD,
  selector: selectUserFromStorage,
  patcher: patchUserToStorage,
  clear: clearUserFromStorage,
}
