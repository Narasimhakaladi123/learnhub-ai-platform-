import React, { useState, useEffect } from 'react';
import { Compass, Search, DollarSign, TrendingUp, CheckCircle, ArrowRight, Sparkles } from 'lucide-react';
import { careerApi } from '../services/api';

export const CareerTracksView = ({ setCurrentTab }) => {
  const [careers, setCareers] = useState([]);
  const [filterCategory, setFilterCategory] = useState('ALL');
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedCareer, setSelectedCareer] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchCareers = async () => {
      try {
        const data = await careerApi.getAll();
        setCareers(data);
        if (data && data.length > 0) setSelectedCareer(data[0]);
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    fetchCareers();
  }, []);

  const filteredCareers = careers.filter(c => {
    const matchesCategory = filterCategory === 'ALL' || c.category === filterCategory;
    const matchesSearch = c.name.toLowerCase().includes(searchQuery.toLowerCase()) || 
                          c.description.toLowerCase().includes(searchQuery.toLowerCase());
    return matchesCategory && matchesSearch;
  });

  return (
    <div className="space-y-6">
      {/* Header & Controls */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h2 className="text-2xl font-black text-white flex items-center space-x-2">
            <Compass className="w-6 h-6 text-brand-400" />
            <span>High-Growth Engineering Career Tracks</span>
          </h2>
          <p className="text-xs text-slate-400 mt-1">Curated industry pathways benchmarked with real-world tech stack requirements</p>
        </div>

        {/* Search Input */}
        <div className="relative w-full sm:w-64">
          <Search className="w-4 h-4 text-slate-500 absolute left-3 top-1/2 -translate-y-1/2" />
          <input
            type="text"
            placeholder="Search tracks (e.g. Java, Cloud)..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="w-full pl-9 pr-3 py-2 bg-slate-900 border border-slate-800 rounded-xl text-xs text-slate-200 placeholder-slate-500 focus:outline-none focus:border-brand-500"
          />
        </div>
      </div>

      {/* Category Pills */}
      <div className="flex flex-wrap gap-2">
        {['ALL', 'SOFTWARE_DEVELOPMENT', 'CLOUD_COMPUTING', 'DATA_AI'].map(cat => (
          <button
            key={cat}
            onClick={() => setFilterCategory(cat)}
            className={`px-3 py-1.5 rounded-lg text-xs font-semibold transition-all ${
              filterCategory === cat 
                ? 'bg-brand-600 text-white shadow-glow' 
                : 'bg-slate-900 text-slate-400 hover:text-slate-200 border border-slate-800'
            }`}
          >
            {cat.replace('_', ' ')}
          </button>
        ))}
      </div>

      {/* Grid of Career Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {filteredCareers.map(career => {
          const isSelected = selectedCareer?.id === career.id;
          return (
            <div
              key={career.id}
              onClick={() => setSelectedCareer(career)}
              className={`glass-panel p-6 rounded-2xl border transition-all cursor-pointer flex flex-col justify-between ${
                isSelected 
                  ? 'border-brand-500/80 shadow-glow bg-slate-900/90' 
                  : 'border-slate-800 hover:border-slate-700 bg-slate-950/60'
              }`}
            >
              <div className="space-y-3">
                <div className="flex items-center justify-between">
                  <span className="px-2 py-0.5 rounded text-[10px] font-bold bg-brand-500/10 text-brand-300 border border-brand-500/20">
                    {career.category?.replace('_', ' ')}
                  </span>
                  <div className="flex items-center space-x-1 text-emerald-400 text-xs font-semibold">
                    <TrendingUp className="w-3.5 h-3.5" />
                    <span>{career.jobOutlook}</span>
                  </div>
                </div>

                <h3 className="text-lg font-extrabold text-white">{career.name}</h3>
                <p className="text-xs text-slate-300 line-clamp-3 leading-relaxed">{career.description}</p>

                {/* Salary Benchmark */}
                <div className="flex items-center space-x-2 text-xs text-slate-200 bg-slate-900/80 p-2.5 rounded-lg border border-slate-800/80">
                  <DollarSign className="w-4 h-4 text-emerald-400" />
                  <span className="font-semibold">{career.averageSalaryRange}</span>
                  <span className="text-[10px] text-slate-500">(Avg. US Benchmark)</span>
                </div>

                {/* Key Skills */}
                <div className="space-y-1.5 pt-2">
                  <p className="text-[11px] font-semibold text-slate-400">Core Competencies:</p>
                  <div className="flex flex-wrap gap-1.5">
                    {career.careerSkills?.map((cs, i) => (
                      <span key={i} className="px-2 py-0.5 rounded bg-slate-800 text-[10px] font-mono text-slate-300">
                        {cs.skill?.name || cs.name}
                      </span>
                    ))}
                  </div>
                </div>
              </div>

              {/* Action Buttons */}
              <div className="mt-6 pt-4 border-t border-slate-800 flex items-center justify-between">
                <button
                  onClick={(e) => {
                    e.stopPropagation();
                    setCurrentTab('skill-gap');
                  }}
                  className="flex items-center space-x-1 text-xs font-bold text-brand-400 hover:text-brand-300"
                >
                  <span>Analyze Gap</span>
                  <ArrowRight className="w-3.5 h-3.5" />
                </button>

                <button
                  onClick={(e) => {
                    e.stopPropagation();
                    setCurrentTab('roadmap');
                  }}
                  className="px-3 py-1.5 text-xs font-semibold rounded-lg bg-brand-600 hover:bg-brand-500 text-white transition-colors"
                >
                  Generate Roadmap
                </button>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
};
