"use client";

import React, { useState } from "react";
import { useMutation } from "@tanstack/react-query";
import { toast } from "react-toastify";

import { AuthLayout } from "@/components/auth/AuthLayout";
import { AuthForm } from "@/components/auth/AuthForm";

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

        throw new Error(
          errorData.message || "Registration failed. Try another username."
        );
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
      setErrorMessage(error.message);
    },
  });

  const formSubmitted = (event: { preventDefault: () => void }) => {
    event.preventDefault();
    setErrorMessage("");
    mutation.mutate();
  };

  return (
    <AuthLayout
      errorMessage={errorMessage}
      subtitle="Create your new account"
      title="SmartOrder Register"
    >
      <AuthForm
        bottomLinkHref="/login"
        bottomLinkText="Login here"
        bottomText="Already have an account?"
        buttonText="Register"
        isPending={mutation.isPending}
        password={password}
        setPassword={setPassword}
        setUsername={setUsername}
        username={username}
        onSubmit={formSubmitted}
      />
    </AuthLayout>
  );
};

export default Register;
