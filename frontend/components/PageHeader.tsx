interface PageHeaderProps {
  title: string;
  description?: string;
}

export default function PageHeader({ title, description }: PageHeaderProps) {
  return (
    <div className="flex flex-col items-center mb-8">
      <h1 className="text-3xl font-semibold text-primary">{title}</h1>
      {description && (
        <p className="mt-2 text-m text-default-500">{description}</p>
      )}
    </div>
  );
}
