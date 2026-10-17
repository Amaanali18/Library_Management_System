import api from "../services/axiosInstance";

export const getBooks = async (params = {}) => {
  const response = await api.get("/api/books", { params });
  return response.data;
};

export const getBookById = async (id) => {
  const response = await api.get(`/api/books/${id}`);
  return response.data;
};

export const getBookPreview = async (id) => {
  const response = await api.get(`/api/books/${id}/preview`);
  return response.data;
};

export const addBook = async (bookData) => {
  const response = await api.post("/api/books", bookData);
  return response.data;
};

export const updateBook = async (id, bookData) => {
  const response = await api.put(`/api/books/${id}`, bookData);
  return response.data;
};
