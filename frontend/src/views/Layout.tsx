import { Outlet } from 'react-router-dom'
import Header from '../components/Header.tsx'
import { getConfiguration } from '../configuration/Configuration.ts'
import Footer from '../components/Footer.tsx'

const Layout = () => {
  const appName = getConfiguration().applicationName
  return (
    <>
      <div className="flex min-h-screen flex-col bg-gray-900 font-sans text-gray-200">
        <Header name={appName} />
        <div className="flex-grow">
          <Outlet />
        </div>
        <Footer />
      </div>
    </>
  )
}

export default Layout
