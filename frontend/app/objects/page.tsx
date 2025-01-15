"use client";

import { LuEllipsisVertical, LuSquarePen, LuTrash2 } from "react-icons/lu";
import React, { useState } from "react";
import {
  Button,
  Chip,
  Dropdown,
  DropdownItem,
  DropdownMenu,
  DropdownTrigger,
  Alert,
  Spinner,
  Table,
  TableBody,
  TableCell,
  TableColumn,
  TableHeader,
  TableRow,
} from "@nextui-org/react";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { useSession } from "next-auth/react";

import CreateObjectModal from "@/components/CreateObjectModal";
import EditObjectModal from "@/components/EditObjectModal";
import { StorageObject, Category } from "@/types";

export default function PricingPage() {
  const { data: session, status } = useSession();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [editObject, setEditObject] = useState<StorageObject | null>(null);
  const [alertMessage, setAlertMessage] = useState<string | null>(null);

  const queryClient = useQueryClient();

  const { data: categories = [] } = useQuery<Category[]>({
    queryKey: ["categories"],
    queryFn: async () => {
      const res = await fetch("http://localhost:4000/rest/object/category");

      console.log("res", res);

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

      if (!res.ok) {
        throw new Error("Failed to fetch objects");
      }

      return res.json();
    },
    enabled: status === "authenticated",
  });

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

  const handleCreateNew = () => {
    setIsModalOpen(true);
  };

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

  return (
    <div className="w-full px-4 ">
      <div className="sm:flex sm:items-center">
        <div className="sm:flex-auto text-left">
          <h1 className="text-3xl font-semibold text-primary">Your Objects</h1>
          <p className="mt-2 text-m text-default-500">
            Manage your objects and their categories
          </p>
        </div>
        <div className="mt-4 sm:mt-0 sm:ml-16 sm:flex-none">
          <Button color="primary" onPress={handleCreateNew}>
            + New Object
          </Button>
        </div>
      </div>

      {fetchError && (
        <Alert
          color="danger"
          description={fetchError.message}
          title="Error"
          onClose={handleAlertClose}
        />
      )}

      {isLoading ? (
        <div className="flex justify-center items-center h-64">
          <Spinner size="lg" />
        </div>
      ) : (
        <div className="mt-8 flow-root">
          <div className="-mx-4 -my-2 overflow-x-auto sm:-mx-6 lg:-mx-8">
            <div className="inline-block min-w-full py-2 align-middle">
              <Table aria-label="Objects Table" selectionMode="none">
                <TableHeader>
                  <TableColumn>Name</TableColumn>
                  <TableColumn>Category</TableColumn>
                  <TableColumn>Quantity</TableColumn>
                  <TableColumn>Weekday</TableColumn>
                  <TableColumn>Actions</TableColumn>
                </TableHeader>
                <TableBody items={items}>
                  {(item) => (
                    <TableRow key={item.id} onClick={() => handleEdit(item)}>
                      <TableCell>{item.name}</TableCell>
                      <TableCell>
                        <Chip color="primary">
                          {categories.find((c) => c.id === item.categoryId)
                            ?.name || "Unknown"}
                        </Chip>
                      </TableCell>
                      <TableCell>{item.quantity}</TableCell>
                      <TableCell>{item.weekday}</TableCell>
                      <TableCell>
                        <Dropdown className="bg-background border-1 border-default-200">
                          <DropdownTrigger>
                            <Button
                              isIconOnly
                              radius="full"
                              size="sm"
                              variant="light"
                            >
                              <LuEllipsisVertical size={18} />
                            </Button>
                          </DropdownTrigger>
                          <DropdownMenu>
                            <DropdownItem
                              key="edit"
                              endContent={<LuSquarePen size={16} />}
                              onPress={() => handleEdit(item)}
                            >
                              Edit
                            </DropdownItem>
                            <DropdownItem
                              key="delete"
                              className="text-danger"
                              color="danger"
                              endContent={<LuTrash2 color="danger" size={16} />}
                              onPress={() => handleDelete(item.id)}
                            >
                              Delete
                            </DropdownItem>
                          </DropdownMenu>
                        </Dropdown>
                      </TableCell>
                    </TableRow>
                  )}
                </TableBody>
              </Table>
            </div>
          </div>
        </div>
      )}

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
          setIsModalOpen(false);
          setAlertMessage(null);
        }}
        onSubmit={handleEditSubmit}
      />
    </div>
  );
}
