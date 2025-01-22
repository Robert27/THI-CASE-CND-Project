"use client";

import React from "react";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { useSession } from "next-auth/react";
import { redirect } from "next/navigation";
import {
  LuCircleCheck,
  LuShoppingCart,
  LuTriangleAlert,
  LuX,
} from "react-icons/lu";
import {
  Table,
  TableHeader,
  TableBody,
  TableColumn,
  TableRow,
  TableCell,
  Button,
  Tooltip,
  Card,
  CardBody,
  Spinner,
} from "@heroui/react";
import { toast } from "react-toastify";

import ErrorDisplay from "@/components/ErrorDisplay";
import PageHeader from "@/components/PageHeader";
import { Order } from "@/types";

type GroupedOrders = {
  [key: string]: Order[];
};

const httpHost = process.env.NEXT_PUBLIC_HTTP_HOST ?? "";

const useLoadingMessages = () => {
  const messages = [
    "Fetching your orders...",
    "Checking latest prices...",
    "Updating availability...",
    "Creating order list...",
    "Almost there...",
    "Hang tight, we're on it...",
    "Just a moment more...",
    "Preparing your data...",
    "Loading your orders...",
    "Finalizing details...",
  ];
  const [currentMessage, setCurrentMessage] = React.useState(0);

  React.useEffect(() => {
    const interval = setInterval(() => {
      setCurrentMessage((prev) => (prev + 1) % messages.length);
    }, 1500);

    return () => clearInterval(interval);
  }, []);

  return messages[currentMessage];
};

