"use client";

import React, { useState } from "react";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { useSession } from "next-auth/react";
import { redirect } from "next/navigation";
import { LuPen, LuTrash2 } from "react-icons/lu";
import {
  Button,
  Alert,
  Table,
  TableBody,
  TableCell,
  TableColumn,
  TableHeader,
  TableRow,
  Chip,
  Tooltip,
} from "@nextui-org/react";
import Fuse from "fuse.js";

import ErrorDisplay from "@/components/ErrorDisplay";
import PageHeader from "@/components/PageHeader";
import LoadingSpinner from "@/components/LoadingSpinner";
import CreateObjectModal from "@/components/CreateObjectModal";
import EditObjectModal from "@/components/EditObjectModal";
import { StorageObject, Category } from "@/types";

export default function ObjectsPage() {
  const { data: session, status } = useSession({
    required: true,
    onUnauthenticated() {
      redirect("/api/auth/signin");
    },
  });

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [editObject, setEditObject] = useState<StorageObject | null>(null);
  const [alertMessage, setAlertMessage] = useState<string | null>(null);
  const [searchQuery, setSearchQuery] = useState("");

  const queryClient = useQueryClient();

  const { data: categories = [] } = useQuery<Category[]>({
    queryKey: ["categories"],
    queryFn: async () => {
      const res = await fetch("http://localhost:4000/rest/object/category");

      if (!res.ok) throw new Error("Failed to fetch categories");

      return res.json();
    },
  });

  const {
    data: items = [],
    error: fetchError,
    isLoading,
  } = useQuery<StorageObject[]>({
    queryKey: ["objects"],
    queryFn: async () => {
      if (!session?.user) throw new Error("No token found");
      const res = await fetch("http://localhost:4000/rest/object/item", {
        headers: {
          Authorization: `Bearer ${session.accessToken}`,
        },
      });

      if (!res.ok) throw new Error("Failed to fetch objects");

      return res.json();
    },
    enabled: status === "authenticated",
  });

  const weekdays = [
    "Monday",
    "Tuesday",
    "Wednesday",
    "Thursday",
    "Friday",
    "Saturday",
    "Sunday",
  ];

  const fuse = new Fuse(
    items.map((item) => ({
      ...item,
      weekdayName: weekdays[item.weekday],
      categoryName:
        categories.find((c) => c.id === item.categoryId)?.name || "Unknown",
    })),
    {
      keys: ["name", "weekdayName", "categoryName"],
      threshold: 0.4,
      includeMatches: true,
    }
  );

  const filteredItems = searchQuery.trim()
    ? fuse
        .search(searchQuery)
        .map((result) => items.find((item) => item.id === result.item.id)!)
    : items;

  const createObjectMutation = useMutation<
    void,
    Error,
    Omit<StorageObject, "id">
  >({
    mutationFn: async (newObjectData) => {
      if (!session?.user) throw new Error("No token found");
      const res = await fetch("http://localhost:4000/rest/object/item", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${session.accessToken}`,
        },
        body: JSON.stringify(newObjectData),
      });

      if (!res.ok) {
        let errorMsg = "Failed to create object";

        try {
          const data = await res.json();

          console.log(data);
          errorMsg = data.errorMessage || errorMsg;
        } catch {}
        throw new Error(errorMsg);
      }
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["objects"] });
      setIsModalOpen(false);
    },
    onError: (error) => {
      setAlertMessage(error.message);
    },
  });

  const handleSubmit = (newObjectData: Omit<StorageObject, "id">) => {
    createObjectMutation.mutate(newObjectData);
  };

  const deleteObjectMutation = useMutation<void, Error, number>({
    mutationFn: async (id) => {
      if (!session?.user) throw new Error("No token found");
      const res = await fetch(`http://localhost:4000/rest/object/item/${id}`, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${session.accessToken}`,
        },
      });

      if (!res.ok) {
        let errorMsg = "Failed to delete object";

        try {
          const data = await res.json();

          errorMsg = data.message || errorMsg;
        } catch {}
        throw new Error(errorMsg);
      }
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["objects"] });
    },
    onError: (error) => {
      alert(error.message);
    },
  });

  const handleDelete = (id: number) => {
    const confirmed = window.confirm(
      "Are you sure you want to delete this item?"
    );

    if (!confirmed) {
      return;
    }

    deleteObjectMutation.mutate(id);
  };

  const handleEdit = (object: StorageObject) => {
    setEditObject(object);
    setIsEditModalOpen(true);
  };

  const handleEditSubmit = (updatedObjectData: StorageObject) => {
    updateObjectMutation.mutate(updatedObjectData);
  };

  const updateObjectMutation = useMutation<void, Error, StorageObject>({
    mutationFn: async (updatedObjectData) => {
      if (!session?.user) throw new Error("No token found");

      const { id, ...rest } = updatedObjectData;
      // Only send changed fields
      const changes = Object.entries(rest).reduce((acc, [key, val]) => {
        if (val !== editObject?.[key as keyof StorageObject]) {
          (acc as any)[key as keyof StorageObject] = val;
        }

        return acc;
      }, {} as Partial<StorageObject>);

      console.log("changes", changes);
      const res = await fetch(`http://localhost:4000/rest/object/item/${id}`, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${session.accessToken}`,
        },
        body: JSON.stringify(changes),
      });

      if (!res.ok) {
        let errorMsg = "Failed to update object";

        try {
          const data = await res.json();

          errorMsg = data.errorMessage || errorMsg;
        } catch {}
        throw new Error(errorMsg);
      }
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["objects"] });
      setIsEditModalOpen(false);
    },
    onError: (error) => {
      setAlertMessage(error.message);
    },
  });

  const handleAlertClose = () => {
    setAlertMessage(null);
  };

  if (isLoading) return <LoadingSpinner />;
  if (fetchError) return <ErrorDisplay error={fetchError as Error} />;

  return (
    <div className="min-h-screen">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="p-6 space-y-6">
          {/* Header Section */}
          <div className="mb-8">
            <PageHeader
              description="Manage your objects and their categories"
              title="Your Objects"
            />
          </div>

          {alertMessage && (
            <Alert
              className="rounded-lg"
              color="danger"
              onClose={() => setAlertMessage(null)}
            >
              {alertMessage}
            </Alert>
          )}

          {/* Search and New Button Section */}
          <div className="flex justify-between items-center mb-4">
            <input
              className="w-64 px-4 py-2 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-primary"
              placeholder="Search objects..."
              type="text"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
            />
            <Button
              color="primary"
              size="lg"
              onPress={() => setIsModalOpen(true)}
            >
              + New Object
            </Button>
          </div>

          <div className="">
            <Table
              aria-label="Objects table"
              className="w-full"
              selectionMode="none"
            >
              <TableHeader>
                <TableColumn>Name</TableColumn>
                <TableColumn>Category</TableColumn>
                <TableColumn>Quantity</TableColumn>
                <TableColumn>Weekday</TableColumn>
                <TableColumn>Actions</TableColumn>
              </TableHeader>
              <TableBody items={filteredItems}>
                {(item) => (
                  <TableRow key={item.id} onClick={() => handleEdit(item)}>
                    <TableCell>{item.name}</TableCell>
                    <TableCell>
                      {categories.find((c) => c.id === item.categoryId)?.name ||
                        "Unknown"}
                    </TableCell>
                    <TableCell>{item.quantity}</TableCell>
                    <TableCell>
                      <Chip color="primary" size="sm">
                        {weekdays[item.weekday]}
                      </Chip>
                    </TableCell>
                    <TableCell>
                      <div className="flex gap-2">
                        <Tooltip content="Edit this item" size="md">
                          <Button
                            isIconOnly
                            color="primary"
                            size="sm"
                            onPress={() => handleEdit(item)}
                          >
                            <LuPen />
                          </Button>
                        </Tooltip>
                        <Tooltip content="Delete this item" size="md">
                          <Button
                            isIconOnly
                            color="default"
                            size="sm"
                            onPress={() => handleDelete(item.id)}
                          >
                            <LuTrash2 />
                          </Button>
                        </Tooltip>
                      </div>
                    </TableCell>
                  </TableRow>
                )}
              </TableBody>
            </Table>
          </div>
        </div>
      </div>

      <CreateObjectModal
        key={String(isModalOpen)}
        alertMessage={alertMessage}
        categories={categories}
        isOpen={isModalOpen}
        onAlertClose={handleAlertClose}
        onClose={() => {
          setIsModalOpen(false);
          setAlertMessage(null);
        }}
        onSubmit={handleSubmit}
      />

      <EditObjectModal
        alertMessage={alertMessage}
        categories={categories}
        editObject={editObject}
        isOpen={isEditModalOpen}
        onAlertClose={handleAlertClose}
        onClose={() => {
          setIsEditModalOpen(false);
          setAlertMessage(null);
        }}
        onSubmit={handleEditSubmit}
      />
    </div>
  );
}
