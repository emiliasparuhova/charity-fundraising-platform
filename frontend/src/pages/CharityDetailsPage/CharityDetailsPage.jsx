import { useState, useEffect } from "react";
import { useParams, useNavigate, Link } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { setDonationDraft } from "../../actions/donationActions"
import CharityService from "../../services/CharityService";
import styles from "./CharityDetailsPage.module.css";
import PaymentService from "../../services/PaymentService";
import DonationService from "../../services/DonationService";
import UserService from "../../services/UserService";

const CharityDetailsPage = () => {
  const accessToken = useSelector((state) => state.auth.token)
  const dispatch = useDispatch();
  const navigate = useNavigate()

  const { id } = useParams();
  const [charity, setCharity] = useState(null);
  const [donationStats, setDonationStats] = useState({ donationCount: 0, totalFundsRaised: 0 });
  const [creatorName, setCreatorName] = useState("Anonymous");
  const [creatorId, setCreatorId] = useState(null);

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [activePhotoIndex, setActivePhotoIndex] = useState(0);

  const [donationAmount, setDonationAmount] = useState(25);
  const [isSubmitting, setIsSubmitting] = useState(false);

  useEffect(() => {
    const fetchCharityDetails = async () => {
      try {
        const response = await CharityService.getById(id);
        if (response.data) {
          const charityData = response.data;
          setCharity(charityData);

          try {
            const userResponse = await UserService.get(charityData.creatorId);
            if (userResponse?.data?.name) {
              setCreatorName(userResponse.data.name);
              setCreatorId(userResponse.data.id);
            }
          } catch (userErr) {
            console.warn("User fetch failed, defaulting to Anonymous:", userErr);
          }

          const statsResponse = await DonationService.getCharityStats(charityData.id);
          setDonationStats(statsResponse.data);
        } else {
          setError("Charity not found.");
        }
      } catch (err) {
        setError("Failed to fetch charity details.");
        console.error("Error fetching charity details:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchCharityDetails();
  }, [id]);

  const convertPhoto = (photo) => {
    if (photo) {
      return `data:image/jpeg;base64,${photo}`;
    }
    return null;
  };

  const handleDonate = async () => {
    if (!accessToken) {
      navigate("/login");
      return;
    }

    try {
      dispatch(setDonationDraft(charity.id, donationAmount));

      const data = {
        amount: donationAmount,
        paypalEmail: charity.paypalEmail
      }

      const response = await PaymentService.create(data);

      window.location.href = response.data.approvalUrl;
    } catch (error) {
      console.error("Error processing donation:", error);
    }
  };

  const calculateProgress = () => {
    if (!charity) return 0;
    const percentage = Math.round((donationStats.totalFundsRaised / charity.fundraisingGoal) * 100);
    return Math.min(percentage, 100);
  };

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'EUR',
      minimumFractionDigits: 0,
      maximumFractionDigits: 0,
    }).format(amount);
  };

  const formatDate = (dateString) => {
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return new Date(dateString).toLocaleDateString(undefined, options);
  };

  if (loading) {
    return (
      <div className={styles.loadingContainer}>
        <div className={styles.loadingSpinner}></div>
        <p>Loading charity details...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className={styles.errorContainer}>
        <div className={styles.errorIcon}>⚠️</div>
        <h2>Something went wrong</h2>
        <p>{error}</p>
        <Link to="/" className={styles.backButton}>
          Back to Charities
        </Link>
      </div>
    );
  }

  return (
    <div className={styles.charityDetailsPage}>
      <div className={styles.charityHeader}>
        <div className={styles.breadcrumbs}>
          <Link to="/">Charities</Link> / <span>{charity.title}</span>
        </div>
        <div className={styles.categoryBadge}>{charity.category}</div>
        <h1 className={styles.charityTitle}>{charity.title}</h1>
        <div className={styles.charityMeta}>
          <div className={styles.metaItem}>
            <span className={styles.metaIcon}>📅</span>
            <span>Created {formatDate(charity.creationDate || new Date())}</span>
          </div>
          <div className={styles.metaItem}>
            <span className={styles.metaIcon}>👤</span>
            {creatorId ? (
              <Link to={`/profile/${creatorId}`} className={styles.creatorLink}>
                By {creatorName}
              </Link>
            ) : (
              <span>By {creatorName}</span>
            )}
          </div>
        </div>
      </div>

      <div className={styles.charityContent}>
        <div className={styles.mainContent}>
          <div className={styles.photoGallery}>
            <div className={styles.mainPhoto}>
              {charity.photos && charity.photos.length > 0 ? (
                <img
                  src={convertPhoto(charity.photos[activePhotoIndex]) || '/placeholder.svg'}
                  alt={charity.title}
                  className={styles.mainPhotoImg}
                />
              ) : (
                <div className={styles.placeholderImage}>
                  <span>No Images Available</span>
                </div>
              )}
            </div>

            {charity.photos && charity.photos.length > 1 && (
              <div className={styles.thumbnails}>
                {charity.photos.map((photo, index) => (
                  <div
                    key={index}
                    className={`${styles.thumbnail} ${index === activePhotoIndex ? styles.activeThumbnail : ''}`}
                    onClick={() => setActivePhotoIndex(index)}
                  >
                    <img
                      src={convertPhoto(photo) || "/placeholder.svg"}
                      alt={`${charity.title} - photo ${index + 1}`}
                    />
                  </div>
                ))}
              </div>
            )}
          </div>

          <div className={styles.descriptionSection}>
            <h2 className={styles.sectionTitle}>About This Charity</h2>
            <div className={styles.description}>
              {charity.description}
            </div>
          </div>
        </div>

        <div className={styles.sidebar}>
          <div className={styles.progressCard}>
            <div className={styles.progressInfo}>
              <div className={styles.raisedAmount}>
                <span className={styles.amountValue}>{formatCurrency(donationStats.totalFundsRaised || 0)}</span>
                <span className={styles.amountLabel}>raised of {formatCurrency(charity.fundraisingGoal)}</span>
              </div>
              <div className={styles.progressPercentage}>{Math.round(calculateProgress())}%</div>
            </div>

            <div className={styles.progressBarContainer}>
              <div
                className={styles.progressBar}
                style={{ width: `${calculateProgress()}%` }}
              ></div>
            </div>

            <div className={styles.donationsInfo}>
              <div className={styles.donationsCount}>
                <span className={styles.donationsValue}>{donationStats.donationCount || 0}</span>
                <span className={styles.donationsLabel}>donations</span>
              </div>
            </div>
          </div>

          <div className={styles.donationCard}>
            <h3 className={styles.donationTitle}>Make a Donation</h3>

            {!charity.isActive && (
              <p className={styles.inactiveNotice}>
                This charity is no longer active. Donations are currently disabled.
              </p>
            )}
            <form className={styles.donationForm}>
              <div className={styles.amountOptions}>
                <button
                  type="button"
                  className={`${styles.amountOption} ${donationAmount === 10 ? styles.activeAmount : ''}`}
                  onClick={() => setDonationAmount(10)}
                  disabled={!charity.isActive}
                >€10</button>
                <button
                  type="button"
                  className={`${styles.amountOption} ${donationAmount === 25 ? styles.activeAmount : ''}`}
                  onClick={() => setDonationAmount(25)}
                  disabled={!charity.isActive}
                >€25</button>
                <button
                  type="button"
                  className={`${styles.amountOption} ${donationAmount === 50 ? styles.activeAmount : ''}`}
                  onClick={() => setDonationAmount(50)}
                  disabled={!charity.isActive}
                >€50</button>
                <button
                  type="button"
                  className={`${styles.amountOption} ${donationAmount === 100 ? styles.activeAmount : ''}`}
                  onClick={() => setDonationAmount(100)}
                  disabled={!charity.isActive}
                >€100</button>
              </div>

              <div className={styles.customAmount}>
                <label htmlFor="customAmount">Custom Amount</label>
                <div className={styles.customAmountInput}>
                  <span className={styles.currencySymbol}>€</span>
                  <input
                    type="number"
                    id="customAmount"
                    value={donationAmount}
                    onChange={(e) => setDonationAmount(Number(e.target.value))}
                    min="1"
                    step="1"
                    disabled={!charity.isActive}
                  />
                </div>
              </div>

              <button
                type="button"
                className={styles.donateButton}
                disabled={!charity.isActive || isSubmitting || donationAmount <= 0}
                onClick={handleDonate}
              >
                {isSubmitting ? 'Processing...' : `Donate ${formatCurrency(donationAmount)}`}
              </button>
            </form>

            <div className={styles.secureInfo}>
              <span className={styles.secureIcon}>🔒</span>
              <span>Secure donation</span>
            </div>
          </div>

          <div className={styles.contactCard}>
            <h3 className={styles.contactTitle}>Questions?</h3>
            <p className={styles.contactText}>
              Contact the organizer directly if you have any questions about this charity.
            </p>
            <button className={styles.contactButton}>
              Contact Organizer
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CharityDetailsPage;