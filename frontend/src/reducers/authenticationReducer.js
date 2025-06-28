import { CREATE_AUTH_SUCCESS, LOGOUT } from "../actions/actionTypes.js";

const initialState = {
  token: null,
};

const authReducer = (state = initialState, action) => {
  switch (action.type) {
    case CREATE_AUTH_SUCCESS:
      const { token } = action.payload;
      return {
        ...state,
        token: token,
      };
      case LOGOUT:
        return {
          ...state,
          token: null,
      };
    default:
      return state;
  }
};

export default authReducer;
