"use client"

import { useEffect, useState } from "react"
import { useParams, useNavigate } from "react-router-dom"
import { useDispatch, useSelector } from "react-redux"
import { logout } from "../../actions/authenticationActions"
import { jwtDecode } from "jwt-decode"
import UserService from "../../services/UserService"
import styles from "./ProfilePage.module.css"

const ProfilePage = () => {
  const { id } = useParams()
  const accessToken = useSelector((state) => state.auth.token)
  const [currentUserId, setCurrentUserId] = useState(null)
  const [user, setUser] = useState(null)
  const [isEditing, setIsEditing] = useState(false)
  const [isDeleteConfirmOpen, setIsDeleteConfirmOpen] = useState(false)
  const [loading, setLoading] = useState(true)
  const [selectedFile, setSelectedFile] = useState(null)
  const [previewImage, setPreviewImage] = useState("")
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    profilePicture: "",
  })

  const navigate = useNavigate()
  const dispatch = useDispatch()

  useEffect(() => {
    if (accessToken) {
      const decodedToken = jwtDecode(accessToken)
      setCurrentUserId(decodedToken.userId)
    }
  }, [accessToken])

  const fetchUser = async () => {
    try {
      setLoading(true)
      const response = await UserService.get(id)
      setUser(response.data)
      setFormData({
        name: response.data.name || "",
        email: response.data.email || "",
        profilePicture: response.data.profilePicture || "",
      })
      if (response.data.profilePicture?.startsWith("data:image")) {
        setPreviewImage(response.data.profilePicture)
      } else {
        setPreviewImage("")
      }
    } catch (error) {
      console.error("Error fetching user data:", error)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchUser()
  }, [id])

  const handleInputChange = (e) => {
    const { name, value } = e.target
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }))
  }

  const handleFileChange = (e) => {
    const file = e.target.files[0]
    if (file) {
      const reader = new FileReader()
      reader.onloadend = () => {
        const base64String = reader.result.split(",")[1]
        setSelectedFile(base64String)
        setPreviewImage(reader.result)
      }
      reader.readAsDataURL(file)
    }
  }

  const handleRemovePhoto = () => {
    setSelectedFile(null)
    setPreviewImage("")
    setFormData((prev) => ({
      ...prev,
      profilePicture: null,
    }))
  }

  const handleSave = async () => {
    try {
      const updatedData = {
        ...formData,
        profilePicture: selectedFile || formData.profilePicture,
      }

      await UserService.update(id, updatedData)

      setIsEditing(false)
      setSelectedFile(null)
      setPreviewImage("")

      fetchUser()
      window.location.reload()
    } catch (error) {
      console.error("Error updating user:", error)
    }
  }

  const handleDeleteAccount = async () => {
    try {
      await UserService.remove(id)
      dispatch(logout())
      window.location.reload()
      navigate("/login")
    } catch (error) {
      console.error("Error deleting user:", error)
    }
  }

  const formatDate = (dateString) => {
    const options = { year: "numeric", month: "long", day: "numeric" }
    return new Date(dateString).toLocaleDateString(undefined, options)
  }

  const isOwnProfile = String(currentUserId) === String(id)

  if (loading) {
    return (
      <div className={styles.loadingContainer}>
        <div className={styles.loadingSpinner}></div>
        <p className={styles.loadingText}>Loading profile...</p>
      </div>
    )
  }

  if (!user) {
    return (
      <div className={styles.errorContainer}>
        <p className={styles.errorText}>User not found</p>
        <p className={styles.errorSubtext}>
          This user doesn't exist or may have been deleted.
        </p>
      </div>
    );
  }


  return (
    <div className={styles.profileContainer}>
      <div className={styles.profileCard}>
        <div className={styles.profileHeader}>
          <div className={styles.bannerBackground}></div>

          <div className={styles.profileAvatarContainer}>
            <div className={styles.profileAvatar}>
              {user.profilePicture ? (
                <img
                  src={`data:image/jpeg;base64,${user.profilePicture}`}
                  alt={user.name}
                  className={styles.avatarImage}
                />
              ) : (
                <div className={styles.avatarFallback}>{user.name?.charAt(0)}</div>
              )}
            </div>
          </div>

          {isOwnProfile && accessToken && (
            <div className={styles.editButtonContainer}>
              <button onClick={() => setIsEditing(true)} className={styles.editButton}>
                <svg
                  className={styles.editIcon}
                  xmlns="http://www.w3.org/2000/svg"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  strokeWidth="2"
                  strokeLinecap="round"
                  strokeLinejoin="round"
                >
                  <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
                  <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
                </svg>
                Edit Profile
              </button>
            </div>
          )}
        </div>

        <div className={styles.profileBody}>
          <div className={styles.profileInfo}>
            <h1 className={styles.profileName}>{user.name}</h1>
            <div className={styles.profileMeta}>
              <div className={styles.metaItem}>
                <svg
                  className={styles.metaIcon}
                  xmlns="http://www.w3.org/2000/svg"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  strokeWidth="2"
                  strokeLinecap="round"
                  strokeLinejoin="round"
                >
                  <rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect>
                  <line x1="16" y1="2" x2="16" y2="6"></line>
                  <line x1="8" y1="2" x2="8" y2="6"></line>
                  <line x1="3" y1="10" x2="21" y2="10"></line>
                </svg>
                <span className={styles.metaText}>Joined {formatDate(user.joinDate || new Date())}</span>
              </div>
              <div className={styles.metaItem}>
                <svg
                  className={styles.metaIcon}
                  xmlns="http://www.w3.org/2000/svg"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  strokeWidth="2"
                  strokeLinecap="round"
                  strokeLinejoin="round"
                >
                  <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"></path>
                  <polyline points="22,6 12,13 2,6"></polyline>
                </svg>
                <span className={styles.metaText}>{user.email}</span>
              </div>
            </div>
            {isOwnProfile && accessToken && (
              <div className={styles.deleteAccountContainer}>
                <button onClick={() => setIsDeleteConfirmOpen(true)} className={styles.deleteAccountButton}>
                  <svg
                    className={styles.deleteIcon}
                    xmlns="http://www.w3.org/2000/svg"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    strokeWidth="2"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                  >
                    <polyline points="3 6 5 6 21 6"></polyline>
                    <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
                    <line x1="10" y1="11" x2="10" y2="17"></line>
                    <line x1="14" y1="11" x2="14" y2="17"></line>
                  </svg>
                  Delete Account
                </button>
              </div>
            )}
          </div>

          <hr className={styles.divider} />
        </div>
      </div>

      {isEditing && (
        <div className={styles.modalOverlay}>
          <div className={styles.modalContent}>
            <div className={styles.modalHeader}>
              <h2 className={styles.modalTitle}>Edit Profile</h2>
              <button className={styles.modalClose} onClick={() => setIsEditing(false)}>
                ×
              </button>
            </div>

            <div className={styles.editForm}>
              <div className={styles.profileImageUpload}>
                <div className={styles.editProfileAvatar}>
                  {previewImage || formData.profilePicture ? (
                    <img
                      src={
                        previewImage ||
                        (formData.profilePicture?.startsWith("data:image")
                          ? formData.profilePicture
                          : `data:image/jpeg;base64,${formData.profilePicture}`)
                      }
                      alt="Profile"
                      className={styles.avatarImage}
                    />
                  ) : (
                    <div className={styles.avatarFallback}>{formData.name?.charAt(0)}</div>
                  )}
                </div>

                <div className={styles.avatarButtons}>
                  <div className={styles.uploadButtonContainer}>
                    <input
                      accept="image/*"
                      id="profile-image-upload"
                      type="file"
                      className={styles.fileInput}
                      onChange={handleFileChange}
                    />
                    <label htmlFor="profile-image-upload" className={styles.uploadButton}>
                      <svg
                        className={styles.buttonIcon}
                        xmlns="http://www.w3.org/2000/svg"
                        viewBox="0 0 24 24"
                        fill="none"
                        stroke="currentColor"
                        strokeWidth="2"
                        strokeLinecap="round"
                        strokeLinejoin="round"
                      >
                        <path d="M23 19a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h4l2-3h6l2 3h4a2 2 0 0 1 2 2z"></path>
                        <circle cx="12" cy="13" r="4"></circle>
                      </svg>
                      Change Photo
                    </label>
                  </div>

                  <button className={styles.removeButton} onClick={handleRemovePhoto}>
                    <svg
                      className={styles.buttonIcon}
                      xmlns="http://www.w3.org/2000/svg"
                      viewBox="0 0 24 24"
                      fill="none"
                      stroke="currentColor"
                      strokeWidth="2"
                      strokeLinecap="round"
                      strokeLinejoin="round"
                    >
                      <polyline points="3 6 5 6 21 6"></polyline>
                      <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
                      <line x1="10" y1="11" x2="10" y2="17"></line>
                      <line x1="14" y1="11" x2="14" y2="17"></line>
                    </svg>
                    Remove Photo
                  </button>
                </div>
              </div>

              <div className={styles.formFields}>
                <div className={styles.formField}>
                  <label htmlFor="name" className={styles.fieldLabel}>
                    Name
                  </label>
                  <input
                    id="name"
                    name="name"
                    value={formData.name}
                    onChange={handleInputChange}
                    className={styles.fieldInput}
                  />
                </div>

                <div className={styles.formField}>
                  <label htmlFor="email" className={styles.fieldLabel}>
                    Email
                  </label>
                  <input
                    id="email"
                    name="email"
                    type="email"
                    value={formData.email}
                    onChange={handleInputChange}
                    className={styles.fieldInput}
                  />
                </div>
              </div>

              <div className={styles.modalFooter}>
                <button className={styles.cancelButton} onClick={() => setIsEditing(false)}>
                  Cancel
                </button>
                <button className={styles.saveButton} onClick={handleSave}>
                  Save Changes
                </button>
              </div>
            </div>
          </div>
        </div>
      )}

      {isDeleteConfirmOpen && (
        <div className={styles.modalOverlay}>
          <div className={styles.modalContent}>
            <div className={styles.modalHeader}>
              <h2 className={styles.modalTitle}>
                <svg
                  className={styles.warningIcon}
                  xmlns="http://www.w3.org/2000/svg"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  strokeWidth="2"
                  strokeLinecap="round"
                  strokeLinejoin="round"
                >
                  <path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"></path>
                  <line x1="12" y1="9" x2="12" y2="13"></line>
                  <line x1="12" y1="17" x2="12.01" y2="17"></line>
                </svg>
                Delete Account
              </h2>
              <button className={styles.modalClose} onClick={() => setIsDeleteConfirmOpen(false)}>
                ×
              </button>
            </div>

            <div className={styles.deleteConfirmContent}>
              <p className={styles.deleteWarning}>Are you sure you want to delete your account?</p>
              <p className={styles.deleteWarningDetails}>
                This action cannot be undone. You won't be able to recover your account!.
              </p>

              <div className={styles.modalFooter}>
                <button className={styles.cancelButton} onClick={() => setIsDeleteConfirmOpen(false)}>
                  Cancel
                </button>
                <button className={styles.deleteButton} onClick={handleDeleteAccount}>
                  Delete Account
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}

export default ProfilePage
