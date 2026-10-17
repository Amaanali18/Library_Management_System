import api from "./axiosInstance";

export const getMyFines = async () => {
  const response = await api.get("/api/fines/my");
  return response.data;
};

export const getAllFines = async (params = {}) => {
  const response = await api.get("/api/fines", { params });
  return response.data;
};

export const getFineById = async (id) => {
  const response = await api.get(`/api/fines/${id}`);
  return response.data;
};
