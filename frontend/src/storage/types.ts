type StorageSelector<T> = () => T

type StoragePatcher<T> = (patchData: T) => void

type StorgageClearer = () => void

type StorageManagementEntity<T, TKey> = {
  selector: StorageSelector<T>
  patcher: StoragePatcher<T>
  clear: StorgageClearer
  key: TKey
}
