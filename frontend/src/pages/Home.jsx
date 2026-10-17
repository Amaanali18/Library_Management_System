import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { getBooks } from "../api/booksApi";

const Home = () => {
  const [featuredBooks, setFeaturedBooks] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchFeatured = async () => {
      try {
        const data = await getBooks({ limit: 6 });
        const books = Array.isArray(data) ? data : data.content || [];
        setFeaturedBooks(books.slice(0, 6));
      } catch (error) {
        console.error("Error fetching books:", error);
      } finally {
        setLoading(false);
      }
    };
    fetchFeatured();
  }, []);

  return (
    <div>
      <section className="bg-gradient-to-r from-blue-600 to-indigo-700 text-white py-16">
        <div className="container mx-auto px-4">
          <div className="max-w-3xl">
            <h1 className="text-4xl md:text-5xl font-bold mb-4">
              Discover Your Next Read
            </h1>
            <p className="text-xl mb-6">
              Browse, borrow, and manage your library books with ease.
            </p>
            <div className="flex gap-4 flex-wrap">
              <Link
                to="/books"
                className="px-6 py-3 bg-white text-blue-600 font-semibold rounded hover:bg-gray-100"
              >
                Browse Catalogue
              </Link>
              <Link
                to="/signup"
                className="px-6 py-3 border-2 border-white text-white font-semibold rounded hover:bg-white hover:text-blue-600"
              >
                Get Started
              </Link>
            </div>
          </div>
        </div>
      </section>

      <section className="py-12">
        <div className="container mx-auto px-4">
          <h2 className="text-2xl font-bold mb-8">Featured Books</h2>
          {loading ? (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {[...Array(6)].map((_, i) => (
                <div key={i} className="bg-gray-100 animate-pulse h-64 rounded"></div>
              ))}
            </div>
          ) : featuredBooks.length === 0 ? (
            <p className="text-gray-600">No books available at the moment.</p>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {featuredBooks.map((book) => (
                <div key={book.id} className="bg-white border rounded p-4 shadow-sm hover:shadow-md">
                  <h3 className="font-semibold text-lg mb-2">{book.title}</h3>
                  <p className="text-gray-600 mb-2">By {book.author}</p>
                  <p className="text-sm text-gray-500 mb-4 line-clamp-2">
                    {book.description}
                  </p>
                  <div className="flex justify-between items-center">
                    <span className={`text-sm px-2 py-1 rounded ${
                      book.available ? "bg-green-100 text-green-800" : "bg-red-100 text-red-800"
                    }`}>
                      {book.available ? "Available" : "Borrowed"}
                    </span>
                    <Link
                      to={`/books/${book.id}`}
                      className="text-blue-600 hover:underline text-sm"
                    >
                      View Details
                    </Link>
                  </div>
                </div>
              ))}
            </div>
          )}
          <div className="text-center mt-8">
            <Link
              to="/books"
              className="px-6 py-3 bg-blue-600 text-white rounded hover:bg-blue-700"
            >
              View All Books
            </Link>
          </div>
        </div>
      </section>
    </div>
  );
};

export default Home;
