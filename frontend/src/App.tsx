import {
  BrowserRouter,
  Navigate,
  Outlet,
  Route,
  Routes,
} from 'react-router-dom'
import Layout from '@/views/core/Layout.tsx'
import Home from '@/views/Home.tsx'
import NoPage from '@/views/core/NoPage.tsx'
import Create from '@/views/Create.tsx'
import Picture from '@/views/Picture.tsx'
import {
  CLIENT_BASEURL,
  CREATE,
  GALLERY,
  IMAGES,
  SIGNIN,
  SIGNUP,
  USERS,
} from '@/constants/routeContants.ts'
import Signin from '@/views/auth/Signin.tsx'
import Signup from '@/views/auth/Signup.tsx'
import AuthProvider from '@/services/AuthProvider.tsx'
import { useAuth } from '@/hooks/useAuth.ts'
import UserImages from '@/views/UserImages.tsx'
import Gallery from '@/views/Gallery.tsx'

const PrivateRoute = () => {
  const jwt = useAuth().token
  if (!jwt) return <Navigate to={SIGNIN} />
  return <Outlet />
}

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          <Route index element={<Home />} />
          <Route path={CLIENT_BASEURL} element={<Layout />}>
            <Route path={GALLERY} element={<Gallery />} />
            <Route path={`${IMAGES}/:id`} element={<Picture />} />
            <Route element={<PrivateRoute />}>
              <Route path={CREATE} element={<Create />} />
              <Route path={`${USERS}/:id/${IMAGES}`} element={<UserImages />} />
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
