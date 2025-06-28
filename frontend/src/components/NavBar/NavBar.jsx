import { Link } from "react-router-dom"
import { useState, useEffect, useRef } from "react"
import { useLocation } from "react-router-dom"
import styles from "./NavBar.module.css"
import { useDispatch, useSelector } from "react-redux"
import { logout } from "../../actions/authenticationActions"
import { Avatar, Menu, MenuItem, Fade } from "@mui/material"
import PersonIcon from "@mui/icons-material/Person"
import LogoutIcon from "@mui/icons-material/Logout"
import { jwtDecode } from "jwt-decode"
import UserService from "../../services/UserService"

const NavBar = () => {
  const accessToken = useSelector((state) => state.auth.token)

  const [isScrolled, setIsScrolled] = useState(false)
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false)
  const [profileMenuOpen, setProfileMenuOpen] = useState(false)
  const [mobileProfileMenuOpen, setMobileProfileMenuOpen] = useState(false)
  const [profilePicture, setProfilePicture] = useState(null)
  const [userRole, setUserRole] = useState(null);
  const [imageError, setImageError] = useState(false);
  const [userName, setUserName] = useState("");


  const location = useLocation()
  const dispatch = useDispatch()
  const profileRef = useRef(null)
  const mobileProfileRef = useRef(null)

  let decodedToken, userId
  if (accessToken) {
    decodedToken = jwtDecode(accessToken)
    userId = decodedToken.userId
  }

  useEffect(() => {
    const handleScroll = () => {
      setIsScrolled(window.scrollY > 20)
    }

    window.addEventListener("scroll", handleScroll)
    return () => {
      window.removeEventListener("scroll", handleScroll)
    }
  }, [])

  useEffect(() => {
    if (accessToken) {
      const decoded = jwtDecode(accessToken)
      const userId = decoded.userId

      UserService.get(userId)
        .then((response) => {
          const { profilePicture, role, name } = response.data
          if (profilePicture) {
            setProfilePicture(`data:image/jpeg;base64,${profilePicture}`)
          }
          setUserRole(role)
          setUserName(name)
          setImageError(false)
        })
        .catch((err) => {
          console.error("Failed to fetch user profile", err)
        })
    }
  }, [accessToken])

  const toggleMobileMenu = () => setIsMobileMenuOpen(!isMobileMenuOpen)
  const closeMobileMenu = () => setIsMobileMenuOpen(false)
  const isActive = (path) => location.pathname === path

  const handleLogout = () => {
    dispatch(logout())
    setProfileMenuOpen(false)
    setMobileProfileMenuOpen(false)
    closeMobileMenu()
  }

  const getInitials = (name) => {
    if (!name) return "U";
    return name
      .split(" ")
      .map((word) => word.charAt(0))
      .join("")
      .toUpperCase()
      .substring(0, 2);
  };

  const handleOpenProfileMenu = () => setProfileMenuOpen(true)
  const handleCloseProfileMenu = () => setProfileMenuOpen(false)

  const handleOpenMobileProfileMenu = () => setMobileProfileMenuOpen(true)
  const handleCloseMobileProfileMenu = () => setMobileProfileMenuOpen(false)

  return (
    <nav className={`${styles.navbar} ${isScrolled ? styles.scrolled : ""}`}>
      <div className={styles.navContainer}>
        <Link to="/" className={styles.logo}>
          <img src="/images/logo.png" alt="Logo" className={styles.logoImage} />
        </Link>

        <div className={styles.navRight}>
          <div className={styles.navButtons}>
            {accessToken && userRole === "ADMIN" ? (
              <Link to="/users" className={styles.createButton} onClick={closeMobileMenu}>
                Users
              </Link>
            ) : (
              <Link to="/create-charity" className={styles.createButton} onClick={closeMobileMenu}>
                Start Fundraising
              </Link>
            )}

            {!accessToken && (
              <Link to="/login" className={styles.loginButton} onClick={closeMobileMenu}>
                Log In
              </Link>
            )}

            {accessToken && (
              <div className={styles.profileContainer}>
                <div className={styles.profilePicture} ref={profileRef} onClick={handleOpenProfileMenu}>
                  {profilePicture && !imageError ? (
                    <img
                      src={profilePicture}
                      alt="Profile"
                      className={styles.profileImage}
                      onError={() => setImageError(true)}
                    />
                  ) : (
                    <div className={styles.profileInitials}>
                      {getInitials(userName)}
                    </div>
                  )}
                </div>
                <Menu
                  id="profile-menu"
                  anchorEl={profileRef.current}
                  open={profileMenuOpen}
                  onClose={handleCloseProfileMenu}
                  className={styles.profileMenu}
                  anchorOrigin={{
                    vertical: "bottom",
                    horizontal: "right",
                  }}
                  transformOrigin={{
                    vertical: "top",
                    horizontal: "right",
                  }}
                >
                  <MenuItem onClick={handleCloseProfileMenu} component={Link} to={`/profile/${userId}`} className={styles.menuItem}>
                    <PersonIcon className={styles.menuIcon} />
                    Profile
                  </MenuItem>
                  <MenuItem onClick={handleLogout} className={styles.menuItem}>
                    <LogoutIcon className={styles.menuIcon} />
                    Log Out
                  </MenuItem>
                </Menu>
              </div>
            )}
          </div>

          <button className={styles.mobileMenuButton} onClick={toggleMobileMenu}>
            <div className={`${styles.menuBar} ${isMobileMenuOpen ? styles.open : ""}`}></div>
            <div className={`${styles.menuBar} ${isMobileMenuOpen ? styles.open : ""}`}></div>
            <div className={`${styles.menuBar} ${isMobileMenuOpen ? styles.open : ""}`}></div>
          </button>
        </div>
      </div>

      <div className={`${styles.mobileMenu} ${isMobileMenuOpen ? styles.active : ""}`}>
        {accessToken && userRole === "ADMIN" ? (
          <Link to="/users" className={styles.mobileNavLink} onClick={closeMobileMenu}>
            Users
          </Link>
        ) : (
          <Link to="/create-charity" className={styles.mobileNavLink} onClick={closeMobileMenu}>
            Start Fundraising
          </Link>
        )}

        {accessToken ? (
          <div className={styles.mobileProfileContainer}>
            <div className={styles.mobileProfileHeader} onClick={handleOpenMobileProfileMenu} ref={mobileProfileRef}>
              <Avatar className={styles.mobileProfileAvatar} src={profilePicture || "/placeholder.svg?height=40&width=40"} />
              <span className={styles.mobileProfileText}>My Profile</span>
            </div>
            <Menu
              id="mobile-profile-menu"
              anchorEl={mobileProfileRef.current}
              open={mobileProfileMenuOpen}
              onClose={handleCloseMobileProfileMenu}
              className={styles.profileMenu}
            >
              <MenuItem
                onClick={() => {
                  handleCloseMobileProfileMenu()
                  closeMobileMenu()
                }}
                component={Link}
                to={`/profile/${userId}`}
                className={styles.menuItem}
              >
                <PersonIcon className={styles.menuIcon} />
                Profile
              </MenuItem>
              <MenuItem
                onClick={() => {
                  handleLogout()
                  handleCloseMobileProfileMenu()
                }}
                className={styles.menuItem}
              >
                <LogoutIcon className={styles.menuIcon} />
                Log Out
              </MenuItem>
            </Menu>
          </div>
        ) : (
          <Link to="/login" className={styles.mobileNavLink} onClick={closeMobileMenu}>
            Log In
          </Link>
        )}
      </div>

      <div className={`${styles.mobileMenuOverlay} ${isMobileMenuOpen ? styles.active : ""}`} onClick={closeMobileMenu}></div>
    </nav>
  )
}

export default NavBar
