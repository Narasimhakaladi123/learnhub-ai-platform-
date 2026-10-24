import React, { createContext, useContext, useState, useEffect } from 'react';
import { authApi } from '../services/api';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(() => {
    const saved = localStorage.getItem('learnhub_user');
    return saved ? JSON.parse(saved) : null;
  });
  const [token, setToken] = useState(() => localStorage.getItem('learnhub_token') || null);
  const [loading, setLoading] = useState(false);

  const login = async (email, password) => {
    setLoading(true);
    try {
      const data = await authApi.login({ email, password });
      const userData = {
        userId: data.userId,
        email: data.email,
        fullName: data.fullName,
        role: data.role || 'ROLE_STUDENT',
      };
      setToken(data.token);
      setUser(userData);
      localStorage.setItem('learnhub_token', data.token);
      localStorage.setItem('learnhub_user', JSON.stringify(userData));
      return { success: true, user: userData };
    } catch (err) {
      return { success: false, error: err.message };
    } finally {
      setLoading(false);
    }
  };

  const quickDemoLogin = async (role) => {
    let email = 'student@learnhub.com';
    let fullName = 'Alex Mercer (Student)';
    let roleType = 'ROLE_STUDENT';

    if (role === 'admin') {
      email = 'admin@learnhub.com';
      fullName = 'System Administrator';
      roleType = 'ROLE_ADMIN';
    } else if (role === 'counselor') {
      email = 'counselor@learnhub.com';
      fullName = 'Sarah Mitchell (Career Advisor)';
      roleType = 'ROLE_COUNSELOR';
    }

    const demoUser = {
      userId: `demo-${role}`,
      email,
      fullName,
      role: roleType,
    };
    const demoToken = `mock-token-${role}-${Date.now()}`;

    setToken(demoToken);
    setUser(demoUser);
    localStorage.setItem('learnhub_token', demoToken);
    localStorage.setItem('learnhub_user', JSON.stringify(demoUser));
  };

  const logout = () => {
    setUser(null);
    setToken(null);
    localStorage.removeItem('learnhub_token');
    localStorage.removeItem('learnhub_user');
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        token,
        isAuthenticated: !!token,
        isAdmin: user?.role === 'ROLE_ADMIN',
        isCounselor: user?.role === 'ROLE_COUNSELOR',
        loading,
        login,
        quickDemoLogin,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
