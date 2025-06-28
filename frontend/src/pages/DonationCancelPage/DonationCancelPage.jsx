import { Link } from "react-router-dom";
import styles from "./DonationCancelPage.module.css";

const DonationCancelPage = () => {
    return (
        <div className={styles.cancelContainer}>
            <div className={styles.cancelContent}>
                <div className={styles.cancelIcon}>
                    <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" className={styles.iconSvg}>
                        <circle cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="2" />
                        <path d="M15 9L9 15" stroke="currentColor" strokeWidth="2" strokeLinecap="round" />
                        <path d="M9 9L15 15" stroke="currentColor" strokeWidth="2" strokeLinecap="round" />
                    </svg>
                </div>

                <h1 className={styles.cancelTitle}>Donation Cancelled</h1>

                <p className={styles.cancelMessage}>Your donation process was cancelled. No payment has been processed.</p>

                <div className={styles.cancelInfo}>
                    <p>
                        We understand that circumstances change, and that's okay. Your support is always appreciated, whenever
                        you're ready.
                    </p>

                    <div className={styles.actionButtons}>
                        <Link to="/" className={styles.primaryButton}>
                            Browse Charities
                        </Link>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default DonationCancelPage;
