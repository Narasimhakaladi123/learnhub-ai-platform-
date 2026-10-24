import React, { useState } from 'react';
import { FileText, UploadCloud, CheckCircle2, AlertTriangle, ArrowRight, Sparkles, Award } from 'lucide-react';
import { resumeApi } from '../services/api';

export const ResumeATSView = () => {
  const [analyzing, setAnalyzing] = useState(false);
  const [report, setReport] = useState(null);
  const [targetRole, setTargetRole] = useState("Full Stack Java Developer");

  const runAnalysis = async () => {
    setAnalyzing(true);
    try {
      const formData = new FormData();
      formData.append('targetRole', targetRole);
      const res = await resumeApi.analyze(formData);
      setReport(res);
    } catch (err) {
      console.error(err);
    } finally {
      setAnalyzing(false);
    }
  };

  return (
    <div className="max-w-4xl mx-auto space-y-6">
      {/* Header */}
      <div className="glass-panel p-6 sm:p-8 rounded-2xl border border-slate-800 bg-gradient-to-r from-slate-900/90 via-slate-900 to-emerald-950/30">
        <div className="space-y-2">
          <div className="inline-flex items-center space-x-1.5 px-2.5 py-0.5 rounded text-xs font-semibold bg-emerald-500/20 text-emerald-300 border border-emerald-500/30">
            <FileText className="w-3.5 h-3.5" />
            <span>Applicant Tracking System (ATS) Diagnostic</span>
          </div>
          <h2 className="text-2xl font-black text-white">ATS Resume Keyword & Quality Analyzer</h2>
          <p className="text-xs text-slate-300 max-w-xl">
            Simulate recruiter ATS scanners. Check keyword density, identify missing technical skills, and verify resume structure.
          </p>
        </div>
      </div>

      {/* Upload Dropzone */}
      <div className="glass-panel p-8 rounded-2xl border-2 border-dashed border-slate-800 hover:border-brand-500/50 transition-colors text-center space-y-4">
        <div className="w-14 h-14 rounded-2xl bg-brand-500/10 text-brand-400 flex items-center justify-center mx-auto border border-brand-500/20">
          <UploadCloud className="w-7 h-7" />
        </div>

        <div className="space-y-1">
          <p className="text-sm font-bold text-white">Upload your PDF or Word resume</p>
          <p className="text-xs text-slate-400">Standard single-column PDFs are recommended for highest ATS accuracy</p>
        </div>

        <div className="flex flex-wrap items-center justify-center gap-3 pt-2">
          <button
            onClick={runAnalysis}
            disabled={analyzing}
            className="px-6 py-2.5 rounded-xl bg-gradient-to-r from-emerald-600 to-teal-600 hover:from-emerald-500 hover:to-teal-500 text-white font-bold text-xs shadow-glow-emerald transition-all"
          >
            {analyzing ? 'Scanning Keywords & Extracting Skills...' : '⚡ Scan Sample Engineering Resume'}
          </button>
        </div>
      </div>

      {/* Analysis Report */}
      {report && (
        <div className="space-y-6 animate-fade-in">
          {/* Top Score Banner */}
          <div className="glass-panel p-6 rounded-2xl border border-slate-800 flex flex-col sm:flex-row sm:items-center justify-between gap-6">
            <div className="space-y-1">
              <span className="text-[10px] font-bold px-2 py-0.5 rounded bg-slate-800 text-slate-300 font-mono">
                CANDIDATE: {report.candidateName || "Alex Mercer"}
              </span>
              <h3 className="text-xl font-bold text-white">ATS Compatibility Match</h3>
              <p className="text-xs text-slate-400">{report.formattingFeedback}</p>
            </div>

            <div className="flex items-center space-x-3 bg-slate-950/80 p-4 rounded-xl border border-slate-800 shrink-0">
              <div className="text-center">
                <span className="text-3xl font-black text-emerald-400">{report.atsScore} / 100</span>
                <p className="text-[10px] font-semibold text-slate-400 uppercase mt-0.5">ATS Rating: Excellent</p>
              </div>
            </div>
          </div>

          {/* Keywords Breakdown Grid */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {/* Detected Skills */}
            <div className="glass-panel p-5 rounded-2xl border border-emerald-500/30 space-y-3">
              <div className="flex items-center space-x-2 text-emerald-400">
                <CheckCircle2 className="w-4 h-4" />
                <h4 className="font-bold text-xs uppercase tracking-wider">Identified Technical Keywords ({report.identifiedSkills?.length})</h4>
              </div>
              <div className="flex flex-wrap gap-1.5">
                {report.identifiedSkills?.map((kw, i) => (
                  <span key={i} className="px-2.5 py-1 rounded bg-emerald-950/30 border border-emerald-500/30 text-emerald-300 text-xs font-mono font-medium">
                    ✓ {kw}
                  </span>
                ))}
              </div>
            </div>

            {/* Missing Keywords */}
            <div className="glass-panel p-5 rounded-2xl border border-red-500/30 space-y-3">
              <div className="flex items-center space-x-2 text-red-400">
                <AlertTriangle className="w-4 h-4" />
                <h4 className="font-bold text-xs uppercase tracking-wider">Recommended Missing Keywords ({report.missingKeywords?.length})</h4>
              </div>
              <div className="flex flex-wrap gap-1.5">
                {report.missingKeywords?.map((kw, i) => (
                  <span key={i} className="px-2.5 py-1 rounded bg-red-950/30 border border-red-500/30 text-red-300 text-xs font-mono font-medium">
                    + {kw}
                  </span>
                ))}
              </div>
            </div>
          </div>

          {/* Actionable Improvement Tips */}
          <div className="glass-panel p-6 rounded-2xl border border-slate-800 space-y-3">
            <h4 className="text-xs font-bold text-slate-200 uppercase tracking-wider flex items-center space-x-1.5">
              <Sparkles className="w-3.5 h-3.5 text-brand-400" />
              <span>Actionable Resume Optimizations</span>
            </h4>
            <div className="space-y-2">
              {report.suggestedImprovements?.map((tip, i) => (
                <div key={i} className="p-3 rounded-xl bg-slate-900/60 border border-slate-800/80 text-xs text-slate-300 flex items-start space-x-2">
                  <span className="text-brand-400 font-bold shrink-0">{i + 1}.</span>
                  <p>{tip}</p>
                </div>
              ))}
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
