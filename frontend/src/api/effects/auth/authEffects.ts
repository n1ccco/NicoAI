import { axiosInstance, axiosInstanceWithCredentials } from '@/api/axios'
import { AUTHSIGNIN, AUTHSIGNUP, REFRESHTOKEN } from '@/constants/apiConstants'
import type { SigninInput, SignupInput } from '@/types/formData'
import type { EffectResult } from '../types'
import { AuthResponse, RefreshTokenData, User } from '@/types/api'

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
  return axiosInstanceWithCredentials
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

export type RefreshTokenResult = EffectResult<RefreshTokenData>;

const RefreshTokenErrorMessage = 'Couldn\'t refresh your token. Try to login'

export const refreshTokenEffect = (): Promise<RefreshTokenResult> => {
  return axiosInstanceWithCredentials
    .post(REFRESHTOKEN)
    .then(response => {
      const castedResponse = response.data as RefreshTokenData
      return {
        type: 'success',
        state: {
          token: castedResponse.token,
        },
      } as RefreshTokenResult
    })
    .catch(error => {
      console.error(error)
      return {
        type: 'failed',
        state: {
          error: RefreshTokenErrorMessage,
        },
      } as RefreshTokenResult
    })
}