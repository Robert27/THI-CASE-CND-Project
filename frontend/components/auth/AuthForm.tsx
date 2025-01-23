import { Input, Button, Form, Divider, Checkbox } from "@heroui/react";
import { LuChevronRight } from "react-icons/lu";
import Link from "next/link";
import { useState } from "react";

interface AuthFormProps {
  onSubmit: (e: React.FormEvent) => void;
  username: string;
  setUsername: (value: string) => void;
  password: string;
  setPassword: (value: string) => void;
  buttonText: string;
  isPending: boolean;
  bottomText: string;
  bottomLinkText: string;
  bottomLinkHref: string;
  isRegister?: boolean;
  termsLink?: string; // Optional link to terms page
}

export const AuthForm: React.FC<AuthFormProps> = ({
  onSubmit,
  username,
  setUsername,
  password,
  setPassword,
  buttonText,
  isPending,
  bottomText,
  bottomLinkText,
  bottomLinkHref,
  isRegister,
}) => {
  const [passwordConfirm, setPasswordConfirm] = useState("");
  const [acceptedTerms, setAcceptedTerms] = useState(false);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit(e);
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
    <>
      <Form
        className="-z-40"
        validationBehavior="native"
        onSubmit={handleSubmit}
      >
        <div className="space-y-4 w-full flex-col">
          <Input
            required
            classNames={inputClasses}
            id="username"
            label="Username"
            name="username"
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
          {isRegister && <Divider />}

          <Input
            required
            classNames={inputClasses}
            id="password"
            label="Password"
            name="password"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          {isRegister && (
            <>
              <Input
                required
                classNames={inputClasses}
                errorMessage={"Passwords do not match."}
                id="passwordConfirm"
                isInvalid={
                  passwordConfirm !== "" && passwordConfirm !== password
                }
                label="Confirm Password"
                name="passwordConfirm"
                type="password"
                value={passwordConfirm}
                onChange={(e) => setPasswordConfirm(e.target.value)}
              />
              <div className="mt-4 flex items-center gap-1">
                <Checkbox
                  isRequired
                  checked={acceptedTerms}
                  onChange={(e) => setAcceptedTerms(e.target.checked)}
                >
                  I agree to the
                </Checkbox>
                <Link
                  className="text-primary hover:underline"
                  href={
                    "https://github.com/Robert27/THI-CASE-CND-Project/blob/main/LICENSE"
                  }
                  target="_blank"
                >
                  terms and conditions
                </Link>
              </div>
            </>
          )}
        </div>
        <Button
          className="w-full bg-gradient-to-r from-primary to-secondary text-white font-semibold 
             py-3 rounded-lg transition-transform hover:scale-102 active:scale-98 mt-2"
          isDisabled={
            isPending ||
            (isRegister && password !== passwordConfirm) ||
            (isRegister && !acceptedTerms) ||
            !username ||
            !password
          }
          isLoading={isPending}
          type="submit"
        >
          {buttonText} {isPending && <LuChevronRight />}
        </Button>
      </Form>
      <div className="text-center text-sm text-foreground-500 pt-8">
        {bottomText}{" "}
        <Link
          className="text-primary hover:underline transition-all font-medium"
          href={bottomLinkHref}
        >
          {bottomLinkText}
        </Link>
      </div>
    </>
  );
};
