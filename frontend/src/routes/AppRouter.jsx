import { BrowserRouter, Routes, Route } from "react-router-dom";
import { Toaster } from "react-hot-toast";
import Home from "../pages/Home";
import Login from "../pages/Login";
import Signup from "../pages/Signup";
import Books from "../pages/Books";
import BookDetails from "../pages/BookDetails";
import Dashboard from "../pages/Dashboard";
import MyBorrowings from "../pages/MyBorrowings";
import Fines from "../pages/Fines";
import Receipt from "../pages/Receipt";
import Profile from "../pages/Profile";
import NotFound from "../pages/NotFound";
import MainLayout from "../layouts/MainLayout";
import ProtectedRoute from "./ProtectedRoute";

export default function AppRouter() {
  return (
    <BrowserRouter>
      <Toaster position="top-right" />
      <Routes>
        <Route path="/" element={<MainLayout />}>
          <Route index element={<Home />} />
          <Route path="books" element={<Books />} />
          <Route path="books/:id" element={<BookDetails />} />
          <Route path="login" element={<Login />} />
          <Route path="signup" element={<Signup />} />
          <Route
            path="dashboard"
            element={
              <ProtectedRoute>
                <Dashboard />
              </ProtectedRoute>
            }
          />
          <Route
            path="borrowings"
            element={
              <ProtectedRoute>
                <MyBorrowings />
              </ProtectedRoute>
            }
          />
          <Route
            path="fines"
            element={
              <ProtectedRoute>
                <Fines />
              </ProtectedRoute>
            }
          />
          <Route
            path="receipts/:paymentId"
            element={
              <ProtectedRoute>
                <Receipt />
              </ProtectedRoute>
            }
          />
          <Route
            path="profile"
            element={
              <ProtectedRoute>
                <Profile />
              </ProtectedRoute>
            }
          />
          <Route
            path="admin/*"
            element={
              <ProtectedRoute allowedRoles={["ADMIN", "LIBRARIAN"]}>
                <div className="container mx-auto px-4 py-8">
                  <h1 className="text-2xl font-bold mb-4">Admin Panel</h1>
                  <p className="text-gray-600">Admin/Librarian features coming soon.</p>
                </div>
              </ProtectedRoute>
            }
          />
        </Route>
        <Route path="*" element={<NotFound />} />
      </Routes>
    </BrowserRouter>
  );
}
