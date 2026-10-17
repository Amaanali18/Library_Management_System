import api from "./axiosInstance";

export const borrowBook = async (borrowRequest) => {
  const response = await api.post("/api/borrow", borrowRequest);
  return response.data;
};

export const returnBook = async (borrowId) => {
  const response = await api.post(`/api/borrow/${borrowId}/return`);
  return response.data;
};

export const getMyBorrowings = async () => {
  const response = await api.get("/api/borrow/my-borrowings");
  return response.data;
};

export const getAllBorrowings = async (params = {}) => {
  const response = await api.get("/api/borrow", { params });
  return response.data;
};
