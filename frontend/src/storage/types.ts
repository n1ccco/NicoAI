type StorageSelector<T> = () => T

type StoragePatcher<T> = (patchData: T) => void

type StorageCleaner = () => void

type StorageManagementEntity<T, TKey> = {
  selector: StorageSelector<T>
  patcher: StoragePatcher<T>
  clear: StorageCleaner
  key: TKey
}
