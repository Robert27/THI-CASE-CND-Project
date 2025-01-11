// eslint-disable-next-line @typescript-eslint/no-unused-vars, unused-imports/no-unused-imports
import NextAuth, { User } from "next-auth";

declare module "next-auth" {
  interface Session {
    accessToken?: string;
    user?: User;
  }

  interface User {
    sub: number;
    username: string;
  }
}
