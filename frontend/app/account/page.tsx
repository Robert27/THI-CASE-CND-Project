"use client";

import { useState } from "react";
import { useMutation } from "@tanstack/react-query";
import { signOut, useSession } from "next-auth/react";
import { toast } from "react-toastify";
import { Card, CardBody, CardHeader, Button } from "@heroui/react";
import { LuTrash2 } from "react-icons/lu";
import { redirect } from "next/navigation";

import { httpHost } from "../providers";

import { PasswordChangeForm } from "@/components/auth/PasswordChangeForm";

export default function Settings() {
  const { data: session } = useSession({
    required: true,
    onUnauthenticated() {
      redirect("/api/auth/signin");
    },
  });
  const [showDeleteConfirm, setShowDeleteConfirm] = useState(false);

  const passwordMutation = useMutation({
    mutationFn: async ({
      oldPassword,
      password,
    }: {
      oldPassword: string;
      password: string;
    }) => {
      if (oldPassword === password) {
        throw new Error("New password cannot be the same as the old password");
      }

      const url =
        httpHost + "/rest/user/user/" + session?.user?.sub + "/changepw";
      const body = JSON.stringify({
        oldPassword: oldPassword,
        newPassword: password,
      });

      console.log("url", url);
      console.log("body", body);
      const res = await fetch(url, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${session?.accessToken}`,
        },
        body: JSON.stringify({
          oldPassword: oldPassword,
          newPassword: password,
        }),
      });

      if (!res.ok) {
        const errorData = await res.json();

        throw new Error(errorData.message || "Failed to change password");
      }
    },
    onSuccess: () => {
      toast.success("Password changed successfully");
      signOut({ callbackUrl: "/api/auth/signin" });
      localStorage.removeItem("next-auth.session-token");
    },
    onError: (error) => {
      console.error("Password change error:", error);
      toast.error(error.message);
    },
  });

  const deleteMutation = useMutation({
    mutationFn: async () => {
      const url = httpHost + "/rest/user/user/" + session?.user?.sub;
      const res = await fetch(url, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${session?.accessToken}`,
        },
      });

      if (!res.ok) {
        throw new Error("Failed to delete account");
      }
    },
    onSuccess: () => {
      toast.success("Account deleted successfully");
      signOut({ callbackUrl: "/login" });
    },
    onError: (error) => {
      toast.error(error.message);
    },
  });

  return (
    <div className="container mx-auto p-4 space-y-4 mt-10">
      <Card className="max-w-xl mx-auto backdrop-blur-xl bg-default-100/40">
        <CardHeader>
          <h2 className="text-lg font-semibold">Change Password</h2>
        </CardHeader>
        <CardBody>
          <PasswordChangeForm
            isPending={passwordMutation.isPending}
            onSubmit={(oldPassword: string, password: string): void =>
              passwordMutation.mutate({ oldPassword, password })
            }
          />
        </CardBody>
      </Card>

      <Card className="max-w-xl mx-auto backdrop-blur-xl bg-default-100/40">
        <CardHeader>
          <h2 className="text-lg font-semibold text-danger">Delete Account</h2>
        </CardHeader>
        <CardBody>
          {!showDeleteConfirm ? (
            <Button
              className="bg-danger text-white"
              startContent={<LuTrash2 />}
              onPress={() => setShowDeleteConfirm(true)}
            >
              Delete Account
            </Button>
          ) : (
            <div className="space-y-4">
              <div className="flex gap-2">
                <Button
                  className="bg-danger text-white"
                  isLoading={deleteMutation.isPending}
                  onPress={() => deleteMutation.mutate()}
                >
                  Yes, delete my account
                </Button>
                <Button
                  variant="flat"
                  onPress={() => setShowDeleteConfirm(false)}
                >
                  Cancel
                </Button>
              </div>
              <p className="text-sm text-danger">
                Are you sure? This action cannot be undone.
              </p>
            </div>
          )}
        </CardBody>
      </Card>
    </div>
  );
}
