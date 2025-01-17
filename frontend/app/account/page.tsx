"use client";
import React, { useState } from "react";
import { Button, Input } from "@nextui-org/react";
import { useMutation } from "@tanstack/react-query";
import { signOut, useSession } from "next-auth/react";
import { useRouter } from "next/navigation";
import { toast } from "react-toastify";

import { AuthLayout } from "@/components/auth/AuthLayout";

export default function AccountPage() {
  const { data: session } = useSession();
  const userId = session?.user?.sub;
  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [message, setMessage] = useState("");
  const [isError, setIsError] = useState(false);
  const router = useRouter();

  // Change Password Mutation
  const changePwMutation = useMutation({
    mutationFn: async (payload: {
      oldPassword: string;
      newPassword: string;
    }) => {
      console.log("Payload:", payload);
      const res = await fetch(
        `http://localhost:4000/rest/user/user/${userId}/changepw`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${session?.accessToken}`,
          },
          body: JSON.stringify(payload),
        }
      );

      if (!res.ok) {
        const errorData = await res.json();

        throw new Error(errorData.message || "Password change failed");
      }
      const data = await res.json();

      console.log("Data:", data);
      if (!data.success === true) {
        throw new Error("Password change failed");
      }

      return data;
    },
    onSuccess: () => {
      setIsError(false);
      setMessage("Password updated successfully!");
    },
    onError: (err: any) => {
      setMessage(err.message);
      setIsError(true);
    },
  });

  // Delete Account Mutation
  const deleteAccountMutation = useMutation({
    mutationFn: async () => {
      const res = await fetch(
        `http://localhost:4000/rest/user/user/${userId}`,
        {
          method: "DELETE",
        }
      );

      if (!res.ok) {
        const errorData = await res.json();

        throw new Error(errorData.message || "Account deletion failed");
      }
    },
    onSuccess: () => {
      signOut({ redirect: false });
      localStorage.removeItem("next-auth.session-token");
      router.push("/");

      toast.success("Account deleted successfully");
    },
    onError: (err: any) => {
      setMessage(err.message);
      setIsError(true);
    },
  });

  const handleChangePassword = (e: React.FormEvent) => {
    e.preventDefault();
    setMessage("");
    changePwMutation.mutate({ oldPassword, newPassword });
  };

  const handleDeleteAccount = () => {
    if (window.confirm("Really delete your account?")) {
      setMessage("");
      deleteAccountMutation.mutate();
    }
  };

  return (
    <AuthLayout
      errorMessage={message}
      isError={isError}
      subtitle="Manage your account settings and password"
      title="Account Settings"
    >
      <form className="space-y-8" onSubmit={handleChangePassword}>
        <div className="space-y-4">
          <Input
            required
            className="hover:scale-101 transition-transform"
            classNames={{
              input: "text-base",
              inputWrapper: "py-2",
            }}
            label="Old Password"
            type="password"
            value={oldPassword}
            onChange={(e) => setOldPassword(e.target.value)}
          />
          <Input
            required
            className="hover:scale-101 transition-transform"
            classNames={{
              input: "text-base",
              inputWrapper: "py-2",
            }}
            label="New Password"
            type="password"
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
          />
        </div>
        <div className="space-y-4">
          <Button
            className="w-full bg-gradient-to-r from-primary to-secondary text-white font-semibold 
                     py-3 rounded-lg transition-transform hover:scale-102 active:scale-98"
            type="submit"
          >
            Change Password
          </Button>
          <Button
            className="w-full bg-danger text-white font-semibold py-3 rounded-lg 
                     transition-transform hover:scale-102 active:scale-98"
            onPress={handleDeleteAccount}
          >
            Delete Account
          </Button>
        </div>
      </form>
    </AuthLayout>
  );
}
