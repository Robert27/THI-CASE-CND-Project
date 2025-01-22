import { Input, Button, Spinner } from "@heroui/react";
import { LuChevronRight } from "react-icons/lu";
import Link from "next/link";

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
}) => {
  return (
    <>
      <form className="space-y-8" onSubmit={onSubmit}>
        <div className="space-y-4">
          <Input
            required
            className="hover:scale-101 transition-transform"
            classNames={{
              input: "text-base",
              inputWrapper: "py-2",
            }}
            id="username"
            name="username"
            placeholder="Username"
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
          <Input
            required
            className="hover:scale-101 transition-transform"
            classNames={{
              input: "text-base",
              inputWrapper: "py-2",
            }}
            id="password"
            name="password"
            placeholder="Password"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>
        <Button
          aria-disabled={isPending}
          className="w-full bg-gradient-to-r from-primary to-secondary text-white font-semibold 
             py-3 rounded-lg transition-transform hover:scale-102 active:scale-98 mt-2"
          type="submit"
        >
          {isPending ? (
            <Spinner color="white" size="sm" />
          ) : (
            <>
              {buttonText} <LuChevronRight />
            </>
          )}
        </Button>
      </form>
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
