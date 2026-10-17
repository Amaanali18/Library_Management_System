import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import { getMyBorrowings, returnBook } from "../api/borrowApi";

const MyBorrowings = () => {
  const [borrowings, setBorrowings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [returningId, setReturningId] = useState(null);
  const navigate = useNavigate();

  const fetchBorrowings = async () => {
    try {
      setLoading(true);
      const data = await getMyBorrowings();
      setBorrowings(Array.isArray(data) ? data : data.content || []);
    } catch (error) {
      toast.error(error.response?.data?.message || "Failed to fetch borrowings");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchBorrowings();
  }, []);

  const handleReturn = async (borrowId) => {
    if (!window.confirm("Confirm return of this book?")) return;
    setReturningId(borrowId);
    try {
      await returnBook(borrowId);
      toast.success("Book returned successfully");
      fetchBorrowings();
    } catch (error) {
      toast.error(error.response?.data?.message || "Failed to return book");
    } finally {
      setReturningId(null);
    }
  };

  const isActive = (b) => !b.returnDate && b.status !== "RETURNED";

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-2xl font-bold mb-6">My Borrowings</h1>
      {loading ? (
        <div className="bg-gray-100 animate-pulse h-32 rounded"></div>
      ) : borrowings.length === 0 ? (
        <p className="text-gray-600">No borrowings found.</p>
      ) : (
        <div className="overflow-x-auto">
          <table className="w-full bg-white border rounded">
            <thead className="bg-gray-50">
              <tr>
                <th className="text-left p-3 border-b">Book</th>
                <th className="text-left p-3 border-b">Borrow Date</th>
                <th className="text-left p-3 border-b">Due Date</th>
                <th className="text-left p-3 border-b">Return Date</th>
                <th className="text-left p-3 border-b">Status</th>
                <th className="text-left p-3 border-b">Actions</th>
              </tr>
            </thead>
            <tbody>
              {borrowings.map((b) => (
                <tr key={b.id}>
                  <td className="p-3 border-b">{b.bookTitle || b.book?.title}</td>
                  <td className="p-3 border-b">
                    {b.borrowDate ? new Date(b.borrowDate).toLocaleDateString() : "N/A"}
                  </td>
                  <td className="p-3 border-b">
                    {b.dueDate ? new Date(b.dueDate).toLocaleDateString() : "N/A"}
                  </td>
                  <td className="p-3 border-b">
                    {b.returnDate ? new Date(b.returnDate).toLocaleDateString() : "-"}
                  </td>
                  <td className="p-3 border-b">
                    <span className={`text-xs px-2 py-1 rounded ${
                      isActive(b) ? "bg-yellow-100 text-yellow-800" : "bg-green-100 text-green-800"
                    }`}>
                      {isActive(b) ? "Active" : "Returned"}
                    </span>
                  </td>
                  <td className="p-3 border-b">
                    {isActive(b) && (
                      <button
                        onClick={() => handleReturn(b.id)}
                        disabled={returningId === b.id}
                        className="text-sm px-3 py-1 bg-blue-600 text-white rounded hover:bg-blue-700 disabled:opacity-50"
                      >
                        {returningId === b.id ? "Returning..." : "Return"}
                      </button>
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

export default MyBorrowings;
