import { combineReducers } from 'redux';
import donationReducer from './donationReducer';
import authReducer from './authenticationReducer';

const rootReducer = combineReducers({
  donation: donationReducer,
  auth: authReducer
});

export default rootReducer;
