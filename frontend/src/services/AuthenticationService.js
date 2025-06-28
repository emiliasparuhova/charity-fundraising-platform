import axios from "../axiosInstance.js";

const create = data => {
    return axios.post(`/users/auth`, data);
};

const AuthenticationService = {
    create
}

export default AuthenticationService;