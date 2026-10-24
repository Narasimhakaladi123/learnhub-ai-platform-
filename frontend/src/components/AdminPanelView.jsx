import React, { useState, useEffect } from 'react';
import { ShieldCheck, Users, TrendingUp, BookOpen, Award, CheckCircle } from 'lucide-react';
import { adminApi } from '../services/api';

export const AdminPanelView = () => {
  const [analytics, setAnalytics] = useState(null);
  const [students, setStudents] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [aData, sData] = await Promise.all([
          adminApi.getAnalytics(),
          adminApi.getStudents()
        ]);
        setAnalytics(aData);
        setStudents(sData);
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="glass-panel p-6 sm:p-8 rounded-2xl border border-slate-800 bg-gradient-to-r from-slate-900/90 via-slate-900 to-purple-950/30">
        <div className="space-y-2">
          <div className="inline-flex items-center space-x-1.5 px-2.5 py-0.5 rounded text-xs font-semibold bg-purple-500/20 text-purple-300 border border-purple-500/30">
            <ShieldCheck className="w-3.5 h-3.5" />
            <span>Administrator Control Center</span>
          </div>
          <h2 className="text-2xl font-black text-white">Platform Analytics & Student Cohorts</h2>
          <p className="text-xs text-slate-300 max-w-xl">
            Monitor institutional student placement readiness, active roadmap completion velocity, and skill gap metrics.
          </p>
        </div>
      </div>

      {/* Analytics KPI Grid */}
      <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
        {[
          { label: "Enrolled Students", value: analytics?.totalStudents || "1,420", sub: "+124 this month", icon: Users, color: "text-brand-400" },
          { label: "Active Roadmaps", value: analytics?.activeRoadmaps || "1,184", sub: "83% completion rate", icon: BookOpen, color: "text-indigo-400" },
          { label: "Assessments Passed", value: analytics?.completedAssessments || "3,490", sub: "Avg score: 81.2%", icon: Award, color: "text-emerald-400" },
          { label: "Cohort Readiness", value: `${analytics?.averageReadinessScore || 76.4}%`, sub: "Interview qualified", icon: TrendingUp, color: "text-purple-400" }
        ].map((stat, i) => {
          const Icon = stat.icon;
          return (
            <div key={i} className="glass-card p-5 rounded-xl border border-slate-800">
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

      {/* Student Roster Table */}
      <div className="glass-panel p-6 rounded-2xl border border-slate-800 space-y-4">
        <div className="flex items-center justify-between">
          <h3 className="text-base font-bold text-white flex items-center space-x-2">
            <Users className="w-4 h-4 text-brand-400" />
            <span>Registered Student Directory</span>
          </h3>
          <span className="text-xs text-slate-400">Total: {students.length} students</span>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs">
            <thead className="bg-slate-900/80 text-slate-400 uppercase text-[10px] font-bold border-b border-slate-800">
              <tr>
                <th className="p-3">Student</th>
                <th className="p-3">Target Career Track</th>
                <th className="p-3">Readiness Gauge</th>
                <th className="p-3">CGPA</th>
                <th className="p-3 text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-800/60">
              {students.map((st, idx) => (
                <tr key={idx} className="hover:bg-slate-900/40 transition-colors">
                  <td className="p-3 font-semibold text-white">
                    <p>{st.fullName}</p>
                    <p className="text-[11px] text-slate-500 font-normal">{st.email}</p>
                  </td>
                  <td className="p-3 text-slate-300 font-mono">{st.targetCareer}</td>
                  <td className="p-3">
                    <div className="flex items-center space-x-2">
                      <div className="w-20 bg-slate-800 h-2 rounded-full overflow-hidden">
                        <div 
                          className="bg-brand-500 h-full rounded-full" 
                          style={{ width: `${st.readinessPercentage || 75}%` }} 
                        />
                      </div>
                      <span className="font-bold text-slate-200">{st.readinessPercentage}%</span>
                    </div>
                  </td>
                  <td className="p-3 text-slate-300 font-mono">{st.cgpa || "8.5"}</td>
                  <td className="p-3 text-right">
                    <button className="px-2.5 py-1 rounded bg-slate-800 hover:bg-slate-700 text-slate-300 text-[11px]">
                      View Profile
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};
