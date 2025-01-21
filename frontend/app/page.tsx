"use client";
import { Link } from "@nextui-org/link";
import { button as buttonStyles } from "@nextui-org/theme";
import { useSession } from "next-auth/react";
import { FaBook } from "react-icons/fa6";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { useState } from "react";
import Confetti from "react-confetti-boom";

import { siteConfig } from "@/config/site";
import { GithubIcon } from "@/components/icons";
import DevPanel from "@/components/DevPanel";

const queryClient = new QueryClient();

export default function Home() {
  const { data: session } = useSession();
  const [showConfetti, setShowConfetti] = useState(false);

  const handleSmartOrderClick = () => {
    setShowConfetti(true);
    setTimeout(() => setShowConfetti(false), 2000);
  };

  return (
    <QueryClientProvider client={queryClient}>
      <section className="flex flex-col items-center justify-center gap-4 py-8 md:py-10">
        {showConfetti && (
          <Confetti
            colors={["#b008da", "#167af5", "#d63ef4", "#0eb4f5"]}
            effectCount={60}
            launchSpeed={1.3}
            particleCount={60}
            shapeSize={20}
          />
        )}
        <div className="inline-block max-w-xl text-center justify-center">
          {session?.user?.username && (
            <div className="text-3xl font-bold mb-4 animate-wiggle">
              Hello {session.user.username} 👋
            </div>
          )}
          <div className="space-y-4">
            <h1 className="text-5xl font-bold">
              Welcome to{" "}
              <span
                className="bg-clip-text text-transparent bg-gradient-to-r from-primary to-secondary cursor-pointer"
                role="button"
                tabIndex={0}
                onClick={handleSmartOrderClick}
                onKeyDown={(e) => {
                  if (e.key === "Enter" || e.key === " ") {
                    handleSmartOrderClick();
                  }
                }}
              >
                SmartOrder
              </span>
            </h1>
            <p className="text-2xl text-foreground-600">
              Manage and reorder your household items
            </p>
            <p className="text-foreground-500">
              Easy to use, open-source, and customizable
            </p>
          </div>
        </div>

        <div className="flex gap-3 flex-col mb-8 mt-8">
          <Link
            className={buttonStyles({
              color: "primary",
              radius: "full",
              variant: "shadow",
            })}
            href="/objects"
          >
            Get Started
          </Link>
          <div className="flex gap-3">
            <Link
              isExternal
              className={buttonStyles({ variant: "bordered", radius: "full" })}
              href={siteConfig.links.github}
            >
              <GithubIcon size={20} />
              GitHub
            </Link>
            <Link
              isExternal
              className={buttonStyles({ variant: "bordered", radius: "full" })}
              href={siteConfig.links.docs}
            >
              <FaBook className="text-default-500" size={20} />
              Docs
            </Link>
          </div>
        </div>

        <DevPanel />
      </section>
    </QueryClientProvider>
  );
}
