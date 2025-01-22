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
  Spinner,
} from "@heroui/react";
import { toast } from "react-toastify";

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
  isLoading?: boolean; // Add this prop
  prefillData?: Omit<StorageObject, "id" | "reorderUrl"> | null;
};

const MOCK_BASE_URL =
  "https://18516f24-a140-4a53-969b-9117ecfba30b.mock.pstmn.io";
const MOCK_OBJECTS = [
  { name: "schwamm", emoji: "🧽" },
  { name: "porsche", emoji: "🚗" },
  { name: "stein", emoji: "🪨" },
  { name: "tastatur", emoji: "⌨️" },
  { name: "papagei", emoji: "🦜" },
];

export default function UniversalObjectModal({
  isOpen,
  onClose,
  onSubmit,
  categories,
  alertMessage,
  onAlertClose,
  mode,
  editObject,
  isLoading = false,
  prefillData,
}: UniversalObjectModalProps) {
  const [formData, setFormData] = useState<Partial<StorageObject>>({
    name: "",
    description: "",
    categoryId: undefined,
    weekday: undefined,
    quantity: 1,
    reorderUrl: "",
  });

  const [isSubmitting, setIsSubmitting] = useState(false);

  useEffect(() => {
    if (mode === "edit" && editObject) {
      setFormData(editObject);
    } else if (mode === "create" && prefillData) {
      setFormData(prefillData);
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
  }, [mode, editObject, isOpen, prefillData]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!formData.categoryId || !formData.weekday) return;

    setIsSubmitting(true);
    try {
      const submitData =
        mode === "edit" && editObject
          ? { ...formData, id: editObject.id }
          : formData;

      await onSubmit(submitData as any);
      // Let the spinner show for a moment before closing
      setTimeout(() => {
        setIsSubmitting(false);
      }, 500);
    } catch (error) {
      setIsSubmitting(false);
    }
  };

  const copyToClipboard = async (text: string) => {
    try {
      await navigator.clipboard.writeText(text);
      toast.success("Link copied to clipboard!");
    } catch (err) {
      toast.error("Failed to copy link");
    }
  };

  const capitalizeFirstLetter = (string: string) => {
    return string.charAt(0).toUpperCase() + string.slice(1);
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
    <Modal isOpen={isOpen} size="2xl" onClose={onClose}>
      <ModalContent className="py-4">
        {(isSubmitting || isLoading) && (
          <div className="absolute inset-0 bg-background/70 backdrop-blur-sm z-50 flex items-center justify-center">
            <div className="flex flex-col items-center gap-2">
              <Spinner size="lg" />
              <p className="text-sm">
                {isSubmitting ? "Saving changes..." : "Processing..."}
              </p>
            </div>
          </div>
        )}
        <ModalHeader className="px-6">
          <h2 className="text-2xl font-semibold">
            {mode === "create" ? "Create New Object" : "Edit Object"}
          </h2>
        </ModalHeader>
        <ModalBody className="px-6">
          {alertMessage && (
            <Alert color="danger" onClose={onAlertClose}>
              {alertMessage}
            </Alert>
          )}
          <Form onSubmit={handleSubmit}>
            <div className="grid grid-cols-2 gap-4 w-full">
              <Input
                isRequired
                label="Name"
                value={formData.name || ""}
                onValueChange={(value) =>
                  setFormData({ ...formData, name: value })
                }
              />

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

              <div className="col-span-2">
                <Textarea
                  label="Description"
                  value={formData.description || ""}
                  onValueChange={(value) =>
                    setFormData({ ...formData, description: value })
                  }
                />
              </div>
              <div>
                <Select
                  isRequired
                  label="Category"
                  placeholder="Select a category"
                  selectedKeys={
                    formData.categoryId ? [formData.categoryId.toString()] : []
                  }
                  onSelectionChange={(keys) => {
                    const selectedId = Array.from(keys)[0];

                    setFormData({
                      ...formData,
                      categoryId: Number(selectedId),
                    });
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
              </div>
              <div>
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
              </div>
              <div className="col-span-2">
                <Input
                  isRequired
                  label="Reorder URL"
                  placeholder="https://example.com/reorder"
                  value={formData.reorderUrl || ""}
                  onValueChange={(value) =>
                    setFormData({ ...formData, reorderUrl: value })
                  }
                />
              </div>
            </div>
            <div className="flex mt-5">
              <Button
                className="px-10"
                color="primary"
                isDisabled={!canSubmit}
                type="submit"
              >
                {mode === "create" ? "Create" : "Save"}
              </Button>
              {!canSubmit && (
                <p className="text-xs text-default-500 self-center ml-4">
                  Fill in all required fields to submit
                </p>
              )}
            </div>
          </Form>

          <div className="h-px bg-divider my-4" />

          <div className="p-4 bg-default-100 rounded-lg">
            <h3 className="text-sm font-semibold mb-2">Available Mock Links</h3>
            <p className="text-sm text-default-500 mb-3">
              Use these mock API endpoints for testing. Click a button to copy
              the full URL.
            </p>
            <div className="flex flex-wrap gap-2">
              {MOCK_OBJECTS.map((obj) => (
                <Button
                  key={obj.name}
                  size="sm"
                  variant="flat"
                  onPress={() =>
                    copyToClipboard(`${MOCK_BASE_URL}/${obj.name}`)
                  }
                >
                  {obj.emoji} {capitalizeFirstLetter(obj.name)}
                </Button>
              ))}
            </div>
          </div>
        </ModalBody>
      </ModalContent>
    </Modal>
  );
}
