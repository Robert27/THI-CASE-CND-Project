import React from "react";
import { Alert } from "@nextui-org/react";

import { Logo } from "@/components/icons";

interface AuthLayoutProps {
  children: React.ReactNode;
  title: string;
  subtitle: string;
  errorMessage?: string;
  isError?: boolean;
}

export const AuthLayout: React.FC<AuthLayoutProps> = ({
  children,
  title,
  subtitle,
  errorMessage,
  isError = true,
}) => {
  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-b from-background to-default-100">
      <div className="w-full max-w-md p-8 space-y-8 bg-background rounded-xl shadow-lg m-4">
        <div className="flex flex-col items-center space-y-6">
          <Logo
            className="transform hover:scale-105 transition-transform"
            size={120}
          />
          <div className="text-center space-y-3">
            <h1 className="text-3xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-primary to-secondary">
              {title}
            </h1>
            <p className="text-foreground-500">{subtitle}</p>
          </div>
        </div>

        {errorMessage && (
          <Alert
            className="animate-shake my-4"
            color={isError ? "danger" : "success"}
            description={errorMessage}
            title={isError ? "Error" : "Success"}
          />
        )}

        {children}
      </div>
    </div>
  );
};
