import { useEffect, useState } from "react"
import { useLocation, Link } from "react-router-dom"
import { useSelector, useDispatch } from 'react-redux';
import PaymentService from "../../services/PaymentService"
import styles from "./DonationSuccessPage.module.css"
import DonationService from "../../services/DonationService";
import CharityService from "../../services/CharityService";
import { jwtDecode } from 'jwt-decode';

const DonationSuccessPage = () => {
    const accessToken = useSelector((state) => state.auth.token)

    const location = useLocation()
    const dispatch = useDispatch();

    const [paymentStatus, setPaymentStatus] = useState(null)
    const [charity, setCharity] = useState(null);
    const [errorMessage, setErrorMessage] = useState(null)
    const [isLoading, setIsLoading] = useState(true)

    const { charityId, amount } = useSelector((state) => state.donation);

    const getQueryParams = () => {
        const urlParams = new URLSearchParams(location.search)
        const token = urlParams.get("token")
        const payerId = urlParams.get("PayerID")
        return { token, payerId }
    }

    const handlePaymentSuccess = async () => {
        const { token, payerId } = getQueryParams();

        if (!token || !payerId) {
            setErrorMessage("Missing token or PayerID in the URL.");
            setIsLoading(false);
            return;
        }

        try {
            const data = { token, payerId };
            let response = await PaymentService.completePayment(data);

            if (response.data.paymentStatus === "COMPLETED") {
                setPaymentStatus("COMPLETED")
                if (!charityId || !amount) {
                    setErrorMessage("Donation details are missing.");
                    setIsLoading(false);
                    return;
                }

                const charityDetails = await CharityService.getById(charityId);
                setCharity(charityDetails.data);

                const decodedToken = jwtDecode(accessToken);
                const userId = decodedToken.userId

                const donationData = {
                    amount: amount,
                    donorId: userId,
                    charityId: charityId
                };

                await DonationService.create(donationData);
            } else {
                setErrorMessage("Payment failed.");
            }
        } catch (error) {
            setErrorMessage("There was an error completing the payment. Please try again.");
            console.error("Payment completion error:", error);
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        handlePaymentSuccess()
    }, [])

    if (isLoading) {
        return (
            <div className={styles.loadingContainer}>
                <div className={styles.loadingContent}>
                    <div className={styles.loadingSpinner}></div>
                    <h2 className={styles.loadingTitle}>Processing Your Donation</h2>
                    <p className={styles.loadingText}>Please wait while we confirm your payment...</p>
                </div>
            </div>
        )
    }

    if (errorMessage) {
        return (
            <div className={styles.errorContainer}>
                <div className={styles.errorContent}>
                    <div className={styles.errorIcon}>⚠️</div>
                    <h2 className={styles.errorTitle}>Something Went Wrong</h2>
                    <p className={styles.errorMessage}>{errorMessage}</p>
                    <div className={styles.actionButtons}>
                        <Link to="/" className={styles.primaryButton}>
                            Browse Charities
                        </Link>
                    </div>
                </div>
            </div>
        )
    }

    if (paymentStatus === "COMPLETED") {
        return (
            <div className={styles.successContainer}>
                <div className={styles.successContent}>
                    <div className={styles.successIcon}>
                        <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" className={styles.checkIcon}>
                            <circle cx="12" cy="12" r="10" fill="currentColor" fillOpacity="0.2" />
                            <path
                                d="M8 12L11 15L16 9"
                                stroke="currentColor"
                                strokeWidth="2"
                                strokeLinecap="round"
                                strokeLinejoin="round"
                            />
                        </svg>
                    </div>
                    <h1 className={styles.successTitle}>Thank You for Your Donation!</h1>
                    <p className={styles.successMessage}>
                        Your generous contribution has been successfully processed and will make a real difference.
                    </p>

                    <div className={styles.donationDetails}>
                        <div className={styles.detailItem}>
                            <span className={styles.detailLabel}>Donation Amount:</span>
                            <span className={styles.detailValue}>€{amount || "0.00"}</span>
                        </div>
                        <div className={styles.detailItem}>
                            <span className={styles.detailLabel}>Charity:</span>
                            <span className={styles.detailValue}>{charity.title}</span>
                        </div>
                        <div className={styles.detailItem}>
                            <span className={styles.detailLabel}>Date:</span>
                            <span className={styles.detailValue}>{new Date().toLocaleDateString()}</span>
                        </div>
                    </div>

                    <div className={styles.actionButtons}>
                        <Link to="/" className={styles.primaryButton}>
                            Browse More Charities
                        </Link>
                    </div>
                </div>
            </div>
        )
    }

    return (
        <div className={styles.failedContainer}>
            <div className={styles.failedContent}>
                <div className={styles.failedIcon}>❌</div>
                <h2 className={styles.failedTitle}>Payment Not Completed</h2>
                <p className={styles.failedMessage}>
                    We couldn't complete your donation. Please try again or use a different payment method.
                </p>
                <div className={styles.actionButtons}>
                    <Link to="/" className={styles.secondaryButton}>
                        Return Home
                    </Link>
                </div>
            </div>
        </div>
    )
}

export default DonationSuccessPage