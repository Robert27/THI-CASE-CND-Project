"use client";

import React, { useState } from "react";
import { useMutation } from "@tanstack/react-query";
import { toast } from "react-toastify";
import Confetti from "react-confetti-boom";

import { httpHost } from "../providers";

import { AuthLayout } from "@/components/auth/AuthLayout";
import { AuthForm } from "@/components/auth/AuthForm";

const Register: React.FC = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [showConfetti, setShowConfetti] = useState(false);

  const mutation = useMutation({
    mutationFn: async () => {
      const res = await fetch(httpHost + "/rest/user/user", {
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
    onSuccess: () => {
      setShowConfetti(true);
      toast.success("Registration successful");
      setTimeout(() => {
        window.location.href = "/login";
      }, 2000); // Redirect after 2 seconds to show confetti
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
    <>
      {showConfetti && (
        <Confetti
          colors={["#b008da", "#167af5", "#d63ef4", "#0eb4f5"]}
          effectCount={60}
          launchSpeed={1.3}
          particleCount={60}
          shapeSize={20}
        />
      )}
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
    </>
  );
};

export default Register;
