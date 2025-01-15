// filepath: /Users/roberteggl/Developer/GitHub/THI-CASE-CND-Projekt/frontend/app/register/page.tsx
"use client";

import React, { useState } from "react";
import Link from "next/link";
import { Alert, Button, Input } from "@nextui-org/react";
import { LuChevronRight } from "react-icons/lu";
import { useMutation } from "@tanstack/react-query";
import { toast } from "react-toastify";

import { Logo } from "@/components/icons";

const Register: React.FC = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  const mutation = useMutation({
    mutationFn: async () => {
      const res = await fetch("http://localhost:4000/rest/user/user", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ username, password }),
      });

      if (!res.ok) {
        const errorData = await res.json();

        throw new Error(errorData.message || "Registration failed");
      }
    },
    onSuccess: (data) => {
      console.log("User registered successfully", data);
      toast.success("Registration successful");
      window.location.href = "/login";
    },
    onError: (error) => {
      // Handle error
      console.error("Error registering user", error);
    },
  });

  const formSubmitted = (event: { preventDefault: () => void }) => {
    event.preventDefault();
    setErrorMessage("");
    mutation.mutate();
  };

  return (
    <div className="flex items-center justify-center min-h-screen">
      <div className="w-full max-w-md p-8">
        <div className="flex justify-center mb-8">
          <Logo size={160} />
        </div>
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold">SmartOrder Register</h1>
          <p>Enter your details below to create a new account</p>
        </div>
        <div aria-atomic="true" aria-live="polite" className="mt-4 pb-6">
          {errorMessage && (
            <Alert
              color="danger"
              description={errorMessage}
              title="Registration failed"
            />
          )}
        </div>
        <form onSubmit={formSubmitted}>
          <div className="grid gap-4">
            <div className="mb-4">
              <Input
                required
                id="username"
                name="username"
                placeholder="Username"
                type="text"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
              />
            </div>
            <div className="mb-4">
              <Input
                required
                id="password"
                name="password"
                placeholder="Password"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </div>
          </div>
          <RegisterButton pending={mutation.isPending} />
        </form>
        <div className="mt-4 text-center text-sm">
          Already have an account?{" "}
          <Link className="underline" href="/login">
            Login here
          </Link>
        </div>
      </div>
    </div>
  );
};

function RegisterButton({ pending }: { pending: boolean }) {
  return (
    <Button
      aria-disabled={pending}
      className="w-1/2 mt-4 mx-auto flex justify-center"
      type="submit"
    >
      Register <LuChevronRight className="ml-auto" />
    </Button>
  );
}

export default Register;
