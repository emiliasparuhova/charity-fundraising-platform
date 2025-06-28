import axios from "../axiosInstance.js";

const BASE_URL = '/users';

const getAll = () => {
  return axios.get(BASE_URL);
};

const get = id => {
  return axios.get(`/users/${id}`);
};

const create = data => {
  return axios.post(BASE_URL, data);
};

const update = (id, data) => {
  return axios.put(`/users/${id}`, data);
};

const remove = id => {
  return axios.delete(`/users/${id}`);
};

const UserService = {
  getAll,
  get,
  create,
  update,
  remove
};

export default UserService;