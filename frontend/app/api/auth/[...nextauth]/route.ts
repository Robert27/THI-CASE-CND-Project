import NextAuth from "next-auth";
import CredentialsProvider from "next-auth/providers/credentials";
import jwt, { JwtPayload } from "jsonwebtoken";

const AUTH_SERVICE_URL =
  process.env.AUTH_SERVICE_URL || "http://localhost:4000/rest/auth/login";

console.log("AUTH_SERVICE_URL:", AUTH_SERVICE_URL);
const authOptions = {
  providers: [
    CredentialsProvider({
      name: "Credentials",
      credentials: {
        username: { label: "Username", type: "text" },
        password: { label: "Password", type: "password" },
      },
      async authorize(credentials) {
        let res;

        try {
          res = await fetch(AUTH_SERVICE_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(credentials),
          });
        } catch (err) {
          console.error("Failed to authenticate:", err);
          throw new Error("Failed to authenticate");
        }
        const user = await res.json();

        if (res.ok && user && user.token) {
          try {
            const formattedKey = process.env
              .JWT_PUBLIC_KEY!.replace("-----BEGIN PUBLIC KEY-----", "")
              .replace("-----END PUBLIC KEY-----", "")
              .replace(/\\n/g, "\n");

            jwt.verify(
              user.token,
              `-----BEGIN PUBLIC KEY-----\n${formattedKey}\n-----END PUBLIC KEY-----`
            );

            return user;
          } catch (err) {
            console.error("Token verification failed:", err);
            throw new Error("Invalid token");
          }
        } else {
          console.error("Failed to authenticate:", user);
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
