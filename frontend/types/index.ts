import { SVGProps } from "react";

export type IconSvgProps = SVGProps<SVGSVGElement> & {
  size?: number;
};

export type StorageObjectResponse = {
  items: StorageObject[];
  totalItems: number;
  hasMore: boolean;
  nextPageToken: null | string;
};

export type StorageObject = {
  id: number;
  name: string;
  description: string;
  categoryId: number;
  reorderUrl: string;
  weekday: number;
  quantity: number;
};

export type Category = {
  id: number;
  name: string;
  description: string;
};

export type EditObjectModalProps = {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (updatedObjectData: StorageObject) => void;
  categories: Category[];
  editObject: StorageObject | null;
};

export interface Order {
  itemId: number;
  itemName: string;
  url: string;
  description: string;
  price: number | null;
  orderQuantity: number;
  availabilityQuantity: number;
  cycleDate: string;
  statusMessage: string;
}
