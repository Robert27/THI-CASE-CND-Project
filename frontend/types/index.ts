import { SVGProps } from "react";

export type IconSvgProps = SVGProps<SVGSVGElement> & {
  size?: number;
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
