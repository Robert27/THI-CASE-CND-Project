"use client";

import React, { useState } from "react";
import {
  Modal,
  ModalContent,
  ModalHeader,
  ModalBody,
  Alert,
} from "@nextui-org/react";
import {
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

type CreateObjectModalProps = {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (newObjectData: Omit<StorageObject, "id">) => void;
  categories: Category[];
  alertMessage: string | null;
  onAlertClose: () => void;
};

export default function CreateObjectModal({
  isOpen,
  onClose,
  onSubmit,
  categories,
  alertMessage,
  onAlertClose,
}: CreateObjectModalProps) {
  const [modalName, setModalName] = useState("");
  const [modalDescription, setModalDescription] = useState("");
  const [modalCategoryId, setModalCategoryId] = useState<number | null>(null);
  const [modalWeekday, setModalWeekday] = useState<number | null>(null); // 0-6 for Monday to Sunday
  const [modalQuantity, setModalQuantity] = useState(1); // 1 unit
  const [modalReorderUrl, setModalReorderUrl] = useState("");

  React.useEffect(() => {
    if (isOpen) {
      setModalName("");
      setModalDescription("");
      setModalCategoryId(null);
      setModalWeekday(null);
      setModalQuantity(1);
      setModalReorderUrl("");
    }
  }, [isOpen]);

  const canCreate =
    modalName.trim() !== "" &&
    modalCategoryId !== null &&
    modalReorderUrl.trim() !== "" &&
    modalWeekday !== null &&
    modalQuantity > 0;

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (modalCategoryId === null || modalWeekday === null) {
      return;
    }
    const newObjectData = {
      name: modalName,
      description: modalDescription,
      categoryId: modalCategoryId, // Send only the ID
      reorderUrl: modalReorderUrl,
      weekday: modalWeekday,
      quantity: modalQuantity,
    };

    onSubmit(newObjectData);
  };

  const handleAlertClose = () => {
    onAlertClose();
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose}>
      <ModalContent className="py-4">
        <ModalHeader>
          <h2 className="text-2xl font-semibold">Create New Object</h2>
        </ModalHeader>
        <ModalBody>
          {alertMessage && (
            <Alert
              color="danger"
              description={alertMessage}
              title="Error"
              onClose={handleAlertClose}
            />
          )}
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
              selectedKeys={modalCategoryId ? [modalCategoryId.toString()] : []}
              onSelectionChange={(keys) => {
                const selectedId = Array.from(keys)[0];

                setModalCategoryId(Number(selectedId));
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
                modalWeekday !== null ? [modalWeekday.toString()] : []
              }
              onSelectionChange={(keys) => {
                const selectedId = Array.from(keys)[0];

                setModalWeekday(Number(selectedId));
              }}
            >
              <SelectItem key="0" value="0">
                Monday
              </SelectItem>
              <SelectItem key="1" value="1">
                Tuesday
              </SelectItem>
              <SelectItem key="2" value="2">
                Wednesday
              </SelectItem>
              <SelectItem key="3" value="3">
                Thursday
              </SelectItem>
              <SelectItem key="4" value="4">
                Friday
              </SelectItem>
              <SelectItem key="5" value="5">
                Saturday
              </SelectItem>
              <SelectItem key="6" value="6">
                Sunday
              </SelectItem>
            </Select>
            <div className="flex gap-4">
              <Input
                isRequired
                label="Quantity"
                min={1}
                type="number"
                value={modalQuantity.toString()}
                onValueChange={(value) => setModalQuantity(Number(value))}
              />
            </div>
            <Input
              isRequired
              required
              label="Reorder URL"
              placeholder="https://example.com/reorder"
              value={modalReorderUrl}
              onValueChange={(value) => setModalReorderUrl(value)}
            />
            <div className="flex justify-center mt-4">
              <Button color="primary" isDisabled={!canCreate} type="submit">
                Create
              </Button>
            </div>
          </Form>
        </ModalBody>
      </ModalContent>
    </Modal>
  );
}
