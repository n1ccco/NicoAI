import { BrowserRouter, Route, Routes } from 'react-router-dom'
import Layout from './views/Layout.tsx'
import Home from './views/Home.tsx'
import Blogs from './views/Blogs.tsx'
import Contact from './views/Contact.tsx'
import NoPage from './views/NoPage.tsx'
import Create from './views/Create.tsx'

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Layout />}>
          <Route index element={<Home />} />
          <Route path="create" element={<Create />} />
          <Route path="blogs" element={<Blogs />} />
          <Route path="contact" element={<Contact />} />
          <Route path="*" element={<NoPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  )
}

export default App
