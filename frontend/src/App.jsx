import React, { useState } from 'react';
import { AuthProvider } from './context/AuthContext';
import { Navbar } from './components/Navbar';
import { HeroSection } from './components/HeroSection';
import { DashboardView } from './components/DashboardView';
import { CareerTracksView } from './components/CareerTracksView';
import { SkillGapView } from './components/SkillGapView';
import { RoadmapView } from './components/RoadmapView';
import { AssessmentsView } from './components/AssessmentsView';
import { InterviewRoomView } from './components/InterviewRoomView';
import { ResumeATSView } from './components/ResumeATSView';
import { AdminPanelView } from './components/AdminPanelView';
import { AICopilotModal } from './components/AICopilotModal';
import { AuthModal } from './components/AuthModal';
import { Github, Code2, Sparkles, Database } from 'lucide-react';

function AppContent() {
  const [currentTab, setCurrentTab] = useState('dashboard');
  const [isCopilotOpen, setIsCopilotOpen] = useState(false);
  const [isAuthOpen, setIsAuthOpen] = useState(false);

  return (
    <div className="min-h-screen flex flex-col bg-slate-950 text-slate-100 selection:bg-brand-500 selection:text-white">
      {/* Navigation */}
      <Navbar
        currentTab={currentTab}
        setCurrentTab={setCurrentTab}
        openCopilot={() => setIsCopilotOpen(true)}
        openAuth={() => setIsAuthOpen(true)}
      />

      {/* Hero Banner (Shown on Dashboard or Career tracks) */}
      {currentTab === 'dashboard' && (
        <HeroSection setCurrentTab={setCurrentTab} />
      )}

      {/* Main View Container */}
      <main className="flex-1 max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {currentTab === 'dashboard' && <DashboardView setCurrentTab={setCurrentTab} />}
        {currentTab === 'careers' && <CareerTracksView setCurrentTab={setCurrentTab} />}
        {currentTab === 'skill-gap' && <SkillGapView setCurrentTab={setCurrentTab} />}
        {currentTab === 'roadmap' && <RoadmapView />}
        {currentTab === 'assessments' && <AssessmentsView />}
        {currentTab === 'interview' && <InterviewRoomView />}
        {currentTab === 'resume' && <ResumeATSView />}
        {currentTab === 'admin' && <AdminPanelView />}
      </main>

      {/* Modals */}
      <AICopilotModal isOpen={isCopilotOpen} onClose={() => setIsCopilotOpen(false)} />
      <AuthModal isOpen={isAuthOpen} onClose={() => setIsAuthOpen(false)} />

      {/* Modern Footer */}
      <footer className="glass-panel border-t border-slate-800/80 py-8 mt-12 bg-slate-950/90">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 flex flex-col sm:flex-row items-center justify-between gap-4">
          <div className="flex items-center space-x-2">
            <Sparkles className="w-4 h-4 text-brand-400" />
            <span className="text-xs font-bold text-slate-300">LearnHub AI Platform</span>
            <span className="text-slate-600">|</span>
            <span className="text-[11px] text-slate-500">Enterprise Full-Stack Career Intelligence Architecture</span>
          </div>

          <div className="flex items-center space-x-3 text-[11px] font-mono text-slate-400">
            <span className="px-2 py-0.5 rounded bg-slate-900 border border-slate-800">Java 17</span>
            <span className="px-2 py-0.5 rounded bg-slate-900 border border-slate-800">Spring Boot 3</span>
            <span className="px-2 py-0.5 rounded bg-slate-900 border border-slate-800">React.js</span>
            <span className="px-2 py-0.5 rounded bg-slate-900 border border-slate-800">MySQL 8</span>
            <span className="px-2 py-0.5 rounded bg-slate-900 border border-slate-800">Docker</span>
          </div>
        </div>
      </footer>
    </div>
  );
}

export default function App() {
  return (
    <AuthProvider>
      <AppContent />
    </AuthProvider>
  );
}
