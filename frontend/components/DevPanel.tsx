import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { LuCog } from "react-icons/lu";
import { Card, CardBody, CardHeader, Input } from "@heroui/react";

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
    <Card
      className="max-w-xl w-full mt-8 backdrop-blur-xl bg-default-100/30"
      isDisabled={!isEnabled}
    >
      <CardHeader className="flex pb-1">
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
        <div className="flex flex-col gap-3">
          <Input
            className="w-64 justify-center"
            classNames={{
              // https://stackoverflow.com/questions/78637238/change-color-textarea-nextui
              label: "text-black/50 dark:text-white/90",
              input: [
                "text-black/90 dark:text-white/90",
                "placeholder:text-default-700/50 dark:placeholder:text-white/60",
              ],
              innerWrapper: "bg-transparent",
              inputWrapper: [
                "shadow-xl",
                "dark:bg-default/60",
                "backdrop-blur-xl",
                "backdrop-saturate-200",
                "group-data-[focus=true]:bg-default-200/50",
                "dark:group-data-[focus=true]:bg-default/60",
                "!cursor-text",
              ],
            }}
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
