import axios from 'axios';
import storeObject from './store';

const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
});

axiosInstance.interceptors.request.use(
  (config) => {
    const { auth } = storeObject.store.getState();
    const token = auth.token;
    
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    
    config.headers['Content-Type'] = 'application/json';
    
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default axiosInstance;