export default function OrderPage() {
  const { data: session, status } = useSession({
    required: true,
    onUnauthenticated() {
      redirect("/api/auth/signin");
    },
  });

  const queryClient = useQueryClient();

  const {
    data: orders,
    error,
    isLoading,
  } = useQuery<Order[]>({
    queryKey: ["orders"],
    queryFn: async () => {
      const res = await fetch(httpHost + "/rest/order/orders", {
        headers: {
          Authorization: `Bearer ${session?.accessToken}`,
        },
      });

      if (!res.ok) throw new Error("Failed to fetch orders");

      return res.json();
    },
    enabled: status === "authenticated",
  });

  const performOrderMutation = useMutation({
    mutationFn: async ({
      itemId,
      quantity,
    }: {
      itemId: number;
      quantity: number;
    }) => {
      const res = await fetch(httpHost + "/rest/order/orders/perform", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${session?.accessToken}`,
        },
        body: JSON.stringify({
          itemId,
          quantity,
          authToken: session?.accessToken,
        }),
      });

      if (!res.ok) throw new Error("Failed to perform order");

      return res.json();
    },
    onSuccess: () => {
      toast.success("Order performed successfully");
      queryClient.invalidateQueries({ queryKey: ["orders"] });
    },
    onError: (error) => {
      toast.error(error.message);
    },
  });

  const abortOrderMutation = useMutation({
    mutationFn: async (itemId: number) => {
      const res = await fetch(`${httpHost}/rest/order/orders/abort/${itemId}`, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${session?.accessToken}`,
        },
      });

      if (!res.ok) throw new Error("Failed to abort order");

      return res.json();
    },
    onSuccess: () => {
      toast.success("Order aborted successfully");
      queryClient.invalidateQueries({ queryKey: ["orders"] });
    },
    onError: (error) => {
      toast.error(error.message);
    },
  });

  const handleBuy = (itemId: number, quantity: number) => {
    performOrderMutation.mutate({ itemId, quantity });
  };

  const handleCancel = (itemId: number) => {
    abortOrderMutation.mutate(itemId);
  };

  const groupOrdersByDate = (orders: Order[]): GroupedOrders => {
    return (
      orders?.reduce((groups: GroupedOrders, order) => {
        const date = new Date(order.cycleDate).toLocaleDateString("de-DE", {
          day: "2-digit",
          month: "2-digit",
          year: "numeric",
        });

        if (!groups[date]) {
          groups[date] = [];
        }
        groups[date].push(order);

        return groups;
      }, {}) || {}
    );
  };
  const loadingMessage = useLoadingMessages();

  if (error) return <ErrorDisplay error={error as Error} />;

  const groupedOrders = groupOrdersByDate(orders || []);
  const sortedDates = Object.keys(groupedOrders).sort((a, b) => {
    const dateA = new Date(a.split(".").reverse().join("-"));
    const dateB = new Date(b.split(".").reverse().join("-"));

    return dateA.getTime() - dateB.getTime();
  });

  const formatPrice = (price: number | null) => {
    if (price === null) return "N/A";

    return price.toFixed(2).replace(".", ",") + " €";
  };

  return (
    <div className="max-w-7xl mx-auto p-4 pt-8 space-y-6">
      <PageHeader
        description="View and confirm your orders here."
        title="Order Lists"
      />

      <div className="mt-8">
        {isLoading ? (
          <div className="flex flex-col items-center gap-4 py-8">
            <Spinner size="lg" />
            <p className="text-default-500 animate-pulse mt-1 font-bold">
              {loadingMessage}
            </p>
          </div>
        ) : sortedDates.length === 0 ? (
          <Card className="max-w-screen-md mx-auto space-y-4 backdrop-blur-xl bg-default-100/40">
            <CardBody className="text-center py-8">
              <h3 className="text-xl font-semibold mb-2">No Orders Found</h3>
              <p className="text-default-500">
                No due orders found yet. Please check back later.
                <br />
                To create objects go to the objects page and assign them to a
                week day.
              </p>
            </CardBody>
          </Card>
        ) : (
          <div className="space-y-6">
            {sortedDates.map((date) => (
              <div key={date} className="space-y-2">
                <h3 className="text-lg font-semibold">Orders for {date}</h3>
                <Table
                  aria-label="Orders table"
                  className="w-full"
                  selectionMode="none"
                >
                  <TableHeader>
                    <TableColumn>Item Name</TableColumn>
                    <TableColumn>Order Quantity</TableColumn>
                    <TableColumn>Price</TableColumn>
                    <TableColumn>Status</TableColumn>
                    <TableColumn>Actions</TableColumn>
                  </TableHeader>
                  <TableBody items={groupedOrders[date]}>
                    {(item) => (
                      <TableRow key={item.itemId}>
                        <TableCell>
                          <Tooltip content={item.description}>
                            {item.itemName}
                          </Tooltip>
                        </TableCell>

                        <TableCell>
                          {item.orderQuantity +
                            " / " +
                            item.availabilityQuantity}
                        </TableCell>
                        <TableCell>{formatPrice(item.price)}</TableCell>
                        <TableCell>
                          <Tooltip content={item.statusMessage}>
                            {item.statusMessage.includes("Fehler") ? (
                              <Button content="error" size="sm">
                                <p className="text-warning">Error</p>
                                <LuTriangleAlert className="text-warning text-xl" />
                              </Button>
                            ) : (
                              <Button content="success" size="sm">
                                <p className="text-success">Success</p>
                                <LuCircleCheck className="text-success text-xl" />
                              </Button>
                            )}
                          </Tooltip>
                        </TableCell>
                        <TableCell>
                          <div className="flex gap-2">
                            <Tooltip content="Buy this item" size="md">
                              <Button
                                isIconOnly
                                color="primary"
                                isLoading={performOrderMutation.isPending}
                                size="sm"
                                onPress={() =>
                                  handleBuy(item.itemId, item.orderQuantity)
                                }
                              >
                                <LuShoppingCart />
                              </Button>
                            </Tooltip>
                            <Tooltip content="Cancel this item" size="md">
                              <Button
                                isIconOnly
                                color="default"
                                isLoading={abortOrderMutation.isPending}
                                size="sm"
                                onPress={() => handleCancel(item.itemId)}
                              >
                                <LuX />
                              </Button>
                            </Tooltip>
                          </div>
                        </TableCell>
                      </TableRow>
                    )}
                  </TableBody>
                </Table>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
