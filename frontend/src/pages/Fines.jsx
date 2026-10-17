import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import toast from "react-hot-toast";
import { getMyFines } from "../api/finesApi";
import { createPayment, choosePaymentOption } from "../api/paymentsApi";

const Fines = () => {
  const [fines, setFines] = useState([]);
  const [loading, setLoading] = useState(true);
  const [processing, setProcessing] = useState(null);

  const fetchFines = async () => {
    try {
      setLoading(true);
      const data = await getMyFines();
      setFines(Array.isArray(data) ? data : data.content || []);
    } catch (error) {
      toast.error(error.response?.data?.message || "Failed to fetch fines");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchFines();
  }, []);

  const handlePayNow = async (fine) => {
    setProcessing(fine.id);
    try {
      const payment = await createPayment(fine.id);
      if (payment?.id || payment?.paymentId) {
        const pid = payment.id || payment.paymentId;
        const res = await choosePaymentOption(pid, "PAY_NOW");
        toast.success(res?.message || "Payment successful (Mock)");
        fetchFines();
      }
    } catch (error) {
      toast.error(error.response?.data?.message || "Payment failed");
    } finally {
      setProcessing(null);
    }
  };

  const handlePayLater = async (fine) => {
    setProcessing(fine.id);
    try {
      const payment = await createPayment(fine.id);
      if (payment?.id || payment?.paymentId) {
        const pid = payment.id || payment.paymentId;
        const res = await choosePaymentOption(pid, "PAY_LATER");
        toast.success(res?.message || "Payment deferred");
        fetchFines();
      }
    } catch (error) {
      toast.error(error.response?.data?.message || "Failed to process");
    } finally {
      setProcessing(null);
    }
  };

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-2xl font-bold mb-6">Fines</h1>
      {loading ? (
        <div className="bg-gray-100 animate-pulse h-32 rounded"></div>
      ) : fines.length === 0 ? (
        <p className="text-gray-600">No fines found.</p>
      ) : (
        <div className="overflow-x-auto">
          <table className="w-full bg-white border rounded">
            <thead className="bg-gray-50">
              <tr>
                <th className="text-left p-3 border-b">Amount</th>
                <th className="text-left p-3 border-b">Days Overdue</th>
                <th className="text-left p-3 border-b">Status</th>
                <th className="text-left p-3 border-b">Book</th>
                <th className="text-left p-3 border-b">Created</th>
                <th className="text-left p-3 border-b">Actions</th>
              </tr>
            </thead>
            <tbody>
              {fines.map((fine) => (
                <tr key={fine.id}>
                  <td className="p-3 border-b">₹{fine.amount}</td>
                  <td className="p-3 border-b">{fine.daysOverdue}</td>
                  <td className="p-3 border-b">
                    <span className={`text-xs px-2 py-1 rounded ${
                      fine.status === "PAID"
                        ? "bg-green-100 text-green-800"
                        : fine.status === "PENDING"
                        ? "bg-yellow-100 text-yellow-800"
                        : "bg-red-100 text-red-800"
                    }`}>
                      {fine.status}
                    </span>
                  </td>
                  <td className="p-3 border-b">{fine.bookTitle || fine.book?.title}</td>
                  <td className="p-3 border-b">
                    {fine.createdAt ? new Date(fine.createdAt).toLocaleDateString() : "N/A"}
                  </td>
                  <td className="p-3 border-b">
                    {fine.status !== "PAID" && (
                      <div className="flex gap-2">
                        <button
                          onClick={() => handlePayNow(fine)}
                          disabled={processing === fine.id}
                          className="text-sm px-3 py-1 bg-green-600 text-white rounded hover:bg-green-700 disabled:opacity-50"
                        >
                          Pay Now
                        </button>
                        <button
                          onClick={() => handlePayLater(fine)}
                          disabled={processing === fine.id}
                          className="text-sm px-3 py-1 bg-yellow-600 text-white rounded hover:bg-yellow-700 disabled:opacity-50"
                        >
                          Pay Later
                        </button>
                      </div>
                    )}
                    {fine.status === "PAID" && fine.paymentId && (
                      <Link
                        to={`/receipts/${fine.paymentId}`}
                        className="text-sm text-blue-600 hover:underline"
                      >
                        View Receipt
                      </Link>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default Fines;
