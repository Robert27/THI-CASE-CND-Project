export type SiteConfig = typeof siteConfig;
const httpHost = process.env.NEXT_PUBLIC_HTTP_HOST ?? "";

export const siteConfig = {
  name: "SmartOrder",
  description: "Haushaltsverwaltung und Nachbestellung",
  navItems: [
    {
      label: "Objects",
      href: "/objects",
    },
    {
      label: "Orders",
      href: "/orders",
    },
  ],
  navMenuItems: [
    {
      label: "Objects",
      href: "/objects",
    },
    {
      label: "Bestellungen",
      href: "/orders",
    },
  ],
  links: {
    github: "https://github.com/Robert27/THI-CASE-CND-Project",
    docs: httpHost + "/docs",
  },
};
