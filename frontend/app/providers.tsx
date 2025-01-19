"use client";

import type { ThemeProviderProps } from "next-themes";

import * as React from "react";
import { NextUIProvider } from "@nextui-org/system";
import { useRouter } from "next/navigation";
import { ThemeProvider as NextThemesProvider } from "next-themes";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { SessionProvider } from "next-auth/react";
import { Bounce, ToastContainer } from "react-toastify";
export interface ProvidersProps {
  children: React.ReactNode;
  themeProps?: ThemeProviderProps;
}

declare module "@react-types/shared" {
  interface RouterConfig {
    routerOptions: NonNullable<
      Parameters<ReturnType<typeof useRouter>["push"]>[1]
    >;
  }
}

export const httpHost = process.env.NEXT_PUBLIC_HTTP_HOST;

export function Providers({ children, themeProps }: ProvidersProps) {
  const router = useRouter();
  const queryClient = new QueryClient();

  return (
    <SessionProvider>
      <NextUIProvider navigate={router.push}>
        <NextThemesProvider {...themeProps}>
          <ToastContainer
            draggable
            pauseOnFocusLoss
            pauseOnHover
            autoClose={3000}
            closeOnClick={false}
            hideProgressBar={false}
            newestOnTop={false}
            position="bottom-right"
            rtl={false}
            theme="dark"
            transition={Bounce}
          />
          <QueryClientProvider client={queryClient}>
            {children}
          </QueryClientProvider>
        </NextThemesProvider>
      </NextUIProvider>
    </SessionProvider>
  );
}
