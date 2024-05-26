import { useContext } from 'react'
import type { AuthContextType } from '@/context/AuthContext'
import AuthContext from '@/context/AuthContext'

export function useAuth(): AuthContextType {
  const context = useContext(AuthContext)
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider')
  }
  return context
}
