import AuthContext, {
  AuthActions,
  AuthContextType,
  AuthState,
} from '@/context/AuthContext'
import { useState } from 'react'
import { UserManagementEntity, TokenManagementEntity } from '@/storage/auth'
import { SigninResult } from '@/api/efects/auth/authEffects'
import { AUTH_TOKEN_NULL_VALUE } from '@/constants/authConstants'

const selectAuthStateFromStorage: () => AuthState = () => {
  const token = TokenManagementEntity.selector()
  if (token === AUTH_TOKEN_NULL_VALUE) {
    return {
      type: 'not-authenticated',
      state: {},
    }
  }
  const user = UserManagementEntity.selector()

  return {
    type: 'authenticated',
    state: {
      token,
      user,
    },
  }
}

type AuthProviderProps = {
  children: React.JSX.Element
}

function AuthProvider({ children }: AuthProviderProps) {
  const [authState, setAuthState] = useState(() => selectAuthStateFromStorage())
  const postSignin = (signinResult: SigninResult) => {
    //Storage patches
    TokenManagementEntity.patcher(signinResult.token)
    UserManagementEntity.patcher(signinResult.user)
    //Enf of storage patches

    //State patches
    setAuthState({
      type: 'authenticated',
      state: {
        token: signinResult.token,
        user: signinResult.user,
      },
    })
    //End of state patched
  }

  const postLogout = () => {
    //Storage patches
    TokenManagementEntity.clear()
    UserManagementEntity.clear()
    //Enf of storage patches

    //State patches
    setAuthState({
      type: 'not-authenticated',
      state: {},
    })
    //End of state patchhed
  }

  const authContextValue = {
    actions: {
      postLogout,
      postSignin,
    },
    state: authState,
  }

  return (
    <AuthContext.Provider value={authContextValue}>
      {children}
    </AuthContext.Provider>
  )
}

export default AuthProvider
