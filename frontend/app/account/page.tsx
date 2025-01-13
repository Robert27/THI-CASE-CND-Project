"use client";
import React, { useState } from "react";
import { Alert, Button, Input } from "@nextui-org/react";
import { useMutation } from "@tanstack/react-query";
import { useSession } from "next-auth/react";

export default function AccountPage() {
  const { data: session } = useSession();
  const userId = session?.user?.sub;
  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [message, setMessage] = useState("");

  // Change Password Mutation
  const changePwMutation = useMutation({
    mutationFn: async (payload: {
      oldPassword: string;
      newPassword: string;
    }) => {
      const res = await fetch(`http://localhost:4000/api/user/${userId}/changepw`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
      });

      if (!res.ok) {
        const errorData = await res.json();

        throw new Error(errorData.message || "Password change failed");
      }

      return res.json();
    },
    onSuccess: () => {
      setMessage("Password updated successfully!");
    },
    onError: (err: any) => {
      setMessage(err.message);
    },
  });

  // Delete Account Mutation
  const deleteAccountMutation = useMutation({
    mutationFn: async () => {
      const res = await fetch(`http://localhost:4000/api/user/${userId}`, {
        method: "DELETE",
      });

      if (!res.ok) {
        const errorData = await res.json();

        throw new Error(errorData.message || "Account deletion failed");
      }
    },
    onSuccess: () => {
      setMessage("Account deleted!");
    },
    onError: (err: any) => {
      setMessage(err.message);
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
    <div className="p-8 flex flex-col items-center gap-4">
      <h1 className="text-3xl font-semibold">User Account</h1>
      {message && (
        <Alert
          color="primary"
          description={message}
          title="Notification"
          onClose={() => setMessage("")}
        />
      )}
      <form
        className="flex flex-col gap-4 w-full max-w-md"
        onSubmit={handleChangePassword}
      >
        <Input
          required
          label="Old Password"
          type="password"
          value={oldPassword}
          onChange={(e) => setOldPassword(e.target.value)}
        />
        <Input
          required
          label="New Password"
          type="password"
          value={newPassword}
          onChange={(e) => setNewPassword(e.target.value)}
        />
        <Button type="submit">Change Password</Button>
      </form>
      <Button color="danger" onPress={handleDeleteAccount}>
        Delete Account
      </Button>
    </div>
  );
}
