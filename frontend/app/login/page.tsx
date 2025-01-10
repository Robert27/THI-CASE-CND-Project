"use client";

import React, { useState } from "react";
import Link from "next/link";
import { signIn, useSession } from "next-auth/react";
import { useRouter } from "next/navigation";
import { Alert, Button, Input } from "@nextui-org/react";
import { LuChevronRight } from "react-icons/lu";

const Login: React.FC = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState(null);
  const [pending, setPending] = useState(false);
  const { status } = useSession();
  const router = useRouter();
  const formSubmitted = async (event) => {
    event.preventDefault();
    setPending(true);

    try {
      console.log("credentials", {
        redirect: false,
        user_id: username,
        password,
      });
      const res = await signIn("credentials", {
        redirect: false,
        user_id: username,
        password,
      });

      if (res?.error) {
        console.log("res error :::: ", res);
        setErrorMessage(res.error);
        setPending(false);
      } else {
        setPending(false);
        router.push("/");
      }
    } catch (error) {
      console.error("Login error:", error);
      setErrorMessage("An error occurred during login");
      setPending(false);
    }
  };

  return (
    <div className="flex items-center justify-center min-h-screen">
      <div className="w-full max-w-md p-8">
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold">SmartOrder Login</h1>
          <p>Enter your official username below to login to your account</p>
        </div>
        <div aria-atomic="true" aria-live="polite" className="mt-4 pb-6">
          {errorMessage && (
            <Alert
              color="danger"
              description={errorMessage}
              title="Login failed"
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
                placeholder="user"
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
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </div>
          </div>
          <LoginButton pending={pending} />
        </form>
        <div className="mt-4 text-center text-sm">
          Forgot Password?{" "}
          <Link className="underline" href="#">
            Contact Admin
          </Link>
        </div>
      </div>
    </div>
  );
};

function LoginButton({ pending }) {
  return (
    <Button
      aria-disabled={pending}
      className="w-1/2 mt-4 mx-auto flex justify-center"
      type="submit"
    >
      Log in <LuChevronRight className="ml-auto" />
    </Button>
  );
}

export default Login;
