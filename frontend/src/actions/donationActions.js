import { SET_DONATION_DRAFT, CLEAR_DONATION_DRAFT } from "./actionTypes.js";

export const setDonationDraft = (charityId, amount) => ({
  type: SET_DONATION_DRAFT,
  payload: { charityId, amount },
});

export const clearDonationDraft = () => ({
  type: CLEAR_DONATION_DRAFT,
});
