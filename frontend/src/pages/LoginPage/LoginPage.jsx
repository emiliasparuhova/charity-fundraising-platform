import { useState } from "react"
import { useDispatch } from "react-redux"
import { useNavigate, Link } from "react-router-dom"
import styles from "./LoginPage.module.css"
import { createAuthSuccess } from "../../actions/authenticationActions"
import { toast } from "react-toastify"
import "react-toastify/dist/ReactToastify.css"
import AuthenticationService from "../../services/AuthenticationService"

const LoginPage = () => {
  const dispatch = useDispatch()
  const navigate = useNavigate()
  const [formData, setFormData] = useState({
    email: "",
    plainTextPassword: "",
  })
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [showPassword, setShowPassword] = useState(false)

  const handleInputChange = (e) => {
    const { name, value } = e.target
    setFormData({
      ...formData,
      [name]: value,
    })
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setIsSubmitting(true)

    try {
      const response = await AuthenticationService.create(formData)

      if (response.data && response.data.accessToken) {
        const token = response.data.accessToken
        dispatch(createAuthSuccess({ token }))
        navigate("/")
      }
    } catch (error) {
      console.error("Error logging in:", error)

      if (error.response && error.response.status === 401) {
        toast.error("Invalid credentials", {
          position: "bottom-center",
          draggable: false
        })
      } else {
        toast.error("Something went wrong!", {
          position: "bottom-center",
          draggable: false
        })
      }
    } finally {
      setIsSubmitting(false)
    }
  }

  const togglePasswordVisibility = () => {
    setShowPassword(!showPassword)
  }

  return (
    <div className={styles.loginContainer}>
      <div className={styles.loginCard}>
        <div className={styles.cardHeader}>
          <h1 className={styles.pageTitle}>Sign In</h1>
          <p className={styles.pageDescription}>Enter your credentials to access your account</p>
        </div>

        <form className={styles.loginForm} onSubmit={handleSubmit}>
          <div className={styles.formGroup}>
            <label htmlFor="email" className={styles.formLabel}>
              Email Address
            </label>
            <div className={styles.inputWrapper}>
              <input
                type="email"
                id="email"
                name="email"
                value={formData.email}
                onChange={handleInputChange}
                className={styles.formInput}
                placeholder="Enter your email address"
                required
              />
            </div>
          </div>

          <div className={styles.formGroup}>
            <div className={styles.passwordHeader}>
              <label htmlFor="plainTextPassword" className={styles.formLabel}>
                Password
              </label>
              <Link to="/forgot-password" className={styles.forgotPassword}>
                Forgot Password?
              </Link>
            </div>
            <div className={styles.inputWrapper}>
              <input
                type={showPassword ? "text" : "password"}
                id="plainTextPassword"
                name="plainTextPassword"
                value={formData.plainTextPassword}
                onChange={handleInputChange}
                className={styles.formInput}
                placeholder="Enter your password"
                required
              />
              <button type="button" className={styles.passwordToggle} onClick={togglePasswordVisibility}>
                {showPassword ? "👁️" : "👁️‍🗨️"}
              </button>
            </div>
          </div>

          <div className={styles.rememberMe}>
            <input type="checkbox" id="remember" />
            <label htmlFor="remember">Remember me</label>
          </div>

          <button type="submit" className={styles.loginButton} disabled={isSubmitting}>
            {isSubmitting ? "Signing In..." : "Sign In"}
          </button>
        </form>

        <div className={styles.registerLink}>
          Don't have an account?{" "}
          <Link to="/register" className={styles.registerButton}>
            Sign Up
          </Link>
        </div>
      </div>
    </div>
  )
}

export default LoginPage