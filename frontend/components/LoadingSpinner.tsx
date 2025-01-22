import { Spinner } from "@heroui/react";

export default function LoadingSpinner() {
  return (
    <div className="flex justify-center items-center min-h-[400px]">
      <Spinner size="lg" />
    </div>
  );
}
