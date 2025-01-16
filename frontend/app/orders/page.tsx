"use client";

import React from "react";
import { useQuery } from "@tanstack/react-query";
import { useSession } from "next-auth/react";

export default function OrderPage() {
  const { data: session, status } = useSession();

  const {
    data: orders,
    error,
    isLoading,
  } = useQuery({
    queryKey: ["orders"],
    queryFn: async () => {
      if (!session?.user) throw new Error("No token found");
      const res = await fetch("http://localhost:4000/rest/order/orders", {
        headers: {
          Authorization: `Bearer ${session.accessToken}`,
        },
      });

      if (!res.ok) {
        throw new Error("Failed to fetch orders");
      }

      return res.json();
    },
    enabled: status === "authenticated",
  });

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error: {(error as Error).message}</div>;
  }

  return (
    <div>
      <div className="sm:flex-auto text-left">
        <h1 className="text-4xl font-semibold text-primary">Order Lists</h1>
        <p className="mt-2 text-m text-default-500">
          View and confirm your orders here.
        </p>
      </div>
      <pre className="mt-4 bg-gray-100 p-4 rounded">
        {JSON.stringify(orders, null, 2)}
      </pre>
    </div>
  );
}
