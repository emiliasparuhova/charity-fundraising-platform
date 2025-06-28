export const CREATE_AUTH_SUCCESS = 'CREATE_AUTH_SUCCESS';
export const LOGOUT = 'LOGOUT';

export const createAuthSuccess = (authData) => ({
  type: CREATE_AUTH_SUCCESS,
  payload: authData,
});

export const logout = () => ({
  type: LOGOUT,
});