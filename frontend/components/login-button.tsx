"use client";
import {
  Button,
  Dropdown,
  DropdownItem,
  DropdownMenu,
  DropdownTrigger,
  Link,
} from "@nextui-org/react";
import { signOut, useSession } from "next-auth/react";
import React from "react";
import { LuLogOut, LuUserCog, LuCircleUser } from "react-icons/lu";

export default function LoginButton() {
  const { status } = useSession();

  const handleLogout = () => {
    signOut({ redirect: false });
    localStorage.removeItem("next-auth.session-token");
  };

  return status === "authenticated" ? (
    <Dropdown>
      <DropdownTrigger>
        <LuCircleUser size={22} />
      </DropdownTrigger>
      <DropdownMenu>
        <DropdownItem
          key="profile"
          endContent={<LuUserCog size={22} />}
          href="/profile"
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
