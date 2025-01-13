import NextAuth from "next-auth";
import CredentialsProvider from "next-auth/providers/credentials";
import jwt, { JwtPayload } from "jsonwebtoken"; // Install this package

const AUTH_URL =
  process.env.NEXT_PUBLIC_AUTH_URL || "http://localhost:4000/auth/login";
const authOptions = {
  providers: [
    CredentialsProvider({
      name: "Credentials",
      credentials: {
        username: { label: "Username", type: "text" },
        password: { label: "Password", type: "password" },
      },
      async authorize(credentials) {
        console.log("Credentials:", credentials); // Debugging
        const res = await fetch(AUTH_URL, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(credentials),
        });

        console.log("Response:", res); // Debugging

        const user = await res.json();

        if (res.ok && user) {
          return user; // This object should include the token
        } else {
          return null;
        }
      },
    }),
  ],
  callbacks: {
    async jwt({ token, user }: { token: any; user?: any }) {
      console.log("JWT callback invoked with token and user:", token, user); // Debugging

      if (user && user.token) {
        token.accessToken = user.token;

        // Decode the token and add claims
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
      console.log("Session callback invoked with token:", token); // Debugging

      if (token) {
        session.accessToken = token.accessToken || null;

        // Add user details
        session.user = {
          username: token.username || null,
          sub: token.sub || null,
        };
      }

      console.log("Session object:", session); // Debugging

      return session;
    },
  },
  pages: {
    signIn: "/login",
    error: "/login",
  },
  secret: "your-secret-key", // Replace with a secure secret
};

export const GET = NextAuth(authOptions);
export const POST = NextAuth(authOptions);
