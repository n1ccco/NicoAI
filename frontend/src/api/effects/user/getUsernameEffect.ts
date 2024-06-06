import { axiosInstance } from '@/api/axios'
import { USERS } from '@/constants/apiConstants'
import type { EffectResult } from '../types'

type GetUsernameState = {
  username: string
}

type GetUsernameResult = EffectResult<GetUsernameState>

type GetUsernameEffectType = (id: number) => Promise<GetUsernameResult>

const GetUsernameErrorMessage = 'Can not get username'

const getUsernameEffect: GetUsernameEffectType = async (id) => {
  return await axiosInstance
    .get(`${USERS}/${id}`)
    .then((response): GetUsernameResult => {
      const { username } = response.data
      return {
        type: 'success',
        state: { username },
      }
    })
    .catch((_err) => {
      return {
        type: 'failed',
        state: {
          error: GetUsernameErrorMessage,
        },
      }
    })
}

export default getUsernameEffect
