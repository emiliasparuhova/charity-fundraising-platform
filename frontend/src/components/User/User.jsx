import { useState } from "react"
import { useNavigate } from "react-router-dom";
import styles from "./User.module.css"

const User = ({ user }) => {
    const [imageError, setImageError] = useState(false)
    const navigate = useNavigate();

    const formatDate = (dateString) => {
        if (!dateString) return "N/A"
        const options = { year: "numeric", month: "short", day: "numeric" }
        return new Date(dateString).toLocaleDateString(undefined, options)
    }

    const getRoleColor = (role) => {
        switch (role?.toLowerCase()) {
            case "admin":
                return "#ef4444"
            case "user":
                return "#10b981"
            default:
                return "#6b7280"
        }
    }

    const getInitials = (name) => {
        if (!name) return "U"
        return name
            .split(" ")
            .map((word) => word.charAt(0))
            .join("")
            .toUpperCase()
            .substring(0, 2)
    }

    return (
        <div className={`${styles.userCard} ${user.isDeleted ? styles.deletedUser : ""}`}>
            <div className={styles.userHeader}>
                <div className={styles.profilePicture}>
                    {user.profilePicture && !imageError ? (
                        <img
                            src={user.profilePicture ? `data:image/jpeg;base64,${user.profilePicture}` : "/placeholder.svg"}
                            alt={`${user.name}'s profile`}
                            className={styles.profileImage}
                            onError={() => setImageError(true)}
                        />
                    ) : (
                        <div className={styles.profileInitials}>{getInitials(user.name)}</div>
                    )}
                    {user.isDeleted && <div className={styles.deletedBadge}>Deleted</div>}
                </div>
                <div className={styles.userInfo}>
                    <h3 className={styles.userName}>{user.name || "Unknown User"}</h3>
                    <p className={styles.userEmail}>{user.email}</p>
                    <div className={styles.userRole} style={{ backgroundColor: getRoleColor(user.role) }}>
                        {user.role || "User"}
                    </div>
                </div>
            </div>

            <div className={styles.userDetails}>
                <div className={styles.detailItem}>
                    <span className={styles.detailLabel}>Join Date:</span>
                    <span className={styles.detailValue}>{formatDate(user.joinDate)}</span>
                </div>

                <div className={styles.detailItem}>
                    <span className={styles.detailLabel}>Status:</span>
                    <span className={`${styles.detailValue} ${user.isDeleted ? styles.deletedStatus : styles.activeStatus}`}>
                        {user.isDeleted ? "Deleted" : "Active"}
                    </span>
                </div>

                {user.isDeleted && user.deletionDate && (
                    <div className={styles.detailItem}>
                        <span className={styles.detailLabel}>Deletion Date:</span>
                        <span className={styles.detailValue}>{formatDate(user.deletionDate)}</span>
                    </div>
                )}

                {user.lastLogin && (
                    <div className={styles.detailItem}>
                        <span className={styles.detailLabel}>Last Login:</span>
                        <span className={styles.detailValue}>{formatDate(user.lastLogin)}</span>
                    </div>
                )}
            </div>

            <div className={styles.userActions}>
                <button
                    className={styles.viewButton}
                    onClick={() => navigate(`/profile/${user.id}`)}
                >
                    View Profile
                </button>
            </div>
        </div>
    )
}

export default User
