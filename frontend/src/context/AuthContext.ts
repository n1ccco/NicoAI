import { SigninResult } from '@/api/effects/auth/authEffects'
import { User } from '@/types/api'
import { SigninInput, SignupInput } from '@/types/formData'
import { StateDescriptor } from '@/utils/types/state'
import { createContext } from 'react'

type StateTypes = 'authenticated' | 'not-authenticated'

type AuthStateDescriptor<
  TState,
  TStateType extends StateTypes,
> = StateDescriptor<TState, TStateType>

type NotAuthenticatedState = {}

type AuthenticatedState = {
  token: string
  user: User
}

export type AuthState =
  | AuthStateDescriptor<NotAuthenticatedState, 'not-authenticated'>
  | AuthStateDescriptor<AuthenticatedState, 'authenticated'>

export type AuthActions = {
  postSignin: (result: SigninResult) => void
  postLogout: () => void
}

export type AuthContextType = {
  state: AuthState
  actions: AuthActions
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export default AuthContext
