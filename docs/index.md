---
# https://vitepress.dev/reference/default-theme-home-page
layout: home

hero:
  name: "SmartOrder Wiki"
  text: "Haushaltsverwaltung und Nachbestellung"
  tagline: Ein THI Projekt des CND Moduls
  image:
    src: logo.png
    alt: SmartOrder Logo
  actions:
    - theme: brand
      text: Architektur und Services
      link: /services/philosophy
    - theme: alt
      text: Verwendung
      link: /usage/local

features:
  - title: Nutzerverwaltung
    details: Ein hexagonaler Service zur Verwaltung des Nutzerkontos.
    link: /services/user-management
  - title: Authentifizierung
    details: Ein hexagonaler Service zur Authentifizierung des Nutzers.
    link: /services/authentication
  - title: Objektverwaltung
    details: Ein hexagonaler Service zur Verwaltung der Objekte.
    link: /services/object-management
  - title: Intervall Monitoring
    details: Ein Service zur Prüfung des Bestellzyklus.
    link: /services/interval-monitor
  - title: URL Validierung
    details: Ein Service zur Validierung der Produkt-URLs.
    link: /services/url-validation
  - title: Preisüberwachung
    details: Ein Service zur Überwachung der Produktpreise.
    link: /services/price-check
  - title: Bestellverwaltung
    details: Ein Service zur Verwaltung der Bestellungen.
    link: /services/order-management
  - title: Frontend
    details: Ein Next.js Frontend zur Verwendung der Services.
    link: /services/frontend
---
