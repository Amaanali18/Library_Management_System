import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { getReceipt } from "../api/paymentsApi";

const Receipt = () => {
  const { paymentId } = useParams();
  const [receipt, setReceipt] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchReceipt = async () => {
      try {
        setLoading(true);
        const data = await getReceipt(paymentId);
        setReceipt(data);
      } catch (err) {
        setError(err.response?.data?.message || "Failed to fetch receipt");
      } finally {
        setLoading(false);
      }
    };
    fetchReceipt();
  }, [paymentId]);

  const handlePrint = () => {
    window.print();
  };

  if (loading) {
    return (
      <div className="container mx-auto px-4 py-8">
        <div className="bg-gray-100 animate-pulse h-64 rounded"></div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="container mx-auto px-4 py-8">
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
          {error}
        </div>
      </div>
    );
  }

  if (!receipt) {
    return (
      <div className="container mx-auto px-4 py-8">
        <p className="text-gray-600">Receipt not found</p>
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="bg-white border rounded p-6 shadow-sm max-w-2xl mx-auto print:shadow-none">
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-2xl font-bold">Receipt</h1>
          <button
            onClick={handlePrint}
            className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 print:hidden"
          >
            Print Receipt
          </button>
        </div>
        <div className="space-y-3">
          <p>
            <strong>Receipt Number:</strong> {receipt.receiptNumber || receipt.id}
          </p>
          <p>
            <strong>Payment ID:</strong> {receipt.paymentId || paymentId}
          </p>
          {receipt.fineId && (
            <p>
              <strong>Fine ID:</strong> {receipt.fineId}
            </p>
          )}
          {receipt.borrowerId && (
            <p>
              <strong>Borrower ID:</strong> {receipt.borrowerId}
            </p>
          )}
          {receipt.bookTitle && (
            <p>
              <strong>Book Title:</strong> {receipt.bookTitle}
            </p>
          )}
          {receipt.author && (
            <p>
              <strong>Author:</strong> {receipt.author}
            </p>
          )}
          {receipt.isbn && (
            <p>
              <strong>ISBN:</strong> {receipt.isbn}
            </p>
          )}
          {receipt.bookCopyId && (
            <p>
              <strong>Book Copy ID:</strong> {receipt.bookCopyId}
            </p>
          )}
          <p>
            <strong>Fine Amount:</strong> ₹{receipt.fineAmount || receipt.amount}
          </p>
          <p>
            <strong>Currency:</strong> {receipt.currency || "INR"}
          </p>
          {receipt.daysOverdue !== undefined && (
            <p>
              <strong>Days Overdue:</strong> {receipt.daysOverdue}
            </p>
          )}
          {receipt.fineStatus && (
            <p>
              <strong>Fine Status:</strong> {receipt.fineStatus}
            </p>
          )}
          {receipt.paymentStatus && (
            <p>
              <strong>Payment Status:</strong> {receipt.paymentStatus}
            </p>
          )}
          {receipt.borrowDate && (
            <p>
              <strong>Borrow Date:</strong> {new Date(receipt.borrowDate).toLocaleString()}
            </p>
          )}
          {receipt.dueDate && (
            <p>
              <strong>Due Date:</strong> {new Date(receipt.dueDate).toLocaleString()}
            </p>
          )}
          {receipt.returnDate && (
            <p>
              <strong>Return Date:</strong> {new Date(receipt.returnDate).toLocaleString()}
            </p>
          )}
          {receipt.timestamp && (
            <p>
              <strong>Receipt Timestamp:</strong> {new Date(receipt.timestamp).toLocaleString()}
            </p>
          )}
          {receipt.paymentTimestamp && (
            <p>
              <strong>Payment Timestamp:</strong> {new Date(receipt.paymentTimestamp).toLocaleString()}
            </p>
          )}
        </div>
        <div className="mt-6 text-center text-sm text-gray-500 print:text-xs">
          <p>This is a mock payment receipt.</p>
        </div>
      </div>
    </div>
  );
};

export default Receipt;
