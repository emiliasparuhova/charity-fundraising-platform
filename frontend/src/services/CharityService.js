import axios from "../axiosInstance.js";

const BASE_URL = '/charities';

const getAll = data => {
    return axios.get(BASE_URL, data);
};

const getById = id => {
    return axios.get(`${BASE_URL}/${id}`);
};

const create = data => {
    return axios.post(BASE_URL, data);
};

const CharityService = {
    getAll,
    getById,
    create
};

export default CharityService;
