export type SiteConfig = typeof siteConfig;

export const siteConfig = {
  name: "SmartOrder",
  description: "Haushaltsverwaltung und Nachbestellung",
  navItems: [
    {
      label: "Home",
      href: "/",
    },
    {
      label: "Docs",
      href: "https://case.eggl.dev",
    },
    {
      label: "Objects",
      href: "/objects",
    },
    {
      label: "Bestellungen",
      href: "/orders",
    },
  ],
  navMenuItems: [
    {
      label: "Home",
      href: "/",
    },
    {
      label: "Docs",
      href: "https://case.eggl.dev",
    },
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
    github: "https://github.com/nextui-org/nextui",
  },
};
