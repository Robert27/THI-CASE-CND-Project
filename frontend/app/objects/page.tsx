"use client";

import React, { useState } from "react";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { useSession } from "next-auth/react";
import { redirect } from "next/navigation";
import { LuPen, LuTrash2, LuCopy, LuPlus } from "react-icons/lu";
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
  Input,
} from "@nextui-org/react";
import Fuse from "fuse.js";

import { httpHost } from "../providers";

import ErrorDisplay from "@/components/ErrorDisplay";
import PageHeader from "@/components/PageHeader";
import LoadingSpinner from "@/components/LoadingSpinner";
import UniversalObjectModal from "@/components/UniversalObjectModal";
import { StorageObject, Category, StorageObjectResponse } from "@/types";

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
  const [alertPageMessage, setAlertPageMessage] = useState<string | null>(null);
  const [searchQuery, setSearchQuery] = useState("");
  const [prefillData, setPrefillData] = useState<Omit<
    StorageObject,
    "id" | "reorderUrl"
  > | null>(null);

  const queryClient = useQueryClient();

  const { data: categories = [] } = useQuery<Category[]>({
    queryKey: ["categories"],
    queryFn: async () => {
      const res = await fetch(httpHost + "/rest/object/category");

      if (!res.ok) {
        const error = "Failed to fetch categories";

        setAlertPageMessage(error);
        throw new Error(error);
      }

      return res.json();
    },
  });

  const {
    data: items = null,
    error: fetchError,
    isLoading,
  } = useQuery<StorageObjectResponse>({
    queryKey: ["objects"],
    queryFn: async () => {
      if (!session?.user) {
        const error = "No token found";

        setAlertPageMessage(error);
        throw new Error(error);
      }
      const res = await fetch(httpHost + "/rest/object/item", {
        headers: {
          Authorization: `Bearer ${session.accessToken}`,
        },
      });

      if (!res.ok) {
        const error = "Failed to fetch objects";

        setAlertPageMessage(error);
        throw new Error(error);
      }

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
    items?.items
      ? items.items.map((item) => ({
          ...item,
          weekdayName: weekdays[item.weekday],
          categoryName:
            categories.find((c) => c.id === item.categoryId)?.name || "Unknown",
        }))
      : [],
    {
      keys: ["name", "weekdayName", "categoryName"],
      threshold: 0.4,
      includeMatches: true,
    }
  );

  const filteredItems =
    searchQuery.trim() && items?.items
      ? fuse
          .search(searchQuery)
          .map((result) =>
            items.items.find((item) => item.id === result.item.id)
          )
          .filter((item): item is StorageObject => item !== undefined)
      : items?.items || [];

  const createObjectMutation = useMutation<
    void,
    Error,
    Omit<StorageObject, "id">
  >({
    mutationFn: async (newObjectData) => {
      if (!session?.user) throw new Error("No token found");
      const res = await fetch(httpHost + "/rest/object/item", {
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
      const res = await fetch(`${httpHost}/rest/object/item/${id}`, {
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
      setAlertMessage(error.message);
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

      const res = await fetch(`${httpHost}/rest/object/item/${id}`, {
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

  const handleDuplicate = (object: StorageObject) => {
    const { id, reorderUrl, ...strippedObject } = object;

    setPrefillData(strippedObject);
    setIsModalOpen(true);
  };

  const handleAlertClose = () => {
    setAlertMessage(null);
  };

  const handleModalClose = () => {
    setIsModalOpen(false);
    setIsEditModalOpen(false);
    setEditObject(null);
    setAlertMessage(null);
    setPrefillData(null);
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

          {alertPageMessage && (
            <Alert
              className="rounded-lg"
              color="danger"
              onClose={() => setAlertPageMessage(null)}
            >
              {alertPageMessage}
            </Alert>
          )}

          {/* Search and New Button Section */}
          <div className="flex justify-between items-center mb-4">
            <Input
              className="w-64"
              classNames={{
                // https://stackoverflow.com/questions/78637238/change-color-textarea-nextui
                label: "text-black/50 dark:text-white/90",
                input: [
                  "text-black/90 dark:text-white/90",
                  "placeholder:text-default-700/50 dark:placeholder:text-white/60",
                ],
                innerWrapper: "bg-transparent",
                inputWrapper: [
                  "shadow-xl",
                  "bg-white/70",
                  "dark:bg-default/60",
                  "backdrop-blur-xl",
                  "backdrop-saturate-200",
                  "group-data-[focus=true]:bg-default-200/50",
                  "dark:group-data-[focus=true]:bg-default/60",
                  "!cursor-text",
                ],
              }}
              placeholder="Search objects..."
              type="text"
              value={searchQuery}
              onChange={(e) => {
                setSearchQuery(e.target.value);
              }}
            />
            <Button
              className="text-white font-medium"
              color="primary"
              size="md"
              onPress={() => setIsModalOpen(true)}
            >
              <LuPlus scale={1.5} />
              New Object
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
                      <Chip className="text-white" color="primary" size="sm">
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
                            <LuPen color="white" />
                          </Button>
                        </Tooltip>
                        <Tooltip content="Duplicate this item" size="md">
                          <Button
                            isIconOnly
                            color="default"
                            size="sm"
                            onPress={() => handleDuplicate(item)}
                          >
                            <LuCopy />
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
            <div className="text-sm text-default-500 text-center mt-4">
              Total items: {items?.totalItems || 0}
            </div>
          </div>
        </div>
      </div>

      <UniversalObjectModal
        alertMessage={alertMessage}
        categories={categories}
        editObject={editObject}
        isLoading={
          createObjectMutation.isPending || updateObjectMutation.isPending
        }
        isOpen={isModalOpen || isEditModalOpen}
        mode={isModalOpen ? "create" : "edit"}
        prefillData={prefillData}
        onAlertClose={handleAlertClose}
        onClose={handleModalClose}
        onSubmit={(data) => {
          if (isModalOpen) {
            handleSubmit(data as Omit<StorageObject, "id">);
          } else {
            handleEditSubmit(data as StorageObject);
          }
        }}
      />
    </div>
  );
}
