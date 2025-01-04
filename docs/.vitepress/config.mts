import { defineConfig } from 'vitepress'

// https://vitepress.dev/reference/site-config
export default defineConfig({
  title: "SmartOrder Wiki",
  description: "Ein THI CSE CND Projekt",
  cleanUrls: true,
  lang: "de-DE",
  themeConfig: {
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
  },
})
