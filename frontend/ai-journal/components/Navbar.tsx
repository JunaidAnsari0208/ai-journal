"use client";

import { BookOpen } from "lucide-react";
import Link from "next/link";
import { useEffect, useState } from "react";

export default function Navbar() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [hasMounted, setHasMounted] = useState(false);

  useEffect(() => {
    setHasMounted(true);
    const token = localStorage.getItem("accessToken");
    if (token) {
      setIsLoggedIn(true);
    }
  }, []);

  const handleLogout = () => {
    localStorage.removeItem("accessToken");
    setIsLoggedIn(false);
    window.location.href = "/login";
  };

  if (!hasMounted) return null;

  return (
    <nav className="border-b border-gray-800 bg-gray-950 px-4 py-4 flex items-center justify-between">
      <Link href="/" className="flex items-center space-x-2">
        <BookOpen className="h-6 w-6 text-emerald-400" />
        <span className="text-lg font-bold text-white">AI Journal</span>
      </Link>
      <div className="flex items-center space-x-3 text-sm">
        {isLoggedIn ? (
          <>
            <Link
              href="/dashboard"
              className="text-gray-300 hover:text-white transition-colors"
            >
              Dashboard
            </Link>
            <button
              onClick={handleLogout}
              className="text-gray-300 hover:text-white transition-colors border border-gray-700 px-4 py-2 rounded-md"
            >
              Logout
            </button>
          </>
        ) : (
          <>
            <Link href="/login">
              <button className="text-gray-300 hover:text-white transition-colors">
                Sign In
              </button>
            </Link>
            <Link href="/register">
              <button className="bg-emerald-600 hover:bg-emerald-700 text-white px-4 py-2 rounded-md transition-colors">
                Get Started
              </button>
            </Link>
          </>
        )}
      </div>
    </nav>
  );
}
