import {
  BrowserRouter,
  Navigate,
  Outlet,
  Route,
  Routes,
} from 'react-router-dom'
import Layout from './views/Layout.tsx'
import Home from './views/Home.tsx'
import NoPage from './views/NoPage.tsx'
import Create from './views/Create.tsx'
import Picture from './views/Picture.tsx'
import {
  BASE,
  CREATE,
  PICTURES,
  SIGNIN,
  SIGNUP,
} from './constants/routeContants.ts'
import Signin from './views/Signin.tsx'
import Signup from './views/Signup.tsx'
import AuthProvider from './services/AuthProvider.tsx'
import { useAuth } from './hooks/useAuth.ts'

const PrivateRoute = () => {
  const user = useAuth()
  if (!user.token) return <Navigate to="/signin" />
  return <Outlet />
}

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          <Route path={BASE} element={<Layout />}>
            <Route index element={<Home />} />
            <Route path={`${PICTURES}/:id`} element={<Picture />} />
            <Route element={<PrivateRoute />}>
              <Route path={CREATE} element={<Create />} />
            </Route>
            <Route path={SIGNIN} element={<Signin />} />
            <Route path={SIGNUP} element={<Signup />} />
            <Route path="*" element={<NoPage />} />
          </Route>
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  )
}

export default App
