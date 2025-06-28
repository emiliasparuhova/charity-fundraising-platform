import { useState } from "react"
import { useEffect } from "react";
import { Link } from "react-router-dom"
import styles from "./Charity.module.css"
import DonationService from "../../services/DonationService";

const Charity = ({ charity }) => {
  const [imageError, setImageError] = useState(false)
  const [donationStats, setDonationStats] = useState({ donationCount: 0, totalFundsRaised: 0 });
  const [progressPercentage, setProgressPercentage] = useState(0);

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat("en-US", {
      style: "currency",
      currency: "EUR",
      minimumFractionDigits: 0,
      maximumFractionDigits: 0,
    }).format(amount)
  }

  const truncateDescription = (text, maxLength = 100) => {
    if (!text) return ""
    if (text.length <= maxLength) return text
    return text.substring(0, maxLength) + "..."
  }

  useEffect(() => {
    if (charity && charity.id) {
      const fetchDonationStats = async () => {
        try {
          const stats = await DonationService.getCharityStats(charity.id);
          setDonationStats(stats.data);

          const newProgressPercentage = stats.data.totalFundsRaised && charity.fundraisingGoal
            ? Math.min(Math.round((stats.data.totalFundsRaised / charity.fundraisingGoal) * 100), 100)
            : 0;

          setProgressPercentage(newProgressPercentage);
        } catch (error) {
          console.error("Error fetching donation stats:", error);
        }
      };

      fetchDonationStats();
    }
  }, [charity]);

  return (
    <div className={styles.charityCard}>
      <div className={styles.imageContainer}>
        {charity.photos && charity.photos.length > 0 && !imageError ? (
          <img
            src={`data:image/jpeg;base64,${charity.photos[0]}`}
            alt={charity.title}
            className={styles.charityImage}
            onError={() => setImageError(true)}
          />
        ) : (
          <div className={styles.placeholderImage}>
            <span>No Images Available</span>
          </div>
        )}
        <div className={styles.categoryBadge}>{charity.category}</div>
      </div>

      <div className={styles.cardContent}>
        <h3 className={styles.charityTitle}>{charity.title}</h3>
        <p className={styles.charityDescription}>{truncateDescription(charity.description)}</p>

        <div className={styles.progressContainer}>
          <div className={styles.progressBar}>
            <div className={styles.progressFill} style={{ width: `${progressPercentage}%` }}></div>
          </div>
          <div className={styles.progressStats}>
            <span className={styles.progressPercentage}>{progressPercentage}%</span>
            <span className={styles.progressAmount}>
              {formatCurrency(donationStats.totalFundsRaised || 0)} raised of {formatCurrency(charity.fundraisingGoal)}
            </span>
          </div>
        </div>

        <div className={styles.cardFooter}>
          <Link to={`/charity/${charity.id}`} className={styles.viewButton}>
            View Details
          </Link>
        </div>
      </div>
    </div>
  )
}

export default Charity

