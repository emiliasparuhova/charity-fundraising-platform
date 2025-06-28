import { useSelector } from "react-redux"
import { Navigate } from "react-router-dom"

export const PrivateRoute = ({ children }) => {
  const token = useSelector((state) => state.auth.token)
  return token ? children : <Navigate to="/login" replace />
}

export const PublicRoute = ({ children }) => {
  const token = useSelector((state) => state.auth.token)
  return token ? <Navigate to="/" replace /> : children
}