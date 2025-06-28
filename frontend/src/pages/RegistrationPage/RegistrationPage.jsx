import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import styles from './RegistrationPage.module.css';
import UserService from '../../services/UserService';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const RegistrationPage = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    password: "",
  });
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [showPassword, setShowPassword] = useState(false);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsSubmitting(true);

    try {
      const userData = {
        name: `${formData.firstName} ${formData.lastName}`,
        email: formData.email,
        password: formData.password,
      };

      await UserService.create(userData);
      navigate("/login"); 
    } catch (error) {
      console.error("Error creating user:", error);
      
      if (error.response && error.response.data && error.response.data.message) {
        toast.error(error.response.data.message, {position: "bottom-center", draggable: false});
      } else {
        toast.error("Something went wrong!", {position: "bottom-center", draggable: false});
      }
      
    } finally {
      setIsSubmitting(false);
    }
  };

  const togglePasswordVisibility = () => {
    setShowPassword(!showPassword);
  };

  return (
    <div className={styles.registrationContainer}>
      <div className={styles.registrationCard}>
        <div className={styles.cardHeader}>
          <h1 className={styles.pageTitle}>Create an Account</h1>
          <p className={styles.pageDescription}>
            Join our community and start making a difference today
          </p>
        </div>

        <form className={styles.registrationForm} onSubmit={handleSubmit}>
          <div className={styles.formGroup}>
            <label htmlFor="firstName" className={styles.formLabel}>
              First Name
            </label>
            <div className={styles.inputWrapper}>
              <input
                type="text"
                id="firstName"
                name="firstName"
                value={formData.firstName}
                onChange={handleInputChange}
                className={styles.formInput}
                placeholder="Enter your first name"
                required
              />
              <span className={styles.inputIcon}>👤</span>
            </div>
          </div>

          <div className={styles.formGroup}>
            <label htmlFor="lastName" className={styles.formLabel}>
              Last Name
            </label>
            <div className={styles.inputWrapper}>
              <input
                type="text"
                id="lastName"
                name="lastName"
                value={formData.lastName}
                onChange={handleInputChange}
                className={styles.formInput}
                placeholder="Enter your last name"
                required
              />
              <span className={styles.inputIcon}>👤</span>
            </div>
          </div>

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
              <span className={styles.inputIcon}>✉️</span>
            </div>
          </div>

          <div className={styles.formGroup}>
            <label htmlFor="password" className={styles.formLabel}>
              Password
            </label>
            <div className={styles.inputWrapper}>
              <input
                type={showPassword ? "text" : "password"}
                id="password"
                name="password"
                value={formData.password}
                onChange={handleInputChange}
                className={styles.formInput}
                placeholder="Create a password"
                required
              />
              <button
                type="button"
                className={styles.passwordToggle}
                onClick={togglePasswordVisibility}
              >
                {showPassword ? "👁️" : "👁️‍🗨️"}
              </button>
            </div>
            <div className={styles.passwordStrength}>
              <div className={styles.strengthMeter}>
                <div 
                  className={styles.strengthIndicator} 
                  style={{ 
                    width: `${formData.password.length > 0 ? Math.min(formData.password.length * 10, 100) : 0}%` ,
                    backgroundColor: formData.password.length > 8 ? '#22c55e' : formData.password.length > 4 ? '#f59e0b' : '#ef4444'
                }}
                ></div>
              </div>
              <span className={styles.strengthText}>
                {formData.password.length === 0 && 'Password strength'}
                {formData.password.length > 0 && formData.password.length <= 4 && 'Weak password'}
                {formData.password.length > 4 && formData.password.length <= 8 && 'Medium password'}
                {formData.password.length > 8 && 'Strong password'}
              </span>
            </div>
          </div>

          <div className={styles.termsCheckbox}>
            <input type="checkbox" id="terms" required />
            <label htmlFor="terms">
              I agree to the <Link to="/terms" className={styles.termsLink}>Terms of Service</Link> and <Link to="/privacy" className={styles.termsLink}>Privacy Policy</Link>
            </label>
          </div>

          <button
            type="submit"
            className={styles.registerButton}
            disabled={isSubmitting}
          >
            {isSubmitting ? 'Creating Account...' : 'Create Account'}
          </button>
        </form>

        <div className={styles.loginLink}>
          Already have an account? <Link to="/login">Log in</Link>
        </div>
      </div>

      <div className={styles.registrationBackground}>
        <div className={styles.backgroundContent}>
          <h2>Join thousands of people making a difference</h2>
          <div className={styles.testimonials}>
            <div className={styles.testimonial}>
              <div className={styles.testimonialContent}>
                "Fundify made it so easy to start my fundraising campaign. I raised over $10,000 for my cause in just two weeks!"
              </div>
              <div className={styles.testimonialAuthor}>
                <div className={styles.authorAvatar}>JD</div>
                <div className={styles.authorInfo}>
                  <div className={styles.authorName}>Jane Doe</div>
                  <div className={styles.authorRole}>Campaign Creator</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default RegistrationPage;
