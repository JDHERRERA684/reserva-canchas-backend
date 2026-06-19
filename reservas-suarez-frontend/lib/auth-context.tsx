"use client"

import { createContext, useContext, useEffect, useState, type ReactNode } from "react"
import { authApi, type LoginResponse } from "./api"

interface AuthContextType {
  user: LoginResponse | null
  isLoading: boolean
  login: (username: string, password: string) => Promise<void>
  logout: () => void
  isAuthenticated: boolean
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<LoginResponse | null>(null)
  const [isLoading, setIsLoading] = useState(true)

  useEffect(() => {
    // Check for existing token on mount
    const token = localStorage.getItem("token")
    const username = localStorage.getItem("username")
    const role = localStorage.getItem("role")
    
    if (token && username && role) {
      setUser({ token, username, role })
    }
    setIsLoading(false)
  }, [])

  const login = async (username: string, password: string) => {
    const response = await authApi.login({ username, password })
    localStorage.setItem("token", response.token)
    localStorage.setItem("username", response.username)
    localStorage.setItem("role", response.role)
    setUser(response)
  }

  const logout = () => {
    localStorage.removeItem("token")
    localStorage.removeItem("username")
    localStorage.removeItem("role")
    setUser(null)
  }

  return (
    <AuthContext.Provider
      value={{
        user,
        isLoading,
        login,
        logout,
        isAuthenticated: !!user,
      }}
    >
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (context === undefined) {
    throw new Error("useAuth must be used within an AuthProvider")
  }
  return context
}
