import React from 'react';
import { Sparkles, ArrowRight, CheckCircle2, ShieldCheck, Cpu, Code2, Database } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

export const HeroSection = ({ setCurrentTab }) => {
  const { quickDemoLogin } = useAuth();

  return (
    <div className="relative overflow-hidden pt-10 pb-16 lg:pt-14 lg:pb-20 border-b border-slate-800/60">
      {/* Background Glow FX */}
      <div className="absolute top-0 left-1/2 -translate-x-1/2 w-3/4 h-80 bg-gradient-to-b from-brand-600/20 via-indigo-500/10 to-transparent blur-3xl pointer-events-none -z-10" />

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
        {/* Release Pill */}
        <div className="inline-flex items-center space-x-2 px-3 py-1 rounded-full bg-slate-900/90 border border-brand-500/30 text-xs text-brand-300 mb-6 shadow-sm">
          <Sparkles className="w-3.5 h-3.5 text-brand-400 animate-pulse" />
          <span className="font-semibold tracking-wide">Enterprise EdTech 2025 Edition</span>
          <span className="text-slate-500">•</span>
          <span className="text-slate-300">Java 17 • Spring Boot 3 • MySQL • React • RAG</span>
        </div>

        {/* Hero Title */}
        <h1 className="text-4xl sm:text-5xl lg:text-6xl font-black text-white tracking-tight leading-[1.15] max-w-4xl mx-auto">
          From Classroom to <span className="bg-gradient-to-r from-brand-400 via-indigo-300 to-purple-400 bg-clip-text text-transparent">Top Software Engineer</span> with AI Guidance.
        </h1>

        {/* Subtitle */}
        <p className="mt-5 text-base sm:text-lg text-slate-300 max-w-2xl mx-auto font-normal leading-relaxed">
          Pinpoint your technical skill gaps, follow personalized milestone roadmaps, practice with an AI mock interviewer, and score your resume against real ATS criteria.
        </p>

        {/* CTA Buttons */}
        <div className="mt-8 flex flex-wrap items-center justify-center gap-3">
          <button
            onClick={() => setCurrentTab('skill-gap')}
            className="flex items-center space-x-2 px-6 py-3 rounded-xl bg-gradient-to-r from-brand-600 to-indigo-600 hover:from-brand-500 hover:to-indigo-500 text-white font-bold text-sm shadow-glow transition-all duration-200 hover:-translate-y-0.5"
          >
            <span>Analyze My Skill Gap</span>
            <ArrowRight className="w-4 h-4" />
          </button>

          <button
            onClick={() => setCurrentTab('careers')}
            className="px-6 py-3 rounded-xl bg-slate-900 hover:bg-slate-800 text-slate-200 border border-slate-700 font-semibold text-sm transition-all duration-200"
          >
            Browse Career Tracks
          </button>

          <button
            onClick={() => quickDemoLogin('student')}
            className="px-5 py-3 rounded-xl bg-purple-950/40 hover:bg-purple-900/40 text-purple-300 border border-purple-500/40 font-semibold text-sm transition-all duration-200"
          >
            ⚡ 1-Click Demo As Student
          </button>
        </div>

        {/* Feature Highlights Grid */}
        <div className="mt-14 grid grid-cols-2 sm:grid-cols-4 gap-4 max-w-4xl mx-auto text-left">
          {[
            { title: "RAG Career Matching", desc: "Vector similarity aligned to real job market data", icon: Cpu },
            { title: "Interactive Roadmaps", desc: "Track progress from foundations to capstones", icon: Code2 },
            { title: "Voice & Text AI Mocks", desc: "Instant evaluation with STAR methodology", icon: Sparkles },
            { title: "ATS Resume Parsing", desc: "Keyword gap analysis with PDF extraction", icon: Database }
          ].map((f, i) => {
            const Icon = f.icon;
            return (
              <div key={i} className="glass-card p-4 rounded-xl hover:border-brand-500/40 transition-colors">
                <Icon className="w-5 h-5 text-brand-400 mb-2" />
                <h4 className="text-xs font-bold text-slate-200">{f.title}</h4>
                <p className="text-[11px] text-slate-400 mt-1 leading-snug">{f.desc}</p>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
};
