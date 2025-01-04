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
import { title } from "@/components/primitives";
import { Chip } from "@nextui-org/react";

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

  // Fetch categories separately
  useEffect(() => {
    fetch("http://localhost:8080/category")
      .then((res) => res.json())
      .then((data) => setCategories(data as Category[]))
      .catch((err) => console.error("Error fetching categories:", err));
  }, []);

  // useAsyncList handles fetching/sorting the objects
  const list = useAsyncList<StorageObject>({
    async load({ signal }) {
      const res = await fetch("http://localhost:8080/object", { signal });
      const data = await res.json();
      return {
        items: data, // pass the array of objects to our list
      };
    },
    async sort({ items, sortDescriptor }) {
      return {
        items: items.slice().sort((a, b) => {
          const first = a[sortDescriptor.column];
          const second = b[sortDescriptor.column];
          let cmp =
            (parseInt(first) || first) < (parseInt(second) || second) ? -1 : 1;

          if (sortDescriptor.direction === "descending") {
            cmp *= -1;
          }
          return cmp;
        }),
      };
    },
  });

  return (
    <div>
      <h1 className={title()}>Your objects</h1>
      <h2 className={'text-lg'}>Click on the column headers to sort</h2>
      <Table
        aria-label="Objects Table"
        sortDescriptor={list.sortDescriptor}
        onSortChange={(desc) => list.sort(desc)}
      >
        <TableHeader>
          <TableColumn allowsSorting>Name</TableColumn>
          <TableColumn>Description</TableColumn>
          <TableColumn allowsSorting>Category</TableColumn>
          <TableColumn>Reorder URL</TableColumn>
        </TableHeader>
        <TableBody items={list.items}>
          {(item) => (
            <TableRow key={item.id}>
              <TableCell>{item.name}</TableCell>
              <TableCell>{item.description}</TableCell>
              <TableCell>
                <Chip color="primary">
                  {categories.find((c) => c.id === item.categoryId)?.name ||
                    "Unknown"}
                </Chip>
              </TableCell>
              <TableCell>{item.reorderUrl}</TableCell>
            </TableRow>
          )}
        </TableBody>
      </Table>
    </div>
  );
}