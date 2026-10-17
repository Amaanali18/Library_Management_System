import api from "../services/axiosInstance";

export const createPayment = async (fineId) => {
  const idempotencyKey = localStorage.getItem(`idem_${fineId}`) || crypto.randomUUID();
  localStorage.setItem(`idem_${fineId}`, idempotencyKey);
  const response = await api.post(
    "/api/payments/create-order",
    { fineId },
    {
      headers: {
        "Idempotency-Key": idempotencyKey,
      },
    }
  );
  return response.data;
};

export const choosePaymentOption = async (paymentId, choice) => {
  const response = await api.post(`/api/payments/${paymentId}/choice`, { choice });
  return response.data;
};

export const getReceipt = async (paymentId) => {
  const response = await api.get(`/api/payments/${paymentId}/receipt`);
  return response.data;
};
