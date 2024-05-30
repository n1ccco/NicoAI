import {
  AUTH_TOKEN_FIELD,
  AUTH_TOKEN_NULL_VALUE,
} from '@/constants/authConstants'

const selectTokenFromStorage: StorageSelector<string> = () =>
  localStorage.getItem(AUTH_TOKEN_FIELD) ?? AUTH_TOKEN_NULL_VALUE

const patchTokenToStorage = (token: string) =>
  localStorage.setItem(AUTH_TOKEN_FIELD, token)

const clearAuthToken = () => {
  localStorage.removeItem(AUTH_TOKEN_FIELD)
}

export const TokenManagementEntity: StorageManagementEntity<
  string,
  typeof AUTH_TOKEN_FIELD
> = {
  key: AUTH_TOKEN_FIELD,
  selector: selectTokenFromStorage,
  patcher: patchTokenToStorage,
  clear: clearAuthToken,
}
