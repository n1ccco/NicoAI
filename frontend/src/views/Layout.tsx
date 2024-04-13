import { Outlet } from 'react-router-dom'
import Header from '../components/Header.tsx'
import { getConfiguration } from '../configuration/Configuration.ts'

const Layout = () => {
  const appName = getConfiguration().applicationName
  return (
    <>
      <p>{appName}</p>
      <Header />
      <Outlet />
    </>
  )
}

export default Layout
