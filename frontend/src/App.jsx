import { BrowserRouter as Router, Routes, Route } from "react-router-dom"
import { PrivateRoute, PublicRoute } from "./RouteGuards"
import { ToastContainer } from 'react-toastify'
import NavBar from "./components/NavBar/NavBar"
import CharitiesPage from "./pages/CharitiesPage/CharitiesPage"
import CreateCharityPage from "./pages/CreateCharityPage/CreateCharityPage"
import CharityDetailsPage from "./pages/CharityDetailsPage/CharityDetailsPage"
import RegistrationPage from "./pages/RegistrationPage/RegistrationPage"
import DonationSuccessPage from "./pages/DonationSuccessPage/DonationSuccessPage"
import DonationCancelPage from "./pages/DonationCancelPage/DonationCancelPage"
import LoginPage from "./pages/LoginPage/LoginPage"
import "./App.css"
import ProfilePage from "./pages/ProfilePage/ProfilePage"
import UsersPage from "./pages/UsersPage/UsersPage"

function App() {
  return (
    <Router>
      <div className="app">
        <ToastContainer />
        <NavBar />
        <div>
          <Routes>
            <Route path="/" element={<CharitiesPage />} />
            <Route path="/charity/:id" element={<CharityDetailsPage />} />
            <Route path="/profile/:id" element={<ProfilePage />} />

            <Route path="/create-charity" element={
              <PrivateRoute><CreateCharityPage /></PrivateRoute>
            } />
            <Route path="/donation/success" element={
              <PrivateRoute><DonationSuccessPage /></PrivateRoute>
            } />
            <Route path="/donation/cancel" element={
              <PrivateRoute><DonationCancelPage /></PrivateRoute>
            } />
            <Route path="/users" element={
              <PrivateRoute><UsersPage /></PrivateRoute>
            } />

            <Route path="/register" element={
              <PublicRoute><RegistrationPage /></PublicRoute>
            } />
            <Route path="/login" element={
              <PublicRoute><LoginPage /></PublicRoute>
            } />
          </Routes>
        </div>
      </div>
    </Router>
  )
}

export default App
