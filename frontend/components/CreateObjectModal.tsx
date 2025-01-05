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
  const [modalInterval, setModalInterval] = useState(1); // 1 minute
  const [modalquantity, setModalQuantity] = useState(1); // 1 unit
  const [modalReorderUrl, setModalReorderUrl] = useState("");

  const canCreate =
    modalName.trim() !== "" &&
    modalDescription.trim() !== "" &&
    modalCategoryId !== null &&
    modalReorderUrl.trim() !== "" &&
    modalInterval > 0 &&
    modalquantity > 0;

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (modalCategoryId === null) {
      return;
    }
    const newObjectData = {
      name: modalName,
      description: modalDescription,
      categoryId: modalCategoryId, // Send only the ID
      reorderUrl: modalReorderUrl,
      interval: modalInterval,
      quantity: modalquantity,
    };

    onSubmit(newObjectData);
  };

  const handleAlertClose = () => {
    onAlertClose();
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose}>
      <ModalContent>
        <ModalHeader>
          <h2>Create New Object</h2>
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
            <div className="flex gap-4">
              <Input
                isRequired
                label="Interval (minutes)"
                min={1}
                type="number"
                value={modalInterval.toString()}
                onValueChange={(value) => setModalInterval(Number(value))}
              />
              <Input
                isRequired
                label="Quantity"
                min={1}
                type="number"
                value={modalquantity.toString()}
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
