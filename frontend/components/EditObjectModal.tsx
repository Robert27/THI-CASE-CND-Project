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
  console.log("editObject", editObject);
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
              onChange={(e) =>
                setLocalEditObject({
                  ...localEditObject,
                  description: e.target.value,
                })
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
            <Select
              isRequired
              label="Weekday"
              placeholder="Select a weekday"
              selectedKeys={
                localEditObject.weekday !== null
                  ? [localEditObject.weekday.toString()]
                  : []
              }
              onSelectionChange={(keys) => {
                const selectedId = Array.from(keys)[0];

                setLocalEditObject({
                  ...localEditObject,
                  weekday: Number(selectedId),
                });
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
