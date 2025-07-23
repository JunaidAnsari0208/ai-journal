"use client";

import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Textarea } from "@/components/ui/textarea";
import api from "@/lib/api";
import { PenTool } from "lucide-react";
import { useEffect, useState } from "react";
import { toast } from "sonner";

interface JournalEntry {
  journalId: number;
  userId: number;
  content: string;
  createdAt: string;
}

export default function DashboardPage() {
  const [journals, setJournals] = useState<JournalEntry[]>([]);
  const [newEntry, setNewEntry] = useState("");
  const [isWriting, setIsWriting] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [reportMessage, setReportMessage] = useState<string | null>(null);

  const fetchJournals = async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await api.get("/api/journals/my-journals");
      setJournals(res.data);
    } catch (err) {
      setError("Failed to fetch journals.");
    } finally {
      setLoading(false);
    }
  };

  const handleSaveEntry = async () => {
    if (!newEntry.trim()) return;
    try {
      await api.post("/api/journals", { content: newEntry });
      setNewEntry("");
      setIsWriting(false);
      await fetchJournals();
    } catch (err) {
      setError("Failed to save entry.");
    }
  };

  const handleGenerateReport = async () => {
    try {
      const res = await api.post("/api/journals/user/generate-report");
      setReportMessage(res.data);
    } catch (err: any) {
      setReportMessage(
        "Failed to generate report: " +
          (err.response?.data?.message || err.message)
      );
    }
  };

  useEffect(() => {
    const hasSeenRateLimitNotice = localStorage.getItem("rateLimitNoticeShown");

    if (!hasSeenRateLimitNotice) {
      toast.info(
        "Note: This is a personal project with a 200 API calls/day limit. If the app doesn’t work, please try again tomorrow.",
        { duration: 7000 }
      );
      localStorage.setItem("rateLimitNoticeShown", "true");
    }
  }, []);

  useEffect(() => {
    if (reportMessage) {
      toast.success(reportMessage);
      setReportMessage(null);
    }
  }, [reportMessage]);

  useEffect(() => {
    const token = localStorage.getItem("accessToken");
    if (!token) window.location.href = "/login";
    else fetchJournals();
  }, []);

  return (
    <div className="min-h-screen bg-black text-white p-6 space-y-8">
      <h1 className="text-3xl font-bold">Dashboard</h1>

      <Card className="bg-gray-900 border border-gray-800">
        <CardHeader>
          <CardTitle>Quick Write</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <Textarea
            value={newEntry}
            onChange={(e) => {
              setNewEntry(e.target.value);
              setIsWriting(e.target.value.length > 0);
            }}
            placeholder="What's on your mind today?"
            className="min-h-[120px] bg-gray-800 border-gray-700 text-white"
          />
          <div className="flex justify-between items-center text-sm text-gray-400">
            <span>
              {newEntry.length} chars •{" "}
              {newEntry.split(" ").filter(Boolean).length} words
            </span>
            <div className="flex gap-2">
              {isWriting && (
                <Button
                  variant="outline"
                  onClick={() => {
                    setNewEntry("");
                    setIsWriting(false);
                  }}
                  className="border-gray-700 text-gray-300 hover:bg-gray-800"
                >
                  Clear
                </Button>
              )}
              <Button
                onClick={handleSaveEntry}
                disabled={!newEntry.trim()}
                className="bg-emerald-600 hover:bg-emerald-700"
              >
                Save Entry
              </Button>
            </div>
          </div>
        </CardContent>
      </Card>

      <Card className="bg-gray-900 border border-gray-800">
        <CardHeader>
          <CardTitle>My Journal Entries</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          {loading && <p className="text-gray-400">Loading entries...</p>}
          {error && <p className="text-red-500">{error}</p>}
          {!loading && journals.length === 0 && (
            <p className="text-gray-400">No journal entries yet.</p>
          )}
          {journals.map((entry) => (
            <div
              key={entry.journalId}
              className="bg-gray-800/60 p-4 rounded-md space-y-2 hover:bg-gray-800 transition-colors"
            >
              <div className="flex items-center justify-between text-sm text-gray-400">
                <div className="flex items-center gap-2">
                  <PenTool className="h-4 w-4 text-emerald-400" />
                  <span>{new Date(entry.createdAt).toLocaleString()}</span>
                </div>
              </div>
              <p className="text-white whitespace-pre-wrap">{entry.content}</p>
            </div>
          ))}
        </CardContent>
      </Card>

      <div className="space-y-2">
        <Button
          onClick={handleGenerateReport}
          className="bg-purple-600 hover:bg-purple-700"
        >
          Generate Report
        </Button>
      </div>
    </div>
  );
}
