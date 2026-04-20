import axios from "axios";

const API_BASE_URL = "http://localhost:8081/products";

export const fetchProductsPaged = async (
  page = 0,
  size = 5,
  sortBy = "id",
  sortDir = "asc"
) => {
  const response = await axios.get(`${API_BASE_URL}/paged`, {
    params: { page, size, sortBy, sortDir },
  });
  return response.data;
};

export const createProduct = async (product) => {
  const response = await axios.post(API_BASE_URL, product);
  return response.data;
};