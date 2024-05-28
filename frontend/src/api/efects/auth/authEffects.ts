import { axiosInstance } from '@/api/axios'
import { AUTHSIGNIN, AUTHSIGNUP } from '@/constants/apiConstants'
import type { SigninInput, SignupInput } from '@/types/formData'
import type { EffectResult } from '../types'
import { AuthResponse, User } from '@/types/api'

const StateEmpty = {}

type RegisterEffectType = (
  registerPayload: SignupInput
) => Promise<EffectResult>

const RegisterErrorMessage = 'Username is already used'

export const registerEffect: RegisterEffectType = async (registerPayload) => {
  return axiosInstance
    .post(AUTHSIGNUP, registerPayload)
    .then(
      (_res): EffectResult => ({
        type: 'success',
        state: StateEmpty,
      })
    )
    .catch((err) => {
      console.error(err)
      return {
        type: 'failed',
        state: {
          error: RegisterErrorMessage,
        },
      }
    })
}

export type SigninResult = {
  user: User
  token: string
}

type LoginResult = EffectResult<SigninResult>

type LoginEffectType = (loginPayload: SigninInput) => Promise<LoginResult>

const LoginErrorMessage = 'Invalid username or password'

export const loginEffect: LoginEffectType = (signInResult) => {
  return axiosInstance
    .post(AUTHSIGNIN, signInResult)
    .then((response): LoginResult => {
      const castedResponse = response.data as AuthResponse
      return {
        type: 'success',
        state: {
          user: castedResponse.user,
          token: castedResponse.jwt,
        },
      }
    })
    .catch((error) => {
      console.error(error)
      return {
        type: 'failed',
        state: {
          error: LoginErrorMessage,
        },
      }
    })
}
