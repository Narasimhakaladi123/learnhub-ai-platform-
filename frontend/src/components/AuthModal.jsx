import React, { useState } from 'react';
import { X, Sparkles, Lock, Mail, User, ShieldCheck } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

export const AuthModal = ({ isOpen, onClose }) => {
  const [tab, setTab] = useState('login');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [fullName, setFullName] = useState('');
  const [error, setError] = useState('');
  const { login, quickDemoLogin } = useAuth();

  if (!isOpen) return null;

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    const res = await login(email, password);
    if (res.success) {
      onClose();
    } else {
      setError(res.error || 'Authentication failed');
    }
  };

  const handleDemoSelect = (role) => {
    quickDemoLogin(role);
    onClose();
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-950/80 backdrop-blur-sm animate-fade-in">
      <div className="glass-panel w-full max-w-md rounded-2xl border border-slate-800 p-6 space-y-5 bg-slate-950/95 shadow-2xl">
        {/* Header */}
        <div className="flex items-center justify-between">
          <div className="flex items-center space-x-2">
            <div className="w-8 h-8 rounded-lg bg-brand-600 flex items-center justify-center text-white">
              <Sparkles className="w-4 h-4" />
            </div>
            <h3 className="font-extrabold text-white text-base">LearnHub AI Portal</h3>
          </div>
          <button onClick={onClose} className="text-slate-400 hover:text-white p-1 rounded-lg">
            <X className="w-4 h-4" />
          </button>
        </div>

        {/* 1-Click Demo Sign-in Banner */}
        <div className="p-3.5 rounded-xl bg-slate-900/90 border border-brand-500/30 space-y-2">
          <p className="text-[11px] font-bold text-brand-300 uppercase tracking-wider flex items-center space-x-1">
            <span>⚡ Instant Demo Access (No Password Required)</span>
          </p>
          <div className="grid grid-cols-3 gap-2">
            <button
              onClick={() => handleDemoSelect('student')}
              className="px-2 py-1.5 rounded-lg bg-brand-600/30 hover:bg-brand-600/50 border border-brand-500/40 text-[11px] font-bold text-brand-200 transition-colors"
            >
              🎓 Student
            </button>
            <button
              onClick={() => handleDemoSelect('counselor')}
              className="px-2 py-1.5 rounded-lg bg-indigo-600/30 hover:bg-indigo-600/50 border border-indigo-500/40 text-[11px] font-bold text-indigo-200 transition-colors"
            >
              👩‍💼 Advisor
            </button>
            <button
              onClick={() => handleDemoSelect('admin')}
              className="px-2 py-1.5 rounded-lg bg-purple-600/30 hover:bg-purple-600/50 border border-purple-500/40 text-[11px] font-bold text-purple-200 transition-colors"
            >
              🛡️ Admin
            </button>
          </div>
        </div>

        {/* Tabs */}
        <div className="flex border-b border-slate-800">
          <button
            onClick={() => setTab('login')}
            className={`flex-1 py-2 text-xs font-bold transition-colors ${tab === 'login' ? 'text-brand-400 border-b-2 border-brand-500' : 'text-slate-400'}`}
          >
            Sign In with JWT
          </button>
          <button
            onClick={() => setTab('register')}
            className={`flex-1 py-2 text-xs font-bold transition-colors ${tab === 'register' ? 'text-brand-400 border-b-2 border-brand-500' : 'text-slate-400'}`}
          >
            Create Student Account
          </button>
        </div>

        {error && (
          <div className="p-2.5 rounded-lg bg-red-950/40 border border-red-500/40 text-[11px] text-red-300">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-3.5">
          {tab === 'register' && (
            <div className="space-y-1">
              <label className="text-[11px] font-semibold text-slate-300">Full Name</label>
              <div className="relative">
                <User className="w-4 h-4 text-slate-500 absolute left-3 top-1/2 -translate-y-1/2" />
                <input
                  type="text"
                  required
                  placeholder="Alex Mercer"
                  value={fullName}
                  onChange={(e) => setFullName(e.target.value)}
                  className="w-full pl-9 pr-3 py-2 bg-slate-900 border border-slate-800 rounded-xl text-xs text-white placeholder-slate-500 focus:outline-none focus:border-brand-500"
                />
              </div>
            </div>
          )}

          <div className="space-y-1">
            <label className="text-[11px] font-semibold text-slate-300">Email Address</label>
            <div className="relative">
              <Mail className="w-4 h-4 text-slate-500 absolute left-3 top-1/2 -translate-y-1/2" />
              <input
                type="email"
                required
                placeholder="student@learnhub.com"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="w-full pl-9 pr-3 py-2 bg-slate-900 border border-slate-800 rounded-xl text-xs text-white placeholder-slate-500 focus:outline-none focus:border-brand-500"
              />
            </div>
          </div>

          <div className="space-y-1">
            <label className="text-[11px] font-semibold text-slate-300">Password</label>
            <div className="relative">
              <Lock className="w-4 h-4 text-slate-500 absolute left-3 top-1/2 -translate-y-1/2" />
              <input
                type="password"
                required
                placeholder="••••••••"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="w-full pl-9 pr-3 py-2 bg-slate-900 border border-slate-800 rounded-xl text-xs text-white placeholder-slate-500 focus:outline-none focus:border-brand-500"
              />
            </div>
          </div>

          <button
            type="submit"
            className="w-full py-2.5 rounded-xl bg-brand-600 hover:bg-brand-500 text-white font-bold text-xs shadow-glow transition-all"
          >
            {tab === 'login' ? 'Authenticate & Sign In' : 'Register Account'}
          </button>
        </form>
      </div>
    </div>
  );
};
