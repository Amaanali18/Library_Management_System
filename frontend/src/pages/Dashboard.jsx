import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { getMyBorrowings } from "../api/borrowApi";

const Dashboard = () => {
  const [borrowings, setBorrowings] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const data = await getMyBorrowings();
        const borrowList = Array.isArray(data) ? data : data.content || [];
        setBorrowings(borrowList);
      } catch (error) {
        console.error("Error fetching borrowings:", error);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  const activeBorrowings = borrowings.filter((b) => b.returnDate === null || b.status === "BORROWED" || b.status === "ACTIVE");
  const overdueBorrowings = borrowings.filter((b) => {
    if (b.returnDate) return false;
    const due = new Date(b.dueDate);
    return due < new Date();
  });

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-2xl font-bold mb-6">Dashboard</h1>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-8">
        <div className="bg-white border rounded p-4 shadow-sm">
          <p className="text-gray-600 text-sm">Active Borrowings</p>
          <p className="text-2xl font-bold">{activeBorrowings.length}</p>
        </div>
        <div className="bg-white border rounded p-4 shadow-sm">
          <p className="text-gray-600 text-sm">Overdue Items</p>
          <p className="text-2xl font-bold text-red-600">{overdueBorrowings.length}</p>
        </div>
        <div className="bg-white border rounded p-4 shadow-sm">
          <p className="text-gray-600 text-sm">Total Borrowed</p>
          <p className="text-2xl font-bold">{borrowings.length}</p>
        </div>
      </div>

      <div>
        <h2 className="text-xl font-semibold mb-4">Recent Activity</h2>
        {loading ? (
          <div className="bg-gray-100 animate-pulse h-32 rounded"></div>
        ) : borrowings.length === 0 ? (
          <p className="text-gray-600">
            No borrowings yet.{" "}
            <Link to="/books" className="text-blue-600 hover:underline">
              Browse books
            </Link>
          </p>
        ) : (
          <div className="space-y-2">
            {borrowings.slice(0, 5).map((b) => (
              <div key={b.id} className="bg-white border rounded p-3">
                <p className="font-medium">{b.bookTitle || b.book?.title}</p>
                <p className="text-sm text-gray-600">
                  Due: {b.dueDate ? new Date(b.dueDate).toLocaleDateString() : "N/A"}
                </p>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default Dashboard;
