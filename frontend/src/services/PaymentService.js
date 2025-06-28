import axios from "../axiosInstance.js";

const BASE_URL = '/payments';

const create = (data) => {
    return axios.post(BASE_URL, data);
};

const completePayment = data => {
    return axios.post(`${BASE_URL}/complete`, data)
}

const PaymentService = {
    create,
    completePayment
};

export default PaymentService;