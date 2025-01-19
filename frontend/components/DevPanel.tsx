import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { LuCog } from "react-icons/lu";
import { Card, CardBody, CardHeader, Input } from "@nextui-org/react";

import { getMockEnabled, getMockDate, setMockDate } from "@/app/api/mock";

export default function DevPanel() {
  const queryClient = useQueryClient();

  const { data: isEnabled } = useQuery({
    queryKey: ["mockEnabled"],
    queryFn: getMockEnabled,
  });

  const { data: currentDate } = useQuery({
    queryKey: ["mockDate"],
    queryFn: getMockDate,
    enabled: isEnabled,
  });

  const mutation = useMutation({
    mutationFn: setMockDate,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["mockDate"] });
    },
  });

  return (
    <Card className="max-w-xl w-full mt-8" isDisabled={!isEnabled}>
      <CardHeader className="flex gap-3">
        <div className="flex flex-col">
          <div className="flex gap-2 items-center">
            <LuCog size={22} />{" "}
            <p className="text-md">Development Mode Panel</p>
          </div>
          {isEnabled === false && (
            <p className="text-tiny text-danger">
              Development mode is disabled via the environment variable.
            </p>
          )}
        </div>
      </CardHeader>
      <CardBody>
        <div className="flex flex-col gap-4">
          <Input
            isDisabled={!isEnabled}
            label="Mock Date"
            type="date"
            value={currentDate}
            onChange={(e) => mutation.mutate(e.target.value)}
          />
          <p className="text-tiny text-default-500">
            Modify the mock date will change the current date of the interval
            monitor service. This is useful for testing the order logic without
            waiting for the next day. Do not use this feature in production as
            it may cause unexpected behaviors.
          </p>
        </div>
      </CardBody>
    </Card>
  );
}
