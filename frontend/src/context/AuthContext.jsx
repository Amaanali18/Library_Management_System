import { createContext, useContext, useState, useEffect } from "react";
import { login as loginApi, signup as signupApi, logout as logoutApi } from "../api/authApi";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [accessToken, setAccessToken] = useState(localStorage.getItem("accessToken"));
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const storedUser = localStorage.getItem("user");
    if (storedUser && accessToken) {
      setUser(JSON.parse(storedUser));
    }
    setLoading(false);
  }, [accessToken]);

  const login = async (credentials) => {
    const response = await loginApi(credentials);
    if (response?.token) {
      setAccessToken(response.token);
      localStorage.setItem("accessToken", response.token);
      const userData = response.user || { email: credentials.email, role: response.role || "USER" };
      setUser(userData);
      localStorage.setItem("user", JSON.stringify(userData));
    }
    return response;
  };

  const signup = async (userData) => {
    const response = await signupApi(userData);
    return response;
  };

  const logout = async () => {
    try {
      await logoutApi();
    } catch (error) {
      // Ignore logout errors
    } finally {
      setAccessToken(null);
      setUser(null);
      localStorage.removeItem("accessToken");
      localStorage.removeItem("user");
    }
  };

  const value = {
    user,
    accessToken,
    loading,
    login,
    signup,
    logout,
    isAuthenticated: !!accessToken,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
};
