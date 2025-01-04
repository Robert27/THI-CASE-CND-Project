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
      label: "Bestellungen",
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
    github: "https://github.com/nextui-org/nextui",
    docs: "https://case.eggl.dev",
  },
};
