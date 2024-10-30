import {
  StorageCleaner,
  StorageManagementEntity,
  StoragePatcher,
  StorageSelector,
} from '@/lib/store/types';

const AUTH_TOKEN_FIELD = 'jwt';
const AUTH_TOKEN_NULL_VALUE = '';

const selectTokenFromStorage: StorageSelector<string> = () =>
  localStorage.getItem(AUTH_TOKEN_FIELD) ?? AUTH_TOKEN_NULL_VALUE;

const patchTokenToStorage: StoragePatcher<string> = (token: string) =>
  localStorage.setItem(AUTH_TOKEN_FIELD, token);

const clearAuthToken: StorageCleaner = () => {
  localStorage.removeItem(AUTH_TOKEN_FIELD);
};

export const TokenManagementEntity: StorageManagementEntity<
  string,
  typeof AUTH_TOKEN_FIELD
> = {
  key: AUTH_TOKEN_FIELD,
  selector: selectTokenFromStorage,
  patcher: patchTokenToStorage,
  clear: clearAuthToken,
};
