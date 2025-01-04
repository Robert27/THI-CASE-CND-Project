"use client";

import React, { useEffect, useState } from "react";
import {
  Table,
  TableHeader,
  TableBody,
  TableColumn,
  TableRow,
  TableCell,
} from "@nextui-org/table";
import { useAsyncList } from "@react-stately/data";
import {
  Button,
  Chip,
  Dropdown,
  DropdownItem,
  DropdownMenu,
  DropdownTrigger,
  Input,
  Textarea,
  Form,
  Select,
  SelectItem,
} from "@nextui-org/react";
import { Modal, ModalContent, ModalHeader, ModalBody } from "@nextui-org/modal";
import { LuEllipsisVertical } from "react-icons/lu";

type StorageObject = {
  id: number;
  name: string;
  description: string;
  categoryId: number;
  reorderUrl: string;
};

type Category = {
  id: number;
  name: string;
  description: string;
};

export default function PricingPage() {
  const [categories, setCategories] = useState<Category[]>([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [modalName, setModalName] = useState("");
  const [modalDescription, setModalDescription] = useState("");
  const [modalCategoryId, setModalCategoryId] = useState<number | null>(null);

  const [modalReorderUrl, setModalReorderUrl] = useState("");

  useEffect(() => {
    fetch("http://localhost:8080/category")
      .then((res) => res.json())
      .then((data) => setCategories(data as Category[]))
      .catch((err) => console.error("Error fetching categories:", err));
  }, []);

  const list = useAsyncList<StorageObject>({
    async load({ signal }) {
      const res = await fetch("http://localhost:8080/object", { signal });
      const data = await res.json();

      return {
        items: data,
      };
    },
    async sort({ items, sortDescriptor }) {
      return {
        items: items.slice().sort((a, b) => {
          const first = a[sortDescriptor.column as keyof StorageObject];
          const second = b[sortDescriptor.column as keyof StorageObject];
          let cmp =
            (parseInt(first as string) || first) <
            (parseInt(second as string) || second)
              ? -1
              : 1;

          if (sortDescriptor.direction === "descending") {
            cmp *= -1;
          }

          return cmp;
        }),
      };
    },
  });

  const handleCreateNew = () => {
    setIsModalOpen(true);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const newObjectData = {
      name: modalName,
      description: modalDescription,
      categoryId: modalCategoryId, // Send only the ID
      reorderUrl: modalReorderUrl,
    };

    console.log(newObjectData);
    try {
      const res = await fetch("http://localhost:8080/object", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(newObjectData),
      });

      if (res.ok) {
        setIsModalOpen(false);
        list.reload();
      } else {
        console.error("Failed to create new object");
      }
    } catch (err) {
      console.error("Error creating new object:", err);
    }
  };

  const handleDelete = async (id: number) => {
    const confirmed = window.confirm(
      "Are you sure you want to delete this item?",
    );

    if (!confirmed) {
      return;
    }

    try {
      const res = await fetch(`http://localhost:8080/object/${id}`, {
        method: "DELETE",
      });

      if (res.ok) {
        list.reload();
      } else {
        alert("Failed to delete object");
      }
    } catch (err) {
      if (err instanceof Error) {
        alert("Error deleting object: " + err.message);
      } else {
        alert("Error deleting object");
      }
    }
  };

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10">
      <div className="sm:flex sm:items-center">
        <div className="sm:flex-auto text-left">
          <h1 className="text-2xl font-semibold text-primary">Your Objects</h1>
          <p className="mt-2 text-sm text-default-500">
            Click on a column header to sort
          </p>
        </div>
        <div className="mt-4 sm:mt-0 sm:ml-16 sm:flex-none">
          <Button color="primary" onPress={handleCreateNew}>
            + New Object
          </Button>
        </div>
      </div>

      <div className="mt-8 flow-root">
        <div className="-mx-4 -my-2 overflow-x-auto sm:-mx-6 lg:-mx-8">
          <div className="inline-block min-w-full py-2 align-middle">
            <Table
              aria-label="Objects Table"
              selectionMode="none"
              sortDescriptor={list.sortDescriptor}
              onSortChange={list.sort}
            >
              <TableHeader>
                <TableColumn allowsSorting>Name</TableColumn>
                <TableColumn>Description</TableColumn>
                <TableColumn allowsSorting>Category</TableColumn>
                <TableColumn>Reorder URL</TableColumn>
                <TableColumn>Actions</TableColumn>
              </TableHeader>
              <TableBody items={list.items}>
                {(item) => (
                  <TableRow key={item.id}>
                    <TableCell>{item.name}</TableCell>
                    <TableCell>{item.description}</TableCell>
                    <TableCell>
                      <Chip color="primary">
                        {categories.find((c) => c.id === item.categoryId)
                          ?.name || "Unknown"}
                      </Chip>
                    </TableCell>
                    <TableCell>{item.reorderUrl}</TableCell>
                    <TableCell>
                      <Dropdown className="bg-background border-1 border-default-200">
                        <DropdownTrigger>
                          <Button
                            isIconOnly
                            radius="full"
                            size="sm"
                            variant="light"
                          >
                            <LuEllipsisVertical />
                          </Button>
                        </DropdownTrigger>
                        <DropdownMenu>
                          <DropdownItem key="view">View & Edit</DropdownItem>
                          <DropdownItem
                            key="delete"
                            className="text-danger"
                            color="danger"
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

      <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)}>
        <ModalContent>
          <ModalHeader>
            <h2>Create New Object</h2>
          </ModalHeader>
          <ModalBody>
            <Form onSubmit={handleSubmit}>
              <Input
                isRequired
                required
                label="Name"
                value={modalName}
                onValueChange={(value) => setModalName(value)}
              />
              <Textarea
                required
                label="Description"
                value={modalDescription}
                onValueChange={(value) => setModalDescription(value)}
              />
              <Select
                isRequired
                label="Category"
                placeholder="Select a category"
                selectedKeys={
                  modalCategoryId ? [modalCategoryId.toString()] : []
                }
                onSelectionChange={(keys) => {
                  const selectedId = Array.from(keys)[0];

                  setModalCategoryId(Number(selectedId));
                }}
              >
                {categories.map((category) => (
                  <SelectItem key={category.id} value={category.id.toString()}>
                    {category.name}
                  </SelectItem>
                ))}
              </Select>
              <Input
                label="Reorder URL"
                value={modalReorderUrl}
                onValueChange={(value) => setModalReorderUrl(value)}
              />
              <Button color="primary" type="submit">
                Create
              </Button>
            </Form>
          </ModalBody>
        </ModalContent>
      </Modal>
    </div>
  );
}
