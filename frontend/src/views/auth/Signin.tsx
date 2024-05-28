import React, { ChangeEvent, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '@/hooks/useAuth'
import { GALLERY, SIGNUP } from '@/constants/routeContants'
import { SigninInput } from '@/types/formData'
import { loginEffect } from '@/api/efects/auth/authEffects'

const Signin = () => {
  const {actions: {postSignin}} = useAuth()

  const navigate = useNavigate()
  const [input, setInput] = useState<SigninInput>({
    username: '',
    password: '',
  })
  const [error, setError] = useState('')

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()

    if(!!input.username && !!input.password){
      loginEffect(input).then(res => {
        if(res.type === "success"){
         postSignin(res.state);
         navigate(`/${GALLERY}`);
        }
        else{
          setError(res.state.error)
        }
      });
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
    <div className="flex min-h-screen items-center justify-center">
      <div className="w-full max-w-md rounded-lg bg-gray-800 p-8 shadow-lg">
        <h2 className="mb-6 text-center text-3xl font-bold">Sign In</h2>
        <form onSubmit={handleSubmit} className="space-y-6">
          <div>
            <label htmlFor="username" className="block text-xl font-medium">
              Username
            </label>
            <input
              type="text"
              id="username"
              name="username"
              className="mt-2 w-full rounded-lg bg-gray-700 p-3 text-gray-200 focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="Type your username"
              onChange={handleInput}
              required
            />
          </div>
          <div>
            <label htmlFor="password" className="block text-xl font-medium">
              Password
            </label>
            <input
              type="password"
              id="password"
              name="password"
              className="mt-2 w-full rounded-lg bg-gray-700 p-3 text-gray-200 focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="********"
              onChange={handleInput}
              required
            />
          </div>
          {error && <p className="mb-2 text-red-500">{error}</p>}
          <button
            type="submit"
            className="w-full rounded-lg bg-blue-500 p-3 text-white transition duration-300 hover:bg-blue-600"
          >
            Sign In
          </button>
        </form>
        <p className="mt-6 text-center text-sm text-gray-400">
          Don't have an account?&nbsp;
          <Link className="text-blue-500 hover:text-blue-600" to={`/${SIGNUP}`}>
            Register
          </Link>
        </p>
      </div>
    </div>
  )
}

export default Signin
