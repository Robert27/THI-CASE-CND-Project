import NextAuth from "next-auth";
import CredentialsProvider from "next-auth/providers/credentials";
import jwt, { JwtPayload } from "jsonwebtoken";

const AUTH_SERVICE_URL = "/rest/auth/login";

console.log("NEXTAUTH_URL:", process.env.NEXTAUTH_URL);
const authOptions = {
  providers: [
    CredentialsProvider({
      name: "Credentials",
      credentials: {
        username: { label: "Username", type: "text" },
        password: { label: "Password", type: "password" },
      },
      async authorize(credentials) {
        console.log("Credentials:", credentials);
        const baseUrl = process.env.NEXTAUTH_URL || "http://localhost";

        console.log("Base URL:", baseUrl);
        const res = await fetch(`${baseUrl}${AUTH_SERVICE_URL}`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(credentials),
        });

        console.log("Response:", res);
        const user = await res.json();

        console.log("User:", user);
        if (res.ok && user && user.token) {
          return user;
        } else {
          throw new Error(user.reason || "Failed to authenticate");
        }
      },
    }),
  ],
  callbacks: {
    async jwt({ token, user }: { token: any; user?: any }) {
      if (user && user.token) {
        token.accessToken = user.token;

        try {
          const decoded = jwt.decode(user.token) as JwtPayload;

          token.username = decoded?.username || null;
          token.sub = decoded?.sub || null;
        } catch (err) {
          console.error("Failed to decode JWT:", err);
        }
      }

      return token;
    },

    async session({ session, token }: { session: any; token: any }) {
      if (token) {
        session.accessToken = token.accessToken || null;

        session.user = {
          username: token.username || null,
          sub: token.sub || null,
        };
      }

      return session;
    },
  },
  pages: {
    signIn: "/login",
    error: "/login",
  },
  secret: process.env.NEXTAUTH_SECRET,
};

export const GET = NextAuth(authOptions);
export const POST = NextAuth(authOptions);
