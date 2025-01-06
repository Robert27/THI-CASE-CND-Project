"use client";

import React, { useState, useEffect } from "react";
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

import { EditObjectModalProps, StorageObject } from "@/types";

export default function EditObjectModal({
  isOpen,
  onClose,
  onSubmit,
  categories,
  editObject,
  alertMessage,
  onAlertClose,
}: EditObjectModalProps & {
  alertMessage: string | null;
  onAlertClose: () => void;
}) {
  const [localEditObject, setLocalEditObject] = useState<StorageObject | null>(
    editObject
  );

  useEffect(() => {
    setLocalEditObject(editObject);
  }, [editObject]);

  if (!localEditObject) return null;

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit(localEditObject);
  };

  const handleAlertClose = () => {
    onAlertClose();
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose}>
      <ModalContent className="py-4">
        <ModalHeader>
          <h2 className="text-2xl font-semibold">Edit Object</h2>
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
              value={localEditObject.name}
              onValueChange={(value) =>
                setLocalEditObject({ ...localEditObject, name: value })
              }
            />
            <Textarea
              required
              label="Description"
              value={localEditObject.description}
              onValueChange={(value) =>
                setLocalEditObject({ ...localEditObject, description: value })
              }
            />
            <Select
              isRequired
              label="Category"
              placeholder="Select a category"
              selectedKeys={
                localEditObject.categoryId
                  ? [localEditObject.categoryId.toString()]
                  : []
              }
              onSelectionChange={(keys) => {
                const selectedId = Array.from(keys)[0];

                setLocalEditObject({
                  ...localEditObject,
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
            <div className="flex gap-4">
              <Input
                isRequired
                label="Interval (minutes)"
                min={1}
                type="number"
                value={localEditObject.interval.toString()}
                onValueChange={(value) =>
                  setLocalEditObject({
                    ...localEditObject,
                    interval: Number(value),
                  })
                }
              />
              <Input
                isRequired
                label="Quantity"
                min={1}
                type="number"
                value={localEditObject.quantity.toString()}
                onValueChange={(value) =>
                  setLocalEditObject({
                    ...localEditObject,
                    quantity: Number(value),
                  })
                }
              />
            </div>
            <Input
              isRequired
              required
              label="Reorder URL"
              placeholder="https://example.com/reorder"
              value={localEditObject.reorderUrl}
              onValueChange={(value) =>
                setLocalEditObject({ ...localEditObject, reorderUrl: value })
              }
            />
            <div className="flex justify-center mt-4">
              <Button color="primary" type="submit">
                Save
              </Button>
            </div>
          </Form>
        </ModalBody>
      </ModalContent>
    </Modal>
  );
}
