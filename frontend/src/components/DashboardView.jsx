import React, { useState, useEffect } from 'react';
import { 
  Target, 
  Award, 
  CheckCircle2, 
  TrendingUp, 
  BookOpen, 
  ArrowRight, 
  Sparkles, 
  Zap, 
  FileText,
  Clock,
  Compass
} from 'lucide-react';
import { profileApi, roadmapApi } from '../services/api';

export const DashboardView = ({ setCurrentTab }) => {
  const [profile, setProfile] = useState(null);
  const [dashboard, setDashboard] = useState(null);
  const [activeRoadmap, setActiveRoadmap] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [profData, dashData, roadData] = await Promise.all([
          profileApi.getProfile(),
          profileApi.getDashboard(),
          roadmapApi.getActive()
        ]);
        setProfile(profData);
        setDashboard(dashData);
        setActiveRoadmap(roadData);
      } catch (err) {
        console.error("Dashboard fetch error:", err);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  if (loading) {
    return (
      <div className="py-20 text-center">
        <div className="w-10 h-10 border-4 border-brand-500 border-t-transparent rounded-full animate-spin mx-auto mb-4" />
        <p className="text-sm text-slate-400">Loading student intelligence profile...</p>
      </div>
    );
  }

  const readiness = dashboard?.readinessPercentage || 78;

  return (
    <div className="space-y-6">
      {/* Top Banner / Welcome */}
      <div className="relative overflow-hidden rounded-2xl glass-panel p-6 sm:p-8 border border-slate-800 bg-gradient-to-r from-slate-900/90 via-slate-900/60 to-brand-950/40">
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-6">
          <div className="space-y-2 max-w-xl">
            <div className="inline-flex items-center space-x-2 px-2.5 py-1 rounded-md bg-brand-500/10 border border-brand-500/20 text-xs text-brand-300">
              <Sparkles className="w-3.5 h-3.5 text-brand-400" />
              <span>Target Role: {dashboard?.targetCareer || "Full Stack Java Developer"}</span>
            </div>
            <h2 className="text-2xl sm:text-3xl font-extrabold text-white">
              Welcome back, {profile?.fullName || "Alex"}! 👋
            </h2>
            <p className="text-sm text-slate-300">
              You're currently in the <strong className="text-brand-300">top 15%</strong> of candidates targeting Enterprise Java engineering roles. Keep advancing your milestone roadmap.
            </p>
          </div>

          {/* Readiness Score Gauge */}
          <div className="flex items-center space-x-4 bg-slate-950/60 p-4 rounded-xl border border-slate-800">
            <div className="relative w-16 h-16 flex items-center justify-center">
              <svg className="w-16 h-16 -rotate-90">
                <circle cx="32" cy="32" r="28" stroke="currentColor" strokeWidth="6" className="text-slate-800" fill="transparent" />
                <circle 
                  cx="32" 
                  cy="32" 
                  r="28" 
                  stroke="currentColor" 
                  strokeWidth="6" 
                  strokeDasharray={175} 
                  strokeDashoffset={175 - (175 * readiness) / 100} 
                  strokeLinecap="round" 
                  className="text-brand-500 transition-all duration-1000 ease-out" 
                  fill="transparent" 
                />
              </svg>
              <span className="absolute font-black text-sm text-white">{readiness}%</span>
            </div>
            <div>
              <p className="text-xs font-bold text-slate-200">Market Readiness</p>
              <p className="text-[11px] text-emerald-400 font-medium">Ready for Junior/Mid Interviews</p>
            </div>
          </div>
        </div>
      </div>

      {/* KPI Metric Cards */}
      <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
        {[
          { label: "Verified Skills", value: dashboard?.verifiedSkillsCount || 5, sub: "+2 this month", icon: Award, color: "text-amber-400" },
          { label: "Quiz Avg. Score", value: `${dashboard?.averageAssessmentScore || 84.5}%`, sub: "3 tests taken", icon: TrendingUp, color: "text-brand-400" },
          { label: "Roadmap Milestones", value: `${dashboard?.completedRoadmapItemsCount || 4} / ${dashboard?.activeRoadmapItemsCount || 6}`, sub: "66% completed", icon: BookOpen, color: "text-indigo-400" },
          { label: "ATS Resume Score", value: "84 / 100", sub: "Top tier keywords", icon: FileText, color: "text-emerald-400" }
        ].map((stat, i) => {
          const Icon = stat.icon;
          return (
            <div key={i} className="glass-card p-5 rounded-xl border border-slate-800 hover:border-slate-700 transition-all">
              <div className="flex items-center justify-between">
                <span className="text-xs font-medium text-slate-400">{stat.label}</span>
                <Icon className={`w-4 h-4 ${stat.color}`} />
              </div>
              <p className="text-2xl font-black text-white mt-2">{stat.value}</p>
              <p className="text-[11px] text-slate-500 mt-1">{stat.sub}</p>
            </div>
          );
        })}
      </div>

      {/* Quick Action Bar */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-3">
        <button
          onClick={() => setCurrentTab('skill-gap')}
          className="flex items-center justify-between p-4 rounded-xl bg-slate-900/90 border border-slate-800 hover:border-brand-500/50 hover:bg-slate-900 transition-all text-left group"
        >
          <div className="flex items-center space-x-3">
            <div className="w-10 h-10 rounded-lg bg-brand-500/20 flex items-center justify-center text-brand-400">
              <Zap className="w-5 h-5" />
            </div>
            <div>
              <h4 className="text-xs font-bold text-slate-200 group-hover:text-brand-300">AI Gap Diagnostic</h4>
              <p className="text-[11px] text-slate-400">Target missing core skills</p>
            </div>
          </div>
          <ArrowRight className="w-4 h-4 text-slate-500 group-hover:text-brand-400 group-hover:translate-x-1 transition-all" />
        </button>

        <button
          onClick={() => setCurrentTab('interview')}
          className="flex items-center justify-between p-4 rounded-xl bg-slate-900/90 border border-slate-800 hover:border-indigo-500/50 hover:bg-slate-900 transition-all text-left group"
        >
          <div className="flex items-center space-x-3">
            <div className="w-10 h-10 rounded-lg bg-indigo-500/20 flex items-center justify-center text-indigo-400">
              <Clock className="w-5 h-5" />
            </div>
            <div>
              <h4 className="text-xs font-bold text-slate-200 group-hover:text-indigo-300">Mock AI Interview</h4>
              <p className="text-[11px] text-slate-400">Practice 3 instant questions</p>
            </div>
          </div>
          <ArrowRight className="w-4 h-4 text-slate-500 group-hover:text-indigo-400 group-hover:translate-x-1 transition-all" />
        </button>

        <button
          onClick={() => setCurrentTab('resume')}
          className="flex items-center justify-between p-4 rounded-xl bg-slate-900/90 border border-slate-800 hover:border-emerald-500/50 hover:bg-slate-900 transition-all text-left group"
        >
          <div className="flex items-center space-x-3">
            <div className="w-10 h-10 rounded-lg bg-emerald-500/20 flex items-center justify-center text-emerald-400">
              <FileText className="w-5 h-5" />
            </div>
            <div>
              <h4 className="text-xs font-bold text-slate-200 group-hover:text-emerald-300">ATS Resume Scanner</h4>
              <p className="text-[11px] text-slate-400">Upload PDF & verify score</p>
            </div>
          </div>
          <ArrowRight className="w-4 h-4 text-slate-500 group-hover:text-emerald-400 group-hover:translate-x-1 transition-all" />
        </button>
      </div>

      {/* Main Dual Grid: Verified Skills & Active Roadmap Items */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Verified Skills Card */}
        <div className="glass-panel p-6 rounded-2xl border border-slate-800 space-y-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center space-x-2">
              <Award className="w-4 h-4 text-brand-400" />
              <h3 className="font-bold text-sm text-slate-200">Verified Technical Skills</h3>
            </div>
            <button
              onClick={() => setCurrentTab('skill-gap')}
              className="text-xs text-brand-400 hover:underline font-medium"
            >
              Analyze Gap →
            </button>
          </div>

          <div className="flex flex-wrap gap-2">
            {profile?.skills?.map((s, idx) => (
              <div 
                key={idx}
                className="flex items-center space-x-2 px-3 py-1.5 rounded-lg bg-slate-900 border border-slate-800 text-xs"
              >
                <span className="font-semibold text-slate-200">{s.skillName}</span>
                <span className={`text-[10px] px-1.5 py-0.2 rounded font-mono ${
                  s.proficiency === 'ADVANCED' ? 'bg-emerald-500/20 text-emerald-300' :
                  s.proficiency === 'INTERMEDIATE' ? 'bg-brand-500/20 text-brand-300' :
                  'bg-amber-500/20 text-amber-300'
                }`}>
                  {s.proficiency}
                </span>
              </div>
            ))}
          </div>

          <div className="p-4 rounded-xl bg-slate-900/60 border border-slate-800 text-xs space-y-1 text-slate-400">
            <p className="font-semibold text-slate-300">💡 AI Recommendation:</p>
            <p>Adding Docker containerization and Redis caching to your verified skills will elevate your target match to 96% for Java full stack positions.</p>
          </div>
        </div>

        {/* Next Roadmap Milestones */}
        <div className="glass-panel p-6 rounded-2xl border border-slate-800 space-y-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center space-x-2">
              <BookOpen className="w-4 h-4 text-indigo-400" />
              <h3 className="font-bold text-sm text-slate-200">Active Milestone Track</h3>
            </div>
            <button
              onClick={() => setCurrentTab('roadmap')}
              className="text-xs text-indigo-400 hover:underline font-medium"
            >
              View Full Tree →
            </button>
          </div>

          <div className="space-y-3">
            {activeRoadmap?.items?.slice(0, 3).map((item, idx) => (
              <div 
                key={idx}
                className="flex items-center justify-between p-3 rounded-xl bg-slate-900/60 border border-slate-800/80"
              >
                <div className="space-y-0.5">
                  <p className="text-xs font-bold text-slate-200">{item.title}</p>
                  <p className="text-[11px] text-slate-400">{item.description}</p>
                </div>
                <span className={`text-[10px] font-bold px-2 py-0.5 rounded ${
                  item.status === 'COMPLETED' ? 'bg-emerald-500/20 text-emerald-300 border border-emerald-500/30' :
                  item.status === 'IN_PROGRESS' ? 'bg-brand-500/20 text-brand-300 border border-brand-500/30' :
                  'bg-slate-800 text-slate-400'
                }`}>
                  {item.status.replace('_', ' ')}
                </span>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};
