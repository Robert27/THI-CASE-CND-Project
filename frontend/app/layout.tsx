import "@/styles/globals.css";
import { Metadata, Viewport } from "next";
import { Link } from "@heroui/link";
import clsx from "clsx";

import { Providers } from "./providers";

import { siteConfig } from "@/config/site";
import { fontSans } from "@/config/fonts";
import { Navbar } from "@/components/navbar";

export const metadata: Metadata = {
  title: {
    default: siteConfig.name,
    template: `%s - ${siteConfig.name}`,
  },
  description: siteConfig.description,
  icons: {
    icon: "/favicon.ico",
  },
};

export const viewport: Viewport = {
  themeColor: [
    { media: "(prefers-color-scheme: light)", color: "white" },
    { media: "(prefers-color-scheme: dark)", color: "black" },
  ],
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html suppressHydrationWarning lang="en">
      <head />
      <body
        className={clsx(
          "min-h-screen font-sans antialiased overflow-x-hidden",
          fontSans.variable
        )}
      >
        <Providers themeProps={{ attribute: "class", defaultTheme: "dark" }}>
          <div className="relative min-h-screen">
            {/* Gradient Mesh Background */}
            <div className="fixed inset-0 -z-30">
              <div className="absolute inset-0">
                <div className="absolute top-[5%] left-[10%] w-[45%] h-[40%] bg-purple-500 rounded-full mix-blend-normal filter blur-2xl opacity-40 animate-blob" />
                <div className="absolute top-[25%] right-[15%] w-[40%] h-[35%] bg-blue-500 rounded-full mix-blend-normal filter blur-3xl opacity-30 animate-blob-reverse animation-delay-2000" />
                <div className="absolute bottom-[20%] left-[20%] w-[42%] h-[38%] bg-pink-500 rounded-full mix-blend-normal filter blur-xl opacity-40 animate-blob animation-delay-4000" />
                <div className="absolute bottom-[15%] right-[15%] w-[38%] h-[40%] bg-purple-400 rounded-full mix-blend-normal filter blur-2xl opacity-30 animate-blob-reverse animation-delay-3000" />
                <div className="absolute top-[45%] left-[5%] w-[35%] h-[35%] bg-blue-400 rounded-full mix-blend-normal filter blur-xl opacity-40 animate-blob animation-delay-1000" />
              </div>
            </div>
            {/* Blur Overlay */}
            <div className="fixed inset-0 bg-background/45 backdrop-blur-[85px] -z-20" />
            {/* Dot Grid Overlay */}
            <div className="fixed inset-0 dot-grid opacity-70 -z-10 pointer-events-none" />
            <div className="relative z-0">
              <Navbar />
              <main>{children}</main>
              <footer className="w-full flex items-center justify-center py-3">
                <span className="text-sm text-center text-default-500">
                  © {new Date().getFullYear()} - {siteConfig.name}
                  <br />
                  Robert Eggl - Leonie Rößler - Andreas Ziegltrum
                </span>
                <Link
                  isExternal
                  className="flex items-center gap-1 text-current"
                  href="https://nextui-docs-v2.vercel.app?utm_source=next-app-template"
                  title="nextui.org homepage"
                />
              </footer>
            </div>
          </div>
        </Providers>
      </body>
    </html>
  );
}
