import React from 'react';
import { useAuth } from '../context/AuthContext';
import { 
  Sparkles, 
  Compass, 
  BarChart3, 
  Map, 
  FileCheck2, 
  Mic2, 
  FileText, 
  ShieldCheck, 
  LogOut, 
  UserCheck, 
  MessageSquareCode,
  Bell
} from 'lucide-react';

export const Navbar = ({ currentTab, setCurrentTab, openCopilot, openAuth }) => {
  const { user, isAuthenticated, logout, quickDemoLogin, isAdmin } = useAuth();

  return (
    <header className="sticky top-0 z-50 glass-panel border-b border-slate-800/80 bg-slate-950/80 backdrop-blur-md">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          {/* Logo */}
          <div 
            onClick={() => setCurrentTab('dashboard')} 
            className="flex items-center space-x-3 cursor-pointer group"
          >
            <div className="w-10 h-10 rounded-xl bg-gradient-to-tr from-brand-600 via-indigo-500 to-purple-500 flex items-center justify-center shadow-glow group-hover:scale-105 transition-transform duration-200">
              <Sparkles className="w-5 h-5 text-white" />
            </div>
            <div>
              <div className="flex items-center space-x-2">
                <span className="font-extrabold text-lg tracking-tight bg-gradient-to-r from-white via-slate-100 to-brand-300 bg-clip-text text-transparent">
                  LearnHub
                </span>
                <span className="px-1.5 py-0.5 text-[10px] font-semibold bg-brand-500/20 text-brand-300 rounded border border-brand-500/30">
                  AI 2.0
                </span>
              </div>
              <p className="text-[10px] text-slate-400 font-medium">Career & Learning Intelligence</p>
            </div>
          </div>

          {/* Navigation Links */}
          <nav className="hidden lg:flex items-center space-x-1">
            {[
              { id: 'dashboard', label: 'Dashboard', icon: BarChart3 },
              { id: 'careers', label: 'Tracks', icon: Compass },
              { id: 'skill-gap', label: 'AI Gap Analyzer', icon: FileCheck2 },
              { id: 'roadmap', label: 'Roadmap', icon: Map },
              { id: 'assessments', label: 'Quizzes', icon: FileCheck2 },
              { id: 'interview', label: 'Mock Interview', icon: Mic2 },
              { id: 'resume', label: 'ATS Resume', icon: FileText },
              ...(isAdmin ? [{ id: 'admin', label: 'Admin', icon: ShieldCheck }] : [])
            ].map(tab => {
              const Icon = tab.icon;
              const isActive = currentTab === tab.id;
              return (
                <button
                  key={tab.id}
                  onClick={() => setCurrentTab(tab.id)}
                  className={`flex items-center space-x-2 px-3 py-1.5 rounded-lg text-xs font-semibold transition-all duration-150 ${
                    isActive 
                      ? 'bg-brand-500/15 text-brand-300 border border-brand-500/30 shadow-sm' 
                      : 'text-slate-400 hover:text-slate-200 hover:bg-slate-900/60'
                  }`}
                >
                  <Icon className={`w-3.5 h-3.5 ${isActive ? 'text-brand-400' : 'text-slate-400'}`} />
                  <span>{tab.label}</span>
                </button>
              );
            })}
          </nav>

          {/* Right Controls */}
          <div className="flex items-center space-x-3">
            {/* AI Assistant Pill Button */}
            <button
              onClick={openCopilot}
              className="flex items-center space-x-1.5 px-3 py-1.5 text-xs font-semibold rounded-lg bg-gradient-to-r from-indigo-500/20 to-purple-500/20 text-indigo-300 border border-indigo-500/30 hover:border-indigo-500/60 hover:shadow-glow transition-all"
            >
              <MessageSquareCode className="w-3.5 h-3.5 text-indigo-400" />
              <span className="hidden sm:inline">Ask AI Advisor</span>
            </button>

            {/* Quick Demo Selector */}
            <div className="hidden md:flex items-center space-x-1 bg-slate-900/80 p-1 rounded-lg border border-slate-800">
              <span className="text-[11px] text-slate-400 px-1 font-mono">Demo:</span>
              <button
                onClick={() => quickDemoLogin('student')}
                title="Switch to Student account"
                className={`px-2 py-0.5 text-[11px] rounded font-medium transition-colors ${user?.role === 'ROLE_STUDENT' ? 'bg-brand-600 text-white' : 'text-slate-300 hover:text-white'}`}
              >
                Student
              </button>
              <button
                onClick={() => quickDemoLogin('admin')}
                title="Switch to Admin account"
                className={`px-2 py-0.5 text-[11px] rounded font-medium transition-colors ${user?.role === 'ROLE_ADMIN' ? 'bg-purple-600 text-white' : 'text-slate-300 hover:text-white'}`}
              >
                Admin
              </button>
            </div>

            {/* User Profile or Login */}
            {isAuthenticated ? (
              <div className="flex items-center space-x-2 pl-2 border-l border-slate-800">
                <div className="w-8 h-8 rounded-full bg-slate-800 border border-slate-700 flex items-center justify-center font-bold text-xs text-brand-300">
                  {user?.fullName?.charAt(0) || 'U'}
                </div>
                <div className="hidden xl:block text-left">
                  <p className="text-xs font-semibold text-slate-200 leading-none">{user?.fullName || 'Alex Mercer'}</p>
                  <p className="text-[10px] text-slate-400 capitalize">{user?.role?.replace('ROLE_', '').toLowerCase()}</p>
                </div>
                <button
                  onClick={logout}
                  title="Logout"
                  className="p-1.5 text-slate-400 hover:text-red-400 hover:bg-slate-900 rounded-lg transition-colors"
                >
                  <LogOut className="w-4 h-4" />
                </button>
              </div>
            ) : (
              <button
                onClick={openAuth}
                className="px-4 py-1.5 text-xs font-bold rounded-lg bg-brand-600 hover:bg-brand-500 text-white shadow-glow transition-all"
              >
                Sign In
              </button>
            )}
          </div>
        </div>
      </div>
    </header>
  );
};
