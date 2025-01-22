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
    icon: 🧑‍💻
    details: Ein hexagonaler Service zum Erstellen, Bearbeiten und Löschen des Nutzerkontos.
    link: /services/user-management
  - title: Authentifizierung
    icon: 🔐
    details: Ein hexagonaler Service zur Authentifizierung und Bereitstellung des JWT Tokens.
    link: /services/authentication
  - title: Objektverwaltung
    icon: 📦
    details: Ein hexagonaler Service zum Erstellen, Bearbeiten und Löschen von Objekten.
    link: /services/object-management
  - title: Intervall Monitoring
    icon: 🕒
    details: Ein Service zur Prüfung von Intervallen und benachrichtigen des Bestellservices.
    link: /services/interval-monitor
  - title: URL Validierung
    icon: 🔗
    details: Ein Service zur Validierung der Produkt-URLs, bei Erstellung von Objekten.
    link: /services/url-validation
  - title: Preisüberwachung
    icon: 💰
    details: Ein Service zur Überwachung der Produktpreise, zur Bereitstellung des aktuellen Preises.
    link: /services/price-check
  - title: Bestellverwaltung
    icon: 📝
    details: Ein Service zum Zusammenstellen und Bestellen der anstehenden Objekten.
    link: /services/order-management
  - title: Frontend
    icon: 🖥️
    details: Ein React Frontend zur Verwendung der Services. Es implementiert sämtliche Funktionen.
    link: /services/frontend
---
