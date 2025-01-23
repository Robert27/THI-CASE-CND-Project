import { DefaultTheme, defineConfig } from "vitepress";

// https://vitepress.dev/reference/site-config
export default defineConfig({
  title: "SmartOrder Wiki",
  description: "Ein THI CSE CND Projekt",
  cleanUrls: true,
  vite: {
    assetsInclude: ["**/*.PNG", "**/*.png"],
  },
  base: process.env.BASE_URL,
  lang: "de-DE",
  themeConfig: {
    nav: [
      { text: "Dokumentation", link: "/services/overview" },
      { text: "Verwendung", link: "/usage/docker" },
    ],
    search: {
      provider: "local",
      options: {
        translations: {
          button: {
            buttonText: "Suchen",
            buttonAriaLabel: "Suchen",
          },
          modal: {
            displayDetails: "Details anzeigen",
            resetButtonTitle: "Zurücksetzen",
            backButtonTitle: "Zurück",
            noResultsText: "Keine Ergebnisse gefunden",
            footer: {
              selectText: "Auswählen",
              navigateText: "Navigieren",
              closeText: "Schließen",
            },
          },
        },
      },
    },
    docFooter: {
      prev: "Vorherige Seite",
      next: "Nächste Seite",
    },
    outline: {
      label: "Auf dieser Seite",
    },
    returnToTopLabel: "Nach oben zurückkehren",
    sidebarMenuLabel: "Seitenmenü",
    darkModeSwitchLabel: "Erscheinungsbild",
    lightModeSwitchTitle: "Zu hellem Modus wechseln",
    darkModeSwitchTitle: "Zu dunklem Modus wechseln",
    socialLinks: [
      {
        icon: "github",
        link: "https://github.com/Robert27/THI-CASE-CND-Project",
      },
    ],
    sidebar: {
      "/services/": { base: "/services/", items: sidebarServices() },
      "/usage/": { base: "/usage/", items: sidebarUsage() },
    },
  },
});

function sidebarServices(): DefaultTheme.SidebarItem[] {
  return [
    {
      text: "Architektur",
      collapsed: false,
      items: [
        { text: "Aufbau", link: "overview" },
        { text: "Limitationen", link: "limitations" },
        { text: "Sicherheit", link: "security" },
      ],
    },
    {
      text: "Services",
      collapsed: false,
      items: [
        {
          text: "Robert Eggl",
          items: [
            { text: "Objekt Verwaltung", link: "object-management" },
            { text: "Intervall Monitoring", link: "interval-monitor" },
            { text: "Frontend", link: "frontend" },
          ],
        },
        {
          text: "Andreas Zieltrum",
          items: [
            { text: "Nutzerverwaltung", link: "user-management" },
            { text: "Authentifizierung", link: "authentication" },
          ],
        },
        {
          text: "Leonie Rößler",
          items: [
            { text: "Preisüberwachung", link: "price-check" },
            { text: "Bestellungsverwaltung", link: "order-management" },
            { text: "URL-Validierung", link: "url-validation" },
          ],
        },
      ],
    },
    { text: "Dokumentation", link: "docs" },
  ];
}

function sidebarUsage(): DefaultTheme.SidebarItem[] {
  return [
    {
      text: "Verwendung",
      collapsed: false,
      items: [
        { text: "Docker Compose", link: "docker" },
        { text: "Kubernetes", link: "kubernetes" },
        { text: "Ohne Docker", link: "local" },
      ],
    },
    {
      text: "Weitere Informationen",
      collapsed: false,
      items: [
        { text: "Konfiguration", link: "configuration" },
        { text: "Bauautomatisierung", link: "ci" },
      ],
    },
  ];
}
