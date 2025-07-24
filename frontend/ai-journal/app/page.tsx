import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { ArrowRight, Mail } from "lucide-react";
import Link from "next/link";

export default function HomePage() {
  return (
    <div className="min-h-screen bg-gray-950 text-gray-100">
      {/* Hero */}
      <section className="py-20 px-4 text-center">
        <Badge className="mb-4 bg-emerald-900/30 text-emerald-300 border-emerald-700">
          Simple Journaling
        </Badge>
        <h1 className="text-4xl font-bold mb-4 text-white">
          Your thoughts, simply organized
        </h1>
        <p className="text-gray-400 mb-6">
          Write about your day and get weekly summaries — for your eyes only.
        </p>
        <Link href="/register">
          <Button className="bg-emerald-600 hover:bg-emerald-700 text-white px-6 py-2">
            Start Journaling <ArrowRight className="ml-2 h-4 w-4" />
          </Button>
        </Link>
        <p className="text-sm text-gray-500 mt-4">
          No spam. Your data stays private.
        </p>
      </section>

      {/* Weekly Report */}
      <section className="py-16 px-4">
        <div className="max-w-xl mx-auto text-center">
          <Badge className="mb-4 bg-purple-900/30 text-purple-300 border-purple-700">
            <Mail className="w-3 h-3 mr-1" /> Weekly Reports
          </Badge>
          <h2 className="text-2xl font-bold mb-4 text-white">
            Your weekly summary, delivered
          </h2>
          <p className="text-gray-400 mb-6">
            Every week, you&apos;ll receive a short email summarizing your mood
            and progress.
          </p>
          <Button
            className="bg-purple-600 hover:bg-purple-700 text-white"
            disabled
          >
            See Sample Report
          </Button>
        </div>
      </section>

      {/* CTA */}
      <section className="py-16 px-4 text-center bg-gray-900/30">
        <h2 className="text-2xl font-bold mb-4 text-white">Ready to start?</h2>
        <p className="text-gray-400 mb-6">
          Begin your personal journaling journey today.
        </p>
        <Link href="/register">
          <Button className="bg-emerald-600 hover:bg-emerald-700 text-white px-6 py-2">
            Get Started <ArrowRight className="ml-2 h-4 w-4" />
          </Button>
        </Link>
      </section>

      {/* Footer */}
      <footer className="border-t border-gray-800 bg-gray-950 py-8 text-center text-sm text-gray-400">
        <p>&copy; 2024 AI Journal. All rights reserved.</p>
      </footer>
    </div>
  );
}
