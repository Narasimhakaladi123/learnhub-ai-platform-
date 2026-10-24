import React, { useState, useEffect } from 'react';
import { FileCheck2, CheckCircle2, AlertTriangle, XCircle, ArrowRight, Sparkles, BookOpen } from 'lucide-react';
import { skillGapApi } from '../services/api';

export const SkillGapView = ({ setCurrentTab }) => {
  const [gapData, setGapData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchGaps = async () => {
      try {
        const data = await skillGapApi.getGaps();
        if (data && data.length > 0) setGapData(data[0]);
      } catch (err) {
        console.error("Skill gap error:", err);
      } finally {
        setLoading(false);
      }
    };
    fetchGaps();
  }, []);

  if (loading) {
    return (
      <div className="py-20 text-center">
        <div className="w-10 h-10 border-4 border-brand-500 border-t-transparent rounded-full animate-spin mx-auto mb-4" />
        <p className="text-sm text-slate-400">Synthesizing skill gap diagnostics...</p>
      </div>
    );
  }

  const matchScore = gapData?.overallMatchPercentage || 78;

  return (
    <div className="space-y-6">
      {/* Overview Banner */}
      <div className="glass-panel p-6 sm:p-8 rounded-2xl border border-slate-800 bg-gradient-to-r from-slate-900/90 to-brand-950/30">
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-6">
          <div className="space-y-2">
            <div className="inline-flex items-center space-x-1.5 px-2.5 py-0.5 rounded text-xs font-semibold bg-brand-500/20 text-brand-300 border border-brand-500/30">
              <Sparkles className="w-3.5 h-3.5" />
              <span>Diagnostic Report</span>
            </div>
            <h2 className="text-2xl font-black text-white">
              Skill Gap Analysis for <span className="text-brand-300">{gapData?.careerName || "Full Stack Java Developer"}</span>
            </h2>
            <p className="text-xs text-slate-300 max-w-xl leading-relaxed">
              {gapData?.aiAnalysisSummary || "Strong core backend grounding. Closing the gap on containerization (Docker) and advancing React state management will push your market readiness to 95%+."}
            </p>
          </div>

          {/* Match Meter */}
          <div className="flex items-center space-x-4 bg-slate-950/80 p-5 rounded-2xl border border-slate-800 shrink-0">
            <div className="text-center">
              <span className="text-4xl font-black text-brand-400">{matchScore}%</span>
              <p className="text-[11px] font-semibold text-slate-400 mt-0.5">Competency Alignment</p>
            </div>
          </div>
        </div>
      </div>

      {/* 3-Column Diagnostic Matrix */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        {/* Matched Skills */}
        <div className="glass-card p-5 rounded-xl border border-emerald-500/30 space-y-3">
          <div className="flex items-center space-x-2 text-emerald-400">
            <CheckCircle2 className="w-4 h-4" />
            <h3 className="font-bold text-xs uppercase tracking-wider">Acquired & Verified ({gapData?.matchingSkills?.length || 4})</h3>
          </div>
          <div className="space-y-2">
            {gapData?.matchingSkills?.map((skill, i) => (
              <div key={i} className="p-2.5 rounded-lg bg-emerald-950/20 border border-emerald-500/20 text-xs font-medium text-emerald-200 flex items-center justify-between">
                <span>{skill}</span>
                <span className="text-[10px] font-bold text-emerald-400">MATCH</span>
              </div>
            ))}
          </div>
        </div>

        {/* Missing Skills */}
        <div className="glass-card p-5 rounded-xl border border-red-500/30 space-y-3">
          <div className="flex items-center space-x-2 text-red-400">
            <XCircle className="w-4 h-4" />
            <h3 className="font-bold text-xs uppercase tracking-wider">Missing Core Skills ({gapData?.missingSkills?.length || 3})</h3>
          </div>
          <div className="space-y-2">
            {gapData?.missingSkills?.map((skill, i) => (
              <div key={i} className="p-2.5 rounded-lg bg-red-950/20 border border-red-500/20 text-xs font-medium text-red-200 flex items-center justify-between">
                <span>{skill}</span>
                <span className="text-[10px] font-bold text-red-400">GAP</span>
              </div>
            ))}
          </div>
        </div>

        {/* Weak / Proficiency Upgrade Needed */}
        <div className="glass-card p-5 rounded-xl border border-amber-500/30 space-y-3">
          <div className="flex items-center space-x-2 text-amber-400">
            <AlertTriangle className="w-4 h-4" />
            <h3 className="font-bold text-xs uppercase tracking-wider">Proficiency Level Gap</h3>
          </div>
          <div className="space-y-2">
            {gapData?.weakSkills?.map((skill, i) => (
              <div key={i} className="p-2.5 rounded-lg bg-amber-950/20 border border-amber-500/20 text-xs font-medium text-amber-200">
                <span>{skill}</span>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* Prioritized AI Action Plan */}
      <div className="glass-panel p-6 rounded-2xl border border-slate-800 space-y-4">
        <div className="flex items-center justify-between">
          <div className="flex items-center space-x-2">
            <Sparkles className="w-5 h-5 text-brand-400" />
            <h3 className="text-base font-bold text-white">Recommended AI Learning Sequence to Reach 95%+ Readiness</h3>
          </div>
          <button
            onClick={() => setCurrentTab('roadmap')}
            className="flex items-center space-x-1 text-xs font-bold text-brand-400 hover:text-brand-300"
          >
            <span>Open Interactive Roadmap</span>
            <ArrowRight className="w-4 h-4" />
          </button>
        </div>

        <div className="space-y-3">
          {[
            { priority: "Priority 1", topic: "Docker Containerization for Spring Boot Microservices", hours: "12 hours", why: "Essential prerequisite for cloud deployment and modern microservice architectures." },
            { priority: "Priority 2", topic: "React State Management & REST API Integration", hours: "16 hours", why: "Elevates frontend proficiency from beginner to intermediate for full-stack balance." },
            { priority: "Priority 3", topic: "Spring Security JWT Token Hardening & Refresh Flows", hours: "8 hours", why: "Frequently asked security topic in mid-level backend engineering interviews." }
          ].map((item, idx) => (
            <div key={idx} className="p-4 rounded-xl bg-slate-900/80 border border-slate-800 flex flex-col sm:flex-row sm:items-center justify-between gap-4">
              <div className="space-y-1">
                <div className="flex items-center space-x-2">
                  <span className="px-2 py-0.5 rounded text-[10px] font-bold bg-brand-500/20 text-brand-300">
                    {item.priority}
                  </span>
                  <h4 className="text-xs font-bold text-slate-100">{item.topic}</h4>
                </div>
                <p className="text-[11px] text-slate-400">{item.why}</p>
              </div>

              <div className="flex items-center space-x-3 shrink-0">
                <span className="text-xs font-mono text-slate-400">⏱️ {item.hours}</span>
                <button
                  onClick={() => setCurrentTab('roadmap')}
                  className="px-3 py-1.5 text-xs font-semibold rounded-lg bg-slate-800 hover:bg-slate-700 text-slate-200 transition-colors"
                >
                  Start Module
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};
