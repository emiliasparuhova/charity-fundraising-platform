import { SET_DONATION_DRAFT, CLEAR_DONATION_DRAFT } from "../actions/actionTypes.js";

const initialState = {
  charityId: null,
  amount: 0,
};

const donationReducer = (state = initialState, action) => {
  switch (action.type) {
    case SET_DONATION_DRAFT:
      return {
        ...state,
        charityId: action.payload.charityId,
        amount: action.payload.amount,
      };
    case CLEAR_DONATION_DRAFT:
      return initialState;
    default:
      return state;
  }
};

export default donationReducer;