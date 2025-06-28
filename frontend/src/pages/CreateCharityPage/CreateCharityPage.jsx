import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './CreateCharityPage.module.css';
import CharityService from '../../services/CharityService';
import { jwtDecode } from 'jwt-decode';
import { useSelector } from 'react-redux';


const CreateCharityPage = () => {
  const accessToken = useSelector((state) => state.auth.token)

  const navigate = useNavigate();
  const [currentStep, setCurrentStep] = useState(1);
  const [formData, setFormData] = useState({
    title: "",
    description: "",
    fundraisingGoal: 0.0,
    category: "",
    paypalEmail: ""
  });
  const [photos, setPhotos] = useState([]);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [dragging, setDragging] = useState(false);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handlePhotoChange = (e) => {
    if (e.target.files) {
      const newPhotos = Array.from(e.target.files);
      setPhotos([...photos, ...newPhotos]);
    }
  };

  const removePhoto = (index) => {
    setPhotos(photos.filter((_, i) => i !== index));
  };

  const arrayBufferToBase64 = (buffer) => {
    return new Promise((resolve, reject) => {
      try {
        const blob = new Blob([buffer], { type: 'application/octet-binary' });
        const reader = new FileReader();

        reader.onloadend = () => {
          const base64String = reader.result.split(",")[1];
          resolve(base64String);
        };

        reader.onerror = (error) => {
          reject(new Error("Error reading ArrayBuffer as Base64: " + error));
        };

        reader.readAsDataURL(blob);
      } catch (error) {
        reject(error);
      }
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsSubmitting(true);

    try {
      const photoPromises = photos.map((photo) => {
        return new Promise((resolve, reject) => {
          const reader = new FileReader();
          reader.readAsArrayBuffer(photo);
          reader.onloadend = () => {
            arrayBufferToBase64(reader.result).then(resolve).catch(reject);
          };
          reader.onerror = reject;
        });
      });

      const base64Strings = await Promise.all(photoPromises);
      const decodedToken = jwtDecode(accessToken);
      const creatorId = decodedToken.userId

      const charityData = {
        ...formData,
        photos: base64Strings,
        creatorId,
      };

      await CharityService.create(charityData);
      navigate("/");
    } catch (error) {
      console.error("Error creating charity:", error);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleDragEnter = (e) => {
    e.preventDefault();
    setDragging(true);
  };

  const handleDragLeave = (e) => {
    e.preventDefault();
    setDragging(false);
  };

  const handleDragOver = (e) => {
    e.preventDefault();
  };

  const handleDrop = (e) => {
    e.preventDefault();
    setDragging(false);

    const files = e.dataTransfer.files;
    if (files.length > 0) {
      const newPhotos = Array.from(files);
      setPhotos([...photos, ...newPhotos]);
    }
  };

  const nextStep = () => {
    setCurrentStep(currentStep + 1);
  };

  const prevStep = () => {
    setCurrentStep(currentStep - 1);
  };

  const renderStepContent = () => {
    switch (currentStep) {
      case 1:
        return (
          <>
            <div className={styles.formGroup}>
              <label htmlFor="title" className={styles.formLabel}>
                Charity Title
              </label>
              <input
                type="text"
                id="title"
                name="title"
                value={formData.title}
                onChange={handleInputChange}
                className={styles.formInput}
                placeholder="Enter charity title"
                required
              />
            </div>

            <div className={styles.formGroup}>
              <label htmlFor="description" className={styles.formLabel}>
                Description
              </label>
              <textarea
                id="description"
                name="description"
                value={formData.description}
                onChange={handleInputChange}
                className={styles.formTextarea}
                placeholder="Describe your charity and its purpose"
                rows="5"
                required
              />
            </div>
            <div className={styles.stepActions}>
              <button
                type="button"
                className={styles.nextButton}
                onClick={nextStep}
                disabled={!formData.title || !formData.description}
              >
                Next Step
              </button>
            </div>
          </>
        );
      case 2:
        return (
          <>
            <div className={styles.formRow}>
              <div className={styles.formGroup}>
                <label htmlFor="fundraisingGoal" className={styles.formLabel}>
                  Fundraising Goal (€)
                </label>
                <input
                  type="number"
                  id="fundraisingGoal"
                  name="fundraisingGoal"
                  value={formData.fundraisingGoal}
                  onChange={handleInputChange}
                  className={styles.formInput}
                  placeholder="0.00"
                  step="1"
                  min="0.1"
                  required
                />
              </div>

              <div className={styles.formGroup}>
                <label htmlFor="category" className={styles.formLabel}>
                  Category
                </label>
                <select
                  id="category"
                  name="category"
                  value={formData.category}
                  onChange={handleInputChange}
                  className={styles.formSelect}
                  required
                >
                  <option value="">Select a category</option>
                  <option value="EDUCATION">Education</option>
                  <option value="HEALTH">Health</option>
                  <option value="ENVIRONMENT">Environment</option>
                  <option value="ANIMALS">Animals</option>
                  <option value="POVERTY">Humanitarian</option>
                  <option value="OTHER">Other</option>
                </select>
              </div>
            </div>
            <div className={styles.stepActions}>
              <button
                type="button"
                className={styles.backButton}
                onClick={prevStep}
              >
                Back
              </button>
              <button
                type="button"
                className={styles.nextButton}
                onClick={nextStep}
                disabled={!formData.fundraisingGoal || !formData.category}
              >
                Next Step
              </button>
            </div>
          </>
        );
      case 3:
        return (
          <>
            <div className={styles.formGroup}>
              <label htmlFor="paypalEmail" className={styles.formLabel}>
                PayPal Email
              </label>
              <input
                type="email"
                id="paypalEmail"
                name="paypalEmail"
                value={formData.paypalEmail}
                onChange={handleInputChange}
                className={styles.formInput}
                placeholder="Enter PayPal email"
                required
              />
            </div>
            <div className={styles.stepActions}>
              <button
                type="button"
                className={styles.backButton}
                onClick={prevStep}
              >
                Back
              </button>
              <button
                type="button"
                className={styles.nextButton}
                onClick={nextStep}
                disabled={!formData.paypalEmail}
              >
                Next Step
              </button>
            </div>
          </>
        );
      case 4:
        return (
          <>
            <div className={styles.formGroup}>
              <label className={styles.formLabel}>Photos</label>
              <div
                className={`${styles.dropZone} ${dragging ? styles.dragging : ''}`}
                onDragEnter={handleDragEnter}
                onDragLeave={handleDragLeave}
                onDragOver={handleDragOver}
                onDrop={handleDrop}
              >
                <div className={styles.dropZoneContent}>
                  <span className={styles.dropZoneIcon}>📷</span>
                  <p>Drag & drop photos here or</p>
                  <label className={styles.fileInputLabel}>
                    Browse Files
                    <input
                      type="file"
                      accept="image/*"
                      multiple
                      onChange={handlePhotoChange}
                      className={styles.fileInput}
                    />
                  </label>
                </div>
              </div>

              {photos.length > 0 && (
                <div className={styles.photoPreviewContainer}>
                  {photos.map((photo, index) => (
                    <div key={index} className={styles.photoPreview}>
                      <img
                        src={URL.createObjectURL(photo) || "/placeholder.svg"}
                        alt={`Preview ${index}`}
                        className={styles.previewImage}
                      />
                      <button
                        type="button"
                        className={styles.removePhotoBtn}
                        onClick={() => removePhoto(index)}
                      >
                        ✕
                      </button>
                    </div>
                  ))}
                </div>
              )}
            </div>
            <div className={styles.stepActions}>
              <button
                type="button"
                className={styles.backButton}
                onClick={prevStep}
              >
                Back
              </button>
              <button
                type="submit"
                className={styles.submitButton}
                disabled={isSubmitting || photos.length === 0}
              >
                {isSubmitting ? 'Creating...' : 'Create Charity'}
              </button>
            </div>
          </>
        );
      default:
        return null;
    }
  };

  return (
    <div className={styles.pageContainer}>
      <div className={styles.leftSection}>
        <div className={styles.leftContent}>
          <div className={styles.animatedCircle}></div>
          <div className={styles.animatedCircle}></div>
          <div className={styles.animatedCircle}></div>
          <h2 className={styles.leftTitle}>Make a Difference</h2>
          <p className={styles.leftDescription}>
            Your fundraising campaign can change lives. Start your journey to making a positive impact today.
          </p>
          <div className={styles.impactStats}>
            <div className={styles.statItem}>
              <span className={styles.statNumber}>10M+</span>
              <span className={styles.statLabel}>People Helped</span>
            </div>
            <div className={styles.statItem}>
              <span className={styles.statNumber}>€50M+</span>
              <span className={styles.statLabel}>Funds Raised</span>
            </div>
            <div className={styles.statItem}>
              <span className={styles.statNumber}>5K+</span>
              <span className={styles.statLabel}>Campaigns</span>
            </div>
          </div>
        </div>
      </div>
      <div className={styles.rightSection}>
        <div className={styles.createCharityContainer}>
          <h1 className={styles.pageTitle}>Start your own fundraising</h1>
          <p className={styles.pageDescription}>
            Fill out the form below to create a new charity campaign
          </p>

          <div className={styles.stepIndicator}>
            <div className={`${styles.step} ${currentStep >= 1 ? styles.active : ''}`}>
              <div className={styles.stepNumber}>1</div>
              <span className={styles.stepLabel}>Basic Info</span>
            </div>
            <div className={styles.stepConnector}></div>
            <div className={`${styles.step} ${currentStep >= 2 ? styles.active : ''}`}>
              <div className={styles.stepNumber}>2</div>
              <span className={styles.stepLabel}>Details</span>
            </div>
            <div className={styles.stepConnector}></div>
            <div className={`${styles.step} ${currentStep >= 3 ? styles.active : ''}`}>
              <div className={styles.stepNumber}>3</div>
              <span className={styles.stepLabel}>PayPal</span>
            </div>
            <div className={styles.stepConnector}></div>
            <div className={`${styles.step} ${currentStep >= 4 ? styles.active : ''}`}>
              <div className={styles.stepNumber}>4</div>
              <span className={styles.stepLabel}>Media</span>
            </div>
          </div>

          <form className={styles.form} onSubmit={handleSubmit}>
            {renderStepContent()}
          </form>
        </div>
      </div>
    </div>
  );
};

export default CreateCharityPage;