import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import { getBookById, getBookPreview } from "../api/booksApi";
import { borrowBook } from "../api/borrowApi";
import { useAuth } from "../context/AuthContext";

const BookDetails = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { isAuthenticated } = useAuth();
  const [book, setBook] = useState(null);
  const [preview, setPreview] = useState(null);
  const [loading, setLoading] = useState(true);
  const [borrowing, setBorrowing] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchBook = async () => {
      try {
        setLoading(true);
        const [bookData, previewData] = await Promise.allSettled([
          getBookById(id),
          getBookPreview(id),
        ]);
        if (bookData.status === "fulfilled") setBook(bookData.value);
        if (previewData.status === "fulfilled") setPreview(previewData.value);
      } catch (err) {
        setError(err.response?.data?.message || "Failed to fetch book details");
      } finally {
        setLoading(false);
      }
    };
    fetchBook();
  }, [id]);

  const handleBorrow = async () => {
    if (!isAuthenticated) {
      toast.error("Please login to borrow this book");
      navigate("/login");
      return;
    }

    if (!window.confirm("Are you sure you want to borrow this book?")) {
      return;
    }

    setBorrowing(true);
    try {
      const response = await borrowBook({ bookId: id });
      if (response?.status >= 200 && response?.status < 300) {
        toast.success("Book borrowed successfully!");
        navigate("/borrowings");
      } else {
        toast.error(response?.message || "Failed to borrow book");
      }
    } catch (err) {
      toast.error(err.response?.data?.message || "Failed to borrow book");
    } finally {
      setBorrowing(false);
    }
  };

  if (loading) {
    return (
      <div className="container mx-auto px-4 py-8">
        <div className="bg-gray-100 animate-pulse h-64 rounded"></div>
      </div>
    );
  }

  if (error || !book) {
    return (
      <div className="container mx-auto px-4 py-8">
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
          {error || "Book not found"}
        </div>
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="bg-white border rounded p-6 shadow-sm">
        <h1 className="text-2xl font-bold mb-4">{book.title}</h1>
        <p className="text-gray-700 mb-2">
          <strong>Author:</strong> {book.author}
        </p>
        {book.isbn && (
          <p className="text-gray-700 mb-2">
            <strong>ISBN:</strong> {book.isbn}
          </p>
        )}
        {book.publicationDate && (
          <p className="text-gray-700 mb-2">
            <strong>Publication Date:</strong> {book.publicationDate}
          </p>
        )}
        {book.pages && (
          <p className="text-gray-700 mb-2">
            <strong>Pages:</strong> {book.pages}
          </p>
        )}
        {book.description && (
          <div className="mt-4 mb-4">
            <strong className="block mb-2">Description:</strong>
            <p className="text-gray-600">{book.description}</p>
          </div>
        )}
        <div className="mb-4">
          <span
            className={`text-sm px-3 py-1 rounded ${
              book.available || book.copiesAvailable > 0
                ? "bg-green-100 text-green-800"
                : "bg-red-100 text-red-800"
            }`}
          >
            {book.available || book.copiesAvailable > 0 ? "Available" : "Not Available"}
          </span>
        </div>
        {preview && preview.content && (
          <div className="mt-6 mb-6">
            <strong className="block mb-2">Preview:</strong>
            <div className="bg-gray-50 p-4 rounded border text-sm text-gray-700">
              {preview.content}
            </div>
          </div>
        )}
        {isAuthenticated ? (
          <button
            onClick={handleBorrow}
            disabled={borrowing || !(book.available || book.copiesAvailable > 0)}
            className="px-6 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 disabled:opacity-50"
          >
            {borrowing ? "Borrowing..." : "Borrow Book"}
          </button>
        ) : (
          <p className="text-gray-600">
            Please{" "}
            <a href="/login" className="text-blue-600 hover:underline">
              login
            </a>{" "}
            to borrow this book.
          </p>
        )}
      </div>
    </div>
  );
};

export default BookDetails;
