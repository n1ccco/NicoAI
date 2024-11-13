export type StorageSelector<T> = () => T;

export type StoragePatcher<T> = (patchData: T) => void;

export type StorageCleaner = () => void;

export type StorageManagementEntity<T, TKey> = {
  selector: StorageSelector<T>;
  patcher: StoragePatcher<T>;
  clear: StorageCleaner;
  key: TKey;
};
