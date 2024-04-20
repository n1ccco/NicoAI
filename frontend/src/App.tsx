import { BrowserRouter, Route, Routes } from 'react-router-dom'
import Layout from './views/Layout.tsx'
import Home from './views/Home.tsx'
import NoPage from './views/NoPage.tsx'
import Create from './views/Create.tsx'
import Picture from './views/Picture.tsx'
import { BASE, CREATE, PICTURE } from './constants/routeContants.ts'

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path={BASE} element={<Layout />}>
          <Route index element={<Home />} />
          <Route path={`${PICTURE}/:id`} element={<Picture />} />
          <Route path={CREATE} element={<Create />} />
          {/*<Route path="blogs" element={<Blogs />} />
          <Route path="contact" element={<Contact />} />*/}
          <Route path="*" element={<NoPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  )
}

export default App
