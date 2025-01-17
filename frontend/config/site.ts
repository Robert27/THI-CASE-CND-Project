export type SiteConfig = typeof siteConfig;

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
    docs: "/docs",
  },
};
