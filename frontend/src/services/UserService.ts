import { axiosInstance } from '@/api/axios'
import { AUTHME } from '@/constants/apiConstants'
import { User } from '@/types/api'

export async function getCurrentUser(): Promise<User | undefined> {
  try {
    const response = await axiosInstance.get(`${AUTHME}`)
    const user: User = response.data
    return user
  } catch (error) {
    console.error('Error fetching data:', error)
  }
}
