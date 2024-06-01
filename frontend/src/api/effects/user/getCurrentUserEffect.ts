import { axiosInstance } from '@/api/axios'
import { AUTHME } from '@/constants/apiConstants'
import type { EffectResult } from '../types'
import { User } from '@/types/api'

type GetCurrentUserState = {
  user: User
}

type GetCurrentUserResult = EffectResult<GetCurrentUserState>

type GetCurrentUserEffectType = () => Promise<GetCurrentUserResult>

const GetCurrentUserErrorMessage = 'Can not get current user'

const getCurrentUserEffect: GetCurrentUserEffectType = async () => {
  return await axiosInstance
    .get(`${AUTHME}`)
    .then((response): GetCurrentUserResult => {
      const user: User = response.data
      return {
        type: 'success',
        state: { user },
      }
    })
    .catch((_err) => {
      return {
        type: 'failed',
        state: {
          error: GetCurrentUserErrorMessage,
        },
      }
    })
}

export default getCurrentUserEffect
