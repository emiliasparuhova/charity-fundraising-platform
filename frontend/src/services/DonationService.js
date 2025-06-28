import axios from "../axiosInstance.js";

const BASE_URL = '/donations';

const create = data => {
    return axios.post(BASE_URL, data);
};

const getCharityStats = charityId => {
    return axios.get(`${BASE_URL}/charity/${charityId}`);
}

const DonationService = {
    create,
    getCharityStats
};

export default DonationService;