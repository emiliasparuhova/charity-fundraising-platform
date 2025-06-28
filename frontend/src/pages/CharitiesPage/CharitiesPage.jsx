import { useState, useEffect } from "react";
import Charity from "../../components/Charity/Charity";
import CharityService from "../../services/CharityService";
import styles from "./CharitiesPage.module.css";

const CharitiesPage = () => {
  const [charities, setCharities] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchCharities = async () => {
      try {
        const response = await CharityService.getAll();
        if (Array.isArray(response.data.charities)) {
          setCharities(response.data.charities);
        } else {
          setError("The data received is not an array.");
        }
      } catch (err) {
        setError("Failed to fetch charities.");
        console.error("Error fetching charities:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchCharities();
  }, []);

  if (loading) {
    return <div className={styles.loadingContainer}>Loading charities...</div>;
  }

  if (error) {
    return <div className={styles.errorContainer}>{error}</div>;
  }

  return (
    <div className={styles.charitiesPage}>
      {charities.length === 0 ? (
        <p className={styles.noCharities}>No charities found. Be the first to create one!</p>
      ) : (
        <div className={styles.charitiesGrid}>
          {[...charities].reverse().map((charity) => (
            <Charity key={charity.id} charity={charity} />
          ))}

        </div>
      )}
    </div>
  );
};

export default CharitiesPage;
