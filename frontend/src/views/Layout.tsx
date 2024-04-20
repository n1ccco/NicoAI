import { Outlet } from 'react-router-dom'
import Header from '../components/Header.tsx'
import { getConfiguration } from '../configuration/Configuration.ts'

const Layout = () => {
  const appName = getConfiguration().applicationName
  return (
    <>
      <Header name={appName} />
      <Outlet />
    </>
  )
}

export default Layout
