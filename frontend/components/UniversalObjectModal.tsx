"use client";

import React, { useState, useEffect } from "react";
import {
  Modal,
  ModalContent,
  ModalHeader,
  ModalBody,
  Alert,
  Button,
  Input,
  Textarea,
  Form,
  Select,
  SelectItem,
} from "@nextui-org/react";

import { StorageObject } from "@/types";

type Category = {
  id: number;
  name: string;
  description: string;
};

type UniversalObjectModalProps = {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (data: StorageObject | Omit<StorageObject, "id">) => void;
  categories: Category[];
  alertMessage: string | null;
  onAlertClose: () => void;
  mode: "create" | "edit";
  editObject?: StorageObject | null;
};

export default function UniversalObjectModal({
  isOpen,
  onClose,
  onSubmit,
  categories,
  alertMessage,
  onAlertClose,
  mode,
  editObject,
}: UniversalObjectModalProps) {
  const [formData, setFormData] = useState<Partial<StorageObject>>({
    name: "",
    description: "",
    categoryId: undefined,
    weekday: undefined,
    quantity: 1,
    reorderUrl: "",
  });

  useEffect(() => {
    if (mode === "edit" && editObject) {
      setFormData(editObject);
    } else {
      setFormData({
        name: "",
        description: "",
        categoryId: undefined,
        weekday: undefined,
        quantity: 1,
        reorderUrl: "",
      });
    }
  }, [mode, editObject, isOpen]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!formData.categoryId || !formData.weekday) return;

    const submitData =
      mode === "edit" && editObject
        ? { ...formData, id: editObject.id }
        : formData;

    onSubmit(submitData as any);
  };

  const weekdays = [
    "Monday",
    "Tuesday",
    "Wednesday",
    "Thursday",
    "Friday",
    "Saturday",
    "Sunday",
  ];

  const canSubmit =
    formData.name?.trim() !== "" &&
    formData.categoryId !== null &&
    formData.reorderUrl?.trim() !== "" &&
    formData.weekday !== null;

  return (
    <Modal isOpen={isOpen} onClose={onClose}>
      <ModalContent className="py-4">
        <ModalHeader>
          <h2 className="text-2xl font-semibold">
            {mode === "create" ? "Create New Object" : "Edit Object"}
          </h2>
        </ModalHeader>
        <ModalBody>
          {alertMessage && (
            <Alert color="danger" onClose={onAlertClose}>
              {alertMessage}
            </Alert>
          )}
          <Form onSubmit={handleSubmit}>
            <Input
              isRequired
              label="Name"
              value={formData.name || ""}
              onValueChange={(value) =>
                setFormData({ ...formData, name: value })
              }
            />
            <Textarea
              label="Description"
              value={formData.description || ""}
              onValueChange={(value) =>
                setFormData({ ...formData, description: value })
              }
            />
            <Select
              isRequired
              label="Category"
              placeholder="Select a category"
              selectedKeys={
                formData.categoryId ? [formData.categoryId.toString()] : []
              }
              onSelectionChange={(keys) => {
                const selectedId = Array.from(keys)[0];

                setFormData({ ...formData, categoryId: Number(selectedId) });
              }}
            >
              {categories.map((category) => (
                <SelectItem
                  key={category.id}
                  description={category.description}
                  value={category.id.toString()}
                >
                  {category.name}
                </SelectItem>
              ))}
            </Select>
            <Select
              isRequired
              label="Weekday"
              placeholder="Select a weekday"
              selectedKeys={
                formData.weekday !== undefined
                  ? [formData.weekday.toString()]
                  : []
              }
              onSelectionChange={(keys) => {
                const selectedId = Array.from(keys)[0];

                setFormData({ ...formData, weekday: Number(selectedId) });
              }}
            >
              {weekdays.map((day, index) => (
                <SelectItem key={index} value={index.toString()}>
                  {day}
                </SelectItem>
              ))}
            </Select>

            <Input
              isRequired
              label="Quantity"
              min={1}
              type="number"
              value={formData.quantity?.toString() || "1"}
              onValueChange={(value) =>
                setFormData({ ...formData, quantity: Number(value) })
              }
            />

            <Input
              isRequired
              label="Reorder URL"
              placeholder="https://example.com/reorder"
              value={formData.reorderUrl || ""}
              onValueChange={(value) =>
                setFormData({ ...formData, reorderUrl: value })
              }
            />
            <div className="flex justify-center mt-4">
              <Button color="primary" isDisabled={!canSubmit} type="submit">
                {mode === "create" ? "Create" : "Save"}
              </Button>
            </div>
          </Form>
        </ModalBody>
      </ModalContent>
    </Modal>
  );
}
