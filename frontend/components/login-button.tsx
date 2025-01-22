"use client";
import {
  Avatar,
  Button,
  Dropdown,
  DropdownItem,
  DropdownMenu,
  DropdownTrigger,
  Link,
} from "@heroui/react";
import { signOut, useSession } from "next-auth/react";
import { useRouter } from "next/navigation";
import React from "react";
import { LuLogOut, LuUserCog } from "react-icons/lu";

export default function LoginButton() {
  const { data: session, status } = useSession();
  const router = useRouter();

  const handleLogout = () => {
    signOut({ redirect: false });
    localStorage.removeItem("next-auth.session-token");
    router.push("/");
  };

  return status === "authenticated" ? (
    <Dropdown>
      <DropdownTrigger>
        <Avatar
          isBordered
          alt="User"
          className="text-white"
          color="primary"
          name={session?.user?.username}
          size="sm"
        />
      </DropdownTrigger>
      <DropdownMenu>
        <DropdownItem
          key="profile"
          endContent={<LuUserCog size={22} />}
          href="/account"
        >
          Profile
        </DropdownItem>
        <DropdownItem
          key="logout"
          className="text-danger"
          color="danger"
          endContent={<LuLogOut size={24} />}
          onPress={() => handleLogout()}
        >
          Logout
        </DropdownItem>
      </DropdownMenu>
    </Dropdown>
  ) : (
    <Link href={"login"}>
      <Button>Login</Button>
    </Link>
  );
}
