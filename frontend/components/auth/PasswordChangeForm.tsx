import { useState } from "react";
import { Input, Button, Spinner } from "@heroui/react";
import { LuChevronRight } from "react-icons/lu";

interface PasswordChangeFormProps {
  onSubmit: (oldPassword: string, password: string) => void;
  isPending: boolean;
}

export const PasswordChangeForm: React.FC<PasswordChangeFormProps> = ({
  onSubmit,
  isPending,
}) => {
  const [oldPassword, setOldPassword] = useState("");
  const [password, setPassword] = useState("");
  const [passwordConfirm, setPasswordConfirm] = useState("");

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    onSubmit(oldPassword, password);
  };

  const inputClasses = {
    // https://stackoverflow.com/questions/78637238/change-color-textarea-nextui
    label: "text-black/50 dark:text-white/90",
    input: [
      "text-black/90 dark:text-white/90",
      "placeholder:text-default-700/50 dark:placeholder:text-white/60",
    ],
    innerWrapper: "bg-transparent",
    inputWrapper: [
      "shadow-xl",
      "bg-transparent",
      "border-2",
      "border-default-500",
      "dark:border-default-400/75",
      "group-data-[focus=true]:border-default-700",
      "!cursor-text",
    ],
  };

  return (
    <form className="space-y-8" onSubmit={handleSubmit}>
      <div className="space-y-3">
        {/* Current Password Section */}
        <div>
          <h3 className="text-sm font-medium mb-2">Current Password</h3>
          <Input
            required
            classNames={inputClasses}
            label="Current Password"
            name="oldPassword"
            type="password"
            value={oldPassword}
            onChange={(e) => setOldPassword(e.target.value)}
          />
        </div>

        {/* New Password Section */}
        <div className="space-y-3">
          <h3 className="text-sm font-medium mb-2">New Password</h3>
          <Input
            required
            classNames={inputClasses}
            label="New Password"
            name="password"
            type="password"
            value={password}
            onChange={(e) => {
              setPassword(e.target.value);
            }}
          />
          <Input
            required
            classNames={inputClasses}
            errorMessage={"Passwords do not match."}
            isInvalid={passwordConfirm !== "" && passwordConfirm !== password}
            label="Confirm New Password"
            name="passwordConfirm"
            type="password"
            value={passwordConfirm}
            onChange={(e) => {
              setPasswordConfirm(e.target.value);
            }}
          />
        </div>
      </div>

      <Button
        className="w-full bg-gradient-to-r from-primary to-secondary text-white font-semibold 
           py-3 rounded-lg transition-transform hover:scale-102 active:scale-98 mt-2"
        isDisabled={
          isPending ||
          !oldPassword ||
          !password ||
          !passwordConfirm ||
          password !== passwordConfirm
        }
        type="submit"
      >
        {isPending ? (
          <Spinner color="white" size="sm" />
        ) : (
          <>
            Change Password <LuChevronRight />
          </>
        )}
      </Button>
    </form>
  );
};
