import React, { useState, useEffect } from 'react';
import { Map, CheckCircle2, Circle, Clock, Sparkles, BookOpen, ExternalLink } from 'lucide-react';
import { roadmapApi } from '../services/api';

export const RoadmapView = () => {
  const [roadmap, setRoadmap] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchRoadmap = async () => {
      try {
        const data = await roadmapApi.getActive();
        setRoadmap(data);
      } catch (err) {
        console.error("Roadmap fetch error:", err);
      } finally {
        setLoading(false);
      }
    };
    fetchRoadmap();
  }, []);

  const toggleStatus = async (item) => {
    const nextStatus = item.status === 'COMPLETED' ? 'NOT_STARTED' :
                       item.status === 'NOT_STARTED' ? 'IN_PROGRESS' : 'COMPLETED';

    try {
      await roadmapApi.updateItemStatus(item.id, nextStatus);
    } catch {
      // Local optimistic update
    }

    setRoadmap(prev => {
      if (!prev) return prev;
      const updatedItems = prev.items.map(it => it.id === item.id ? { ...it, status: nextStatus } : it);
      const completed = updatedItems.filter(i => i.status === 'COMPLETED').length;
      const progress = Math.round((completed / updatedItems.length) * 100);
      return { ...prev, items: updatedItems, progressPercentage: progress };
    });
  };

  if (loading) {
    return (
      <div className="py-20 text-center">
        <div className="w-10 h-10 border-4 border-brand-500 border-t-transparent rounded-full animate-spin mx-auto mb-4" />
        <p className="text-sm text-slate-400">Loading personalized career roadmap...</p>
      </div>
    );
  }

  const items = roadmap?.items || [];
  const completedCount = items.filter(i => i.status === 'COMPLETED').length;
  const progress = items.length ? Math.round((completedCount / items.length) * 100) : 65;

  return (
    <div className="space-y-6">
      {/* Header Banner */}
      <div className="glass-panel p-6 sm:p-8 rounded-2xl border border-slate-800 bg-gradient-to-r from-slate-900/90 via-slate-900 to-indigo-950/30">
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-6">
          <div className="space-y-2">
            <div className="inline-flex items-center space-x-1.5 px-2.5 py-0.5 rounded text-xs font-semibold bg-indigo-500/20 text-indigo-300 border border-indigo-500/30">
              <Map className="w-3.5 h-3.5" />
              <span>Tailored Milestone Tree</span>
            </div>
            <h2 className="text-2xl font-black text-white">
              Target Track: {roadmap?.careerName || "Full Stack Java Developer"}
            </h2>
            <p className="text-xs text-slate-300 max-w-xl">
              Complete each structured milestone. Click any milestone status tag to mark as In-Progress or Completed.
            </p>
          </div>

          {/* Progress Bar Widget */}
          <div className="bg-slate-950/80 p-5 rounded-2xl border border-slate-800 w-full md:w-64 space-y-2">
            <div className="flex items-center justify-between text-xs font-bold">
              <span className="text-slate-300">Milestone Progress</span>
              <span className="text-brand-400 font-black">{progress}%</span>
            </div>
            <div className="w-full bg-slate-800 h-2.5 rounded-full overflow-hidden">
              <div 
                className="bg-gradient-to-r from-brand-500 to-indigo-500 h-full rounded-full transition-all duration-500" 
                style={{ width: `${progress}%` }}
              />
            </div>
            <p className="text-[11px] text-slate-500 text-right">{completedCount} of {items.length} milestones complete</p>
          </div>
        </div>
      </div>

      {/* Timeline Steps */}
      <div className="space-y-4">
        {items.map((item, index) => {
          const isDone = item.status === 'COMPLETED';
          const isInProgress = item.status === 'IN_PROGRESS';

          return (
            <div 
              key={item.id || index}
              className={`glass-panel p-5 rounded-2xl border transition-all ${
                isDone ? 'border-emerald-500/40 bg-slate-950/80' :
                isInProgress ? 'border-brand-500/60 bg-slate-900/90 shadow-glow' :
                'border-slate-800 bg-slate-950/40'
              }`}
            >
              <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
                <div className="flex items-start space-x-4">
                  {/* Step Number Circle */}
                  <div className={`w-8 h-8 rounded-full flex items-center justify-center font-bold text-xs shrink-0 ${
                    isDone ? 'bg-emerald-500/20 text-emerald-400 border border-emerald-500/40' :
                    isInProgress ? 'bg-brand-500/20 text-brand-300 border border-brand-500/40 animate-pulse' :
                    'bg-slate-800 text-slate-400'
                  }`}>
                    {isDone ? <CheckCircle2 className="w-4 h-4" /> : index + 1}
                  </div>

                  <div className="space-y-1">
                    <div className="flex items-center space-x-2">
                      <span className="text-[10px] font-bold px-1.5 py-0.2 rounded bg-slate-800 text-slate-400 font-mono">
                        {item.stage || 'STAGE'}
                      </span>
                      <h3 className="text-sm font-bold text-white">{item.title}</h3>
                    </div>
                    <p className="text-xs text-slate-300">{item.description}</p>
                    <div className="flex items-center space-x-3 text-[11px] text-slate-500 pt-1 font-mono">
                      <span>⏱️ Est: {item.estimatedHours || 15} hrs</span>
                    </div>
                  </div>
                </div>

                {/* Status Toggle Button */}
                <div className="flex items-center space-x-2 shrink-0">
                  <button
                    onClick={() => toggleStatus(item)}
                    className={`px-3 py-1.5 rounded-lg text-xs font-bold transition-all ${
                      isDone ? 'bg-emerald-500/20 text-emerald-300 border border-emerald-500/40 hover:bg-emerald-500/30' :
                      isInProgress ? 'bg-brand-500/20 text-brand-300 border border-brand-500/40 hover:bg-brand-500/30' :
                      'bg-slate-800 text-slate-400 hover:text-slate-200 hover:bg-slate-700'
                    }`}
                  >
                    {item.status.replace('_', ' ')} ↻
                  </button>
                </div>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
};
