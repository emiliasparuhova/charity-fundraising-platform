"use client"

import { useEffect, useState } from "react"
import UserService from "../../services/UserService"
import styles from "./UsersPage.module.css"
import User from "../../components/User/User"

const UsersPage = () => {
  const [users, setUsers] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    UserService.getAll()
      .then((response) => {
        setUsers(response.data.users)
      })
      .catch((error) => {
        console.error("Failed to fetch users:", error)
        setError("Failed to fetch users")
      })
      .finally(() => {
        setLoading(false)
      })
  }, [])

  if (loading) {
    return (
      <div className={styles.loadingContainer}>
        <div className={styles.loadingSpinner}></div>
        <p>Loading users...</p>
      </div>
    )
  }

  if (error) {
    return (
      <div className={styles.errorContainer}>
        <div className={styles.errorIcon}>⚠️</div>
        <h2>Something went wrong</h2>
        <p>{error}</p>
      </div>
    )
  }

  return (
    <div className={styles.usersPage}>
      <div className={styles.pageHeader}>
        <h1 className={styles.pageTitle}>Users</h1>
        <p className={styles.pageDescription}>Manage and view all registered users</p>
        <div className={styles.userStats}>
          <div className={styles.statItem}>
            <span className={styles.statNumber}>{users.length}</span>
            <span className={styles.statLabel}>Total Users</span>
          </div>
          <div className={styles.statItem}>
            <span className={styles.statNumber}>{users.filter((user) => !user.isDeleted).length}</span>
            <span className={styles.statLabel}>Active Users</span>
          </div>
          <div className={styles.statItem}>
            <span className={styles.statNumber}>{users.filter((user) => user.isDeleted).length}</span>
            <span className={styles.statLabel}>Deleted Accounts</span>
          </div>
        </div>
      </div>

      {users.length === 0 ? (
        <div className={styles.noUsers}>
          <div className={styles.noUsersIcon}>👥</div>
          <h3>No users found</h3>
          <p>There are currently no registered users in the system.</p>
        </div>
      ) : (
        <div className={styles.usersGrid}>
          {users.map((user) => (
            <User key={user.id} user={user} />
          ))}
        </div>
      )}
    </div>
  )
}

export default UsersPage
