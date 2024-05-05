import React, { ChangeEvent, useState } from 'react'
import { Link } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'

const Signin = () => {
  const auth = useAuth()
  const [input, setInput] = useState({
    username: '',
    password: '',
  })
  const [error, setError] = useState('')

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      if (input.username !== '' && input.password !== '') {
        auth.loginAction(input)
        return
      }
      alert('pleae provide a valid input')
    } catch (error) {
      setError('Invalid username or password')
    }
  }

  const handleInput = (e: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target
    setInput((prev) => ({
      ...prev,
      [name]: value,
    }))
  }

  return (
    <div className="flex min-h-screen items-center justify-center bg-gray-100">
      <div className="rounded bg-white p-8 shadow-md">
        <h2 className="mb-4 text-2xl font-semibold">Sign In</h2>
        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <label htmlFor="username" className="block text-gray-700">
              Username
            </label>
            <input
              type="text"
              id="username"
              name="username"
              className="form-input mt-1 block w-full"
              onChange={handleInput}
            />
          </div>
          <div className="mb-4">
            <label htmlFor="password" className="block text-gray-700">
              Password
            </label>
            <input
              type="password"
              id="password"
              name="password"
              className="form-input mt-1 block w-full"
              onChange={handleInput}
            />
          </div>
          {error && <p className="mb-2 text-red-500">{error}</p>}
          <button
            type="submit"
            className="w-full rounded bg-blue-500 px-4 py-2 font-semibold text-white"
          >
            Sign In
          </button>
        </form>
        <Link to="/signup">Don't have an account? Register</Link>
      </div>
    </div>
  )
}

export default Signin
