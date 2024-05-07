import { ChangeEvent, useState } from 'react'
import { useAuth } from '../hooks/useAuth'

const Signup = () => {
  const [input, setInput] = useState({
    username: '',
    password: '',
    confirmPassword: '',
  })
  const auth = useAuth()
  const [error, setError] = useState('')

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      if (
        input.username !== '' &&
        input.password !== '' &&
        input.password === input.confirmPassword
      ) {
        const { confirmPassword, ...model } = input
        auth.registerAction(model)
      }
      setError('Input correct data')
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
        <h2 className="mb-4 text-2xl font-semibold">Sign Up</h2>
        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <label htmlFor="username" className="block text-gray-700">
              Name
            </label>
            <input
              type="text"
              id="username"
              name="username"
              className="form-input mt-1 block w-full rounded-md"
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
              className="form-input mt-1 block w-full rounded-md"
              onChange={handleInput}
            />
          </div>
          <div className="mb-4">
            <label htmlFor="confirmPassword" className="block text-gray-700">
              Confirm Password
            </label>
            <input
              type="password"
              id="confirmPassword"
              name="confirmPassword"
              className="form-input mt-1 block w-full rounded-md border-gray-300"
              onChange={handleInput}
            />
          </div>
          {error && <p className="mb-2 text-red-500">{error}</p>}
          <button
            type="submit"
            className="w-full rounded bg-blue-500 px-4 py-2 font-semibold text-white"
          >
            Sign Up
          </button>
        </form>
      </div>
    </div>
  )
}

export default Signup
