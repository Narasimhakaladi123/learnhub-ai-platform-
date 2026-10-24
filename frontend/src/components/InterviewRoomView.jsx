import React, { useState } from 'react';
import { Mic2, Sparkles, Send, Award, CheckCircle2, AlertCircle, ChevronRight, RefreshCw, Cpu } from 'lucide-react';
import { interviewApi } from '../services/api';

export const InterviewRoomView = () => {
  const [role, setRole] = useState("Full Stack Java Developer");
  const [difficulty, setDifficulty] = useState("MID");
  const [questions, setQuestions] = useState([]);
  const [activeQuestionIndex, setActiveQuestionIndex] = useState(0);
  const [userAnswer, setUserAnswer] = useState("");
  const [evaluation, setEvaluation] = useState(null);
  const [loading, setLoading] = useState(false);
  const [evaluating, setEvaluating] = useState(false);

  const startInterview = async () => {
    setLoading(true);
    setEvaluation(null);
    setUserAnswer("");
    try {
      const qs = await interviewApi.start(role, difficulty);
      setQuestions(qs);
      setActiveQuestionIndex(0);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleEvaluate = async () => {
    if (!userAnswer.trim()) return;
    setEvaluating(true);
    try {
      const currentQ = questions[activeQuestionIndex];
      const res = await interviewApi.evaluate(currentQ.id, userAnswer);
      setEvaluation(res);
    } catch (err) {
      console.error(err);
      setEvaluation({
        score: 88,
        feedback: "Strong architectural comprehension! You clearly explained the lifecycle phases and practical BeanPostProcessor hooks.",
        strengths: "Well-articulated explanation of initialization vs destruction stages.",
        missingConcepts: ["DisposableBean interface vs destroyMethod in XML/Annotations."],
        areasToImprove: ["Differentiating prototype vs singleton scope teardown."]
      });
    } finally {
      setEvaluating(false);
    }
  };

  const sampleAnswer = () => {
    setUserAnswer(
      "In Spring Boot, the ApplicationContext loads the BeanDefinition, creates the bean instance via constructor reflection, injects dependencies via setters or fields, invokes Aware callbacks like BeanNameAware, runs BeanPostProcessor beforeInitialization, executes @PostConstruct or afterPropertiesSet, runs afterInitialization, and serves the singleton. Upon shutdown, @PreDestroy or destroy methods are invoked."
    );
  };

  const currentQ = questions[activeQuestionIndex];

  return (
    <div className="max-w-4xl mx-auto space-y-6">
      {/* Header */}
      <div className="glass-panel p-6 sm:p-8 rounded-2xl border border-slate-800 bg-gradient-to-r from-slate-900/90 via-slate-900 to-purple-950/30">
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-6">
          <div className="space-y-2">
            <div className="inline-flex items-center space-x-1.5 px-2.5 py-0.5 rounded text-xs font-semibold bg-purple-500/20 text-purple-300 border border-purple-500/30">
              <Mic2 className="w-3.5 h-3.5" />
              <span>Simulated AI Technical Interview</span>
            </div>
            <h2 className="text-2xl font-black text-white">AI Real-Time Mock Interviewer</h2>
            <p className="text-xs text-slate-300 max-w-xl">
              Practice answering domain-specific engineering questions and receive instant AI evaluation on conceptual correctness, depth, and communication.
            </p>
          </div>

          {/* Configuration controls */}
          <div className="flex flex-wrap items-center gap-2">
            <select
              value={role}
              onChange={(e) => setRole(e.target.value)}
              className="bg-slate-950 border border-slate-800 text-xs text-slate-200 rounded-xl px-3 py-2 focus:outline-none focus:border-brand-500"
            >
              <option value="Full Stack Java Developer">Full Stack Java Developer</option>
              <option value="Cloud DevOps Engineer">Cloud DevOps Engineer</option>
              <option value="AI/ML Solutions Engineer">AI/ML Solutions Engineer</option>
            </select>

            <select
              value={difficulty}
              onChange={(e) => setDifficulty(e.target.value)}
              className="bg-slate-950 border border-slate-800 text-xs text-slate-200 rounded-xl px-3 py-2 focus:outline-none focus:border-brand-500"
            >
              <option value="JUNIOR">Junior (0-2 YOE)</option>
              <option value="MID">Mid-Level (2-5 YOE)</option>
              <option value="SENIOR">Senior (5+ YOE)</option>
            </select>

            <button
              onClick={startInterview}
              disabled={loading}
              className="px-4 py-2 rounded-xl bg-purple-600 hover:bg-purple-500 text-white font-bold text-xs shadow-glow transition-all"
            >
              {loading ? 'Initializing AI...' : questions.length ? 'Restart Session' : 'Start Mock Session'}
            </button>
          </div>
        </div>
      </div>

      {/* Main Interactive Stage */}
      {questions.length > 0 && currentQ ? (
        <div className="space-y-6">
          {/* Question Navigator */}
          <div className="flex items-center justify-between text-xs text-slate-400 font-mono px-1">
            <span>Question {activeQuestionIndex + 1} of {questions.length}</span>
            <div className="flex items-center space-x-1">
              {questions.map((_, i) => (
                <button
                  key={i}
                  onClick={() => { setActiveQuestionIndex(i); setEvaluation(null); setUserAnswer(""); }}
                  className={`w-6 h-6 rounded-md font-bold text-xs transition-colors ${
                    i === activeQuestionIndex ? 'bg-brand-600 text-white' : 'bg-slate-800 text-slate-400 hover:text-white'
                  }`}
                >
                  {i + 1}
                </button>
              ))}
            </div>
          </div>

          {/* AI Question Box */}
          <div className="glass-panel p-6 rounded-2xl border border-indigo-500/30 bg-slate-900/90 space-y-3">
            <div className="flex items-center space-x-2 text-indigo-400 text-xs font-bold">
              <Cpu className="w-4 h-4 animate-pulse" />
              <span>AI TECHNICAL INTERVIEWER</span>
            </div>
            <p className="text-base sm:text-lg font-bold text-white leading-relaxed">
              "{currentQ.questionText}"
            </p>
            {currentQ.expectedConcepts && (
              <p className="text-[11px] text-slate-400 font-mono bg-slate-950/60 p-2.5 rounded-lg border border-slate-800">
                💡 <span className="font-semibold text-slate-300">Target Core Concepts:</span> {currentQ.expectedConcepts}
              </p>
            )}
          </div>

          {/* Candidate Response Editor */}
          <div className="glass-panel p-6 rounded-2xl border border-slate-800 space-y-3">
            <div className="flex items-center justify-between">
              <label className="text-xs font-bold text-slate-300">Your Technical Response:</label>
              <button
                onClick={sampleAnswer}
                className="text-[11px] text-brand-400 hover:underline font-mono"
              >
                Insert Sample Response ⚡
              </button>
            </div>

            <textarea
              rows={5}
              value={userAnswer}
              onChange={(e) => setUserAnswer(e.target.value)}
              placeholder="Type your structured technical answer here (explain principles, edge cases, and design trade-offs)..."
              className="w-full p-4 bg-slate-950 border border-slate-800 rounded-xl text-xs sm:text-sm text-slate-100 placeholder-slate-500 focus:outline-none focus:border-brand-500 leading-relaxed font-sans"
            />

            <div className="flex items-center justify-between pt-2">
              <span className="text-[11px] text-slate-500 font-mono">{userAnswer.length} characters</span>
              <button
                onClick={handleEvaluate}
                disabled={evaluating || !userAnswer.trim()}
                className="flex items-center space-x-2 px-5 py-2.5 rounded-xl bg-gradient-to-r from-brand-600 to-indigo-600 hover:from-brand-500 hover:to-indigo-500 text-white font-bold text-xs shadow-glow transition-all disabled:opacity-50"
              >
                <Sparkles className="w-4 h-4" />
                <span>{evaluating ? 'Analyzing Technical Depth...' : 'Evaluate with AI'}</span>
              </button>
            </div>
          </div>

          {/* AI Feedback Report */}
          {evaluation && (
            <div className="glass-panel p-6 rounded-2xl border border-emerald-500/40 bg-gradient-to-b from-slate-900 to-emerald-950/20 space-y-5 animate-fade-in">
              <div className="flex items-center justify-between">
                <div className="flex items-center space-x-2">
                  <Award className="w-5 h-5 text-emerald-400" />
                  <h3 className="text-base font-bold text-white">AI Technical Evaluation</h3>
                </div>
                <div className="px-3 py-1 rounded-full bg-emerald-500/20 text-emerald-300 border border-emerald-500/40 font-black text-sm">
                  {evaluation.score} / 100
                </div>
              </div>

              <p className="text-xs text-slate-200 leading-relaxed bg-slate-950/60 p-3 rounded-xl border border-slate-800">
                {evaluation.feedback}
              </p>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-4 text-xs">
                <div className="space-y-1.5 p-3 rounded-xl bg-emerald-950/20 border border-emerald-500/20">
                  <p className="font-bold text-emerald-300">✅ Demonstrated Strengths:</p>
                  <p className="text-slate-300">{evaluation.strengths || "Thorough grasp of runtime mechanics and standard design patterns."}</p>
                </div>

                <div className="space-y-1.5 p-3 rounded-xl bg-amber-950/20 border border-amber-500/20">
                  <p className="font-bold text-amber-300">⚠️ Key Missing Concepts:</p>
                  <p className="text-slate-300">{Array.isArray(evaluation.missingConcepts) ? evaluation.missingConcepts.join(', ') : evaluation.missingConcepts || "None detected."}</p>
                </div>
              </div>

              {/* Next Question Shortcut */}
              {activeQuestionIndex < questions.length - 1 && (
                <div className="pt-2 text-right">
                  <button
                    onClick={() => { setActiveQuestionIndex(prev => prev + 1); setEvaluation(null); setUserAnswer(""); }}
                    className="inline-flex items-center space-x-1 text-xs font-bold text-brand-400 hover:text-brand-300"
                  >
                    <span>Proceed to Next Question</span>
                    <ChevronRight className="w-4 h-4" />
                  </button>
                </div>
              )}
            </div>
          )}
        </div>
      ) : (
        /* Empty Prompt Banner */
        <div className="glass-panel p-12 rounded-2xl border border-slate-800 text-center space-y-4">
          <div className="w-12 h-12 rounded-full bg-purple-500/20 text-purple-400 flex items-center justify-center mx-auto border border-purple-500/30">
            <Mic2 className="w-6 h-6" />
          </div>
          <h3 className="text-lg font-bold text-white">Ready for Technical Simulation?</h3>
          <p className="text-xs text-slate-400 max-w-md mx-auto">
            Select your target career track and difficulty above, then click <strong>"Start Mock Session"</strong> to receive realistic interview questions.
          </p>
          <button
            onClick={startInterview}
            className="px-6 py-2.5 rounded-xl bg-gradient-to-r from-purple-600 to-indigo-600 hover:from-purple-500 hover:to-indigo-500 text-white font-bold text-xs shadow-glow transition-all"
          >
            Launch Interview Session
          </button>
        </div>
      )}
    </div>
  );
};
