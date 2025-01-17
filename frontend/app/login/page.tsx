"use client";

import React, { useState } from "react";
import { signIn } from "next-auth/react";
import { useRouter } from "next/navigation";
import { toast } from "react-toastify";

import { AuthLayout } from "@/components/auth/AuthLayout";
import { AuthForm } from "@/components/auth/AuthForm";

const Login: React.FC = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [pending, setPending] = useState(false);
  const router = useRouter();
  const formSubmitted = async (event: { preventDefault: () => void }) => {
    event.preventDefault();
    setPending(true);

    try {
      const res = await signIn("credentials", {
        redirect: false,
        username,
        password,
      });

      if (res?.error) {
        console.log("res error :::: ", res);
        setErrorMessage(res.error);
        setPending(false);
      } else {
        setPending(false);
        toast.success("Login successful");
        router.push("/");
      }
    } catch (error) {
      console.error("Login error:", error);
      setErrorMessage("An error occurred during login");
      setPending(false);
    }
  };

  return (
    <AuthLayout
      errorMessage={errorMessage}
      subtitle="Enter your credentials to access your account"
      title="SmartOrder Login"
    >
      <AuthForm
        bottomLinkHref="/register"
        bottomLinkText="Register here"
        bottomText="New user?"
        buttonText="Log in"
        isPending={pending}
        password={password}
        setPassword={setPassword}
        setUsername={setUsername}
        username={username}
        onSubmit={formSubmitted}
      />
    </AuthLayout>
  );
};

export default Login;
