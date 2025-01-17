interface ErrorDisplayProps {
  error: Error;
}

export default function ErrorDisplay({ error }: ErrorDisplayProps) {
  return (
    <div className="flex flex-col items-center justify-center min-h-[400px] p-4">
      <div className="text-danger text-xl font-semibold mb-2">Error</div>
      <div className="text-default-500">{error.message}</div>
    </div>
  );
}
