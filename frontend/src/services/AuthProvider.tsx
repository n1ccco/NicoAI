import AuthContext, { AuthState } from '@/context/AuthContext'
import { useEffect, useState } from 'react'
import { TokenManagementEntity, UserManagementEntity } from '@/storage/auth'
import { refreshTokenEffect, SigninResult } from '@/api/effects/auth/authEffects'
import { AUTH_TOKEN_NULL_VALUE } from '@/constants/authConstants'
import { AxiosInstance } from 'axios'
import { axiosInstance } from '@/api/axios'
import { RefreshTokenData } from '@/types/api.ts'

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
  const [authState, setAuthState] = useState<AuthState>(() =>
    selectAuthStateFromStorage()
  )

  useEffect(() => {
    // Setup Axios interceptors with postRefresh and postLogout
    setupInterceptors(axiosInstance, postRefresh, postLogout)
  }, [])

  if (authState.type === 'authenticated') {
    applyAuthorizationToAxios(authState.state.token, axiosInstance)
  } else {
    applyAuthorizationToAxios(null, axiosInstance)
  }

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
    applyAuthorizationToAxios(signinResult.token, axiosInstance)
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
    //End of state patched
    applyAuthorizationToAxios(null, axiosInstance)
  }

  const postRefresh = (result: RefreshTokenData) => {
    TokenManagementEntity.patcher(result.token)

    setAuthState({
      type: 'authenticated',
      state: {
        token: result.token,
        user: UserManagementEntity.selector(),
      },
    })

    applyAuthorizationToAxios(result.token, axiosInstance)
  }

  const authContextValue = {
    actions: {
      postLogout,
      postSignin,
      postRefresh,
    },
    state: authState,
  }

  return (
    <AuthContext.Provider value={authContextValue}>
      {children}
    </AuthContext.Provider>
  )
}

const applyAuthorizationToAxios = (
  token: string | null,
  axiosInstance: AxiosInstance
): void => {
  if (token === null) {
    delete axiosInstance.defaults.headers.common['Authorization']
  } else {
    axiosInstance.defaults.headers.common['Authorization'] = `Bearer ${token}`
  }
}

const setupInterceptors = (axiosInstance: AxiosInstance, postRefresh: (result: RefreshTokenData) => void, postLogout: () => void): () => void => {
  const refreshInterceptor = axiosInstance.interceptors.response.use((response) => response, async (error) => {
    const originalRequest = error.config

    // Check if the request has already been retried
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true // Mark the request as retried
      try {
        const res = await refreshTokenEffect()
        if (res.type === 'success') {
          postRefresh(res.state) // Refresh tokens and update state
          // Retry the original request with the new token
          originalRequest.headers['Authorization'] = `Bearer ${res.state.token}`
          return axiosInstance(originalRequest)
        } else {
          console.log(res.state.error)
          postLogout() // Logout if refresh fails
        }
      } catch {
        postLogout() // Logout if refresh fails
      }
    }

    // If the request has already been retried or another error, reject it
    return Promise.reject(error)
  })

  return () => {
    axiosInstance.interceptors.response.eject(refreshInterceptor)
  }
}

export default AuthProvider
