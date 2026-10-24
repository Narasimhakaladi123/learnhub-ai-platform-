import React, { useState } from 'react';
import { X, Sparkles, Send, Bot, User, Trash2 } from 'lucide-react';
import { chatApi } from '../services/api';

export const AICopilotModal = ({ isOpen, onClose }) => {
  const [messages, setMessages] = useState([
    {
      role: 'ASSISTANT',
      content: "Hello Alex! I'm your LearnHub Career & Technical Advisor. I can help analyze your skill gaps, explain difficult architecture concepts, review system design topics, or guide your learning roadmap. What are you working on today?"
    }
  ]);
  const [input, setInput] = useState('');
  const [loading, setLoading] = useState(false);

  if (!isOpen) return null;

  const handleSend = async (textToSend) => {
    const prompt = textToSend || input;
    if (!prompt.trim() || loading) return;

    const userMsg = { role: 'USER', content: prompt };
    setMessages(prev => [...prev, userMsg]);
    setInput('');
    setLoading(true);

    try {
      const reply = await chatApi.send(prompt);
      setMessages(prev => [...prev, reply]);
    } catch (err) {
      setMessages(prev => [
        ...prev,
        {
          role: 'ASSISTANT',
          content: "I'm currently running in local demonstration mode. For full production AI responses, you can configure an OpenAI API Key in your backend `application.yml` or run Ollama locally!"
        }
      ]);
    } finally {
      setLoading(false);
    }
  };

  const samplePrompts = [
    "What projects should I put on my resume for Java Backend?",
    "Explain the Spring Bean lifecycle in simple terms.",
    "How do I prepare for a Full Stack Java coding interview?"
  ];

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-950/80 backdrop-blur-sm animate-fade-in">
      <div className="glass-panel w-full max-w-2xl h-[600px] rounded-2xl border border-slate-800 flex flex-col overflow-hidden shadow-2xl bg-slate-950/95">
        {/* Header */}
        <div className="p-4 border-b border-slate-800 flex items-center justify-between bg-slate-900/60">
          <div className="flex items-center space-x-2.5">
            <div className="w-8 h-8 rounded-lg bg-gradient-to-tr from-brand-600 to-indigo-500 flex items-center justify-center text-white shadow-glow">
              <Sparkles className="w-4 h-4" />
            </div>
            <div>
              <h3 className="text-sm font-bold text-white flex items-center space-x-1.5">
                <span>LearnHub AI Career Copilot</span>
                <span className="text-[10px] px-1.5 py-0.2 rounded bg-brand-500/20 text-brand-300 font-mono">RAG Powered</span>
              </h3>
              <p className="text-[11px] text-slate-400">Contextual guidance linked to your profile and target tracks</p>
            </div>
          </div>

          <div className="flex items-center space-x-2">
            <button
              onClick={() => setMessages([{ role: 'ASSISTANT', content: 'Chat history cleared. How can I help you today?' }])}
              title="Clear chat"
              className="p-1.5 text-slate-400 hover:text-slate-200 hover:bg-slate-800 rounded-lg transition-colors"
            >
              <Trash2 className="w-4 h-4" />
            </button>
            <button
              onClick={onClose}
              className="p-1.5 text-slate-400 hover:text-white hover:bg-slate-800 rounded-lg transition-colors"
            >
              <X className="w-4 h-4" />
            </button>
          </div>
        </div>

        {/* Messages Scroll Area */}
        <div className="flex-1 overflow-y-auto p-4 space-y-4">
          {messages.map((m, i) => {
            const isBot = m.role === 'ASSISTANT';
            return (
              <div key={i} className={`flex items-start space-x-2.5 ${isBot ? '' : 'flex-row-reverse space-x-reverse'}`}>
                <div className={`w-7 h-7 rounded-lg flex items-center justify-center shrink-0 ${
                  isBot ? 'bg-brand-600 text-white' : 'bg-slate-800 text-slate-300'
                }`}>
                  {isBot ? <Bot className="w-4 h-4" /> : <User className="w-4 h-4" />}
                </div>

                <div className={`p-3.5 rounded-2xl max-w-[85%] text-xs leading-relaxed ${
                  isBot 
                    ? 'bg-slate-900 border border-slate-800 text-slate-200' 
                    : 'bg-brand-600 text-white shadow-glow'
                }`}>
                  <p className="whitespace-pre-line">{m.content}</p>
                </div>
              </div>
            );
          })}

          {loading && (
            <div className="flex items-center space-x-2 text-xs text-brand-400 p-2">
              <Sparkles className="w-4 h-4 animate-spin" />
              <span>Synthesizing RAG career knowledge...</span>
            </div>
          )}
        </div>

        {/* Prompt Chips */}
        <div className="px-4 py-2 border-t border-slate-800/60 bg-slate-900/30 flex items-center space-x-2 overflow-x-auto no-scrollbar">
          {samplePrompts.map((p, idx) => (
            <button
              key={idx}
              onClick={() => handleSend(p)}
              className="px-2.5 py-1 text-[11px] rounded-full bg-slate-900 hover:bg-slate-800 text-slate-300 border border-slate-800 whitespace-nowrap transition-colors"
            >
              {p}
            </button>
          ))}
        </div>

        {/* Input Bar */}
        <form
          onSubmit={(e) => { e.preventDefault(); handleSend(); }}
          className="p-3 border-t border-slate-800 bg-slate-900/60 flex items-center space-x-2"
        >
          <input
            type="text"
            value={input}
            onChange={(e) => setInput(e.target.value)}
            placeholder="Ask about roadmaps, interview questions, or system design..."
            className="flex-1 bg-slate-950 border border-slate-800 rounded-xl px-3.5 py-2.5 text-xs text-slate-200 placeholder-slate-500 focus:outline-none focus:border-brand-500 font-sans"
          />
          <button
            type="submit"
            disabled={loading || !input.trim()}
            className="p-2.5 rounded-xl bg-brand-600 hover:bg-brand-500 disabled:opacity-50 text-white shadow-glow transition-all"
          >
            <Send className="w-4 h-4" />
          </button>
        </form>
      </div>
    </div>
  );
};
