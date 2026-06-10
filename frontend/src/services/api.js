import axios from "axios";

const API_BASE_URL = "http://localhost:8080/api/v1";

export const executeCode = async (
    language,
    code,
    input
) => {

    const response = await axios.post(
        `${API_BASE_URL}/execute`,
        {
            language,
            code,
            input
        }
    );

    return response.data;
};