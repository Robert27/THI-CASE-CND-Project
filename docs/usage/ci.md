# Bauautomatisierung

Das Projekt verwendet GitHub Actions für die Automatisierung der Builds und Tests. Die Konfigurationen sind in den jeweiligen Dienste sind im Ordner `.github/workflows` zu finden.
Nach erfolgreichen Build wird der Container in der GitHub Container Registry veröffentlicht. Daneben wird die Dokumentation automatisch auf GitHub Pages veröffentlicht.

## Angepasste Dockerfiles

Normalerweise wird für den Bau der Container ein Multi-Stage Dockerfile verwendet. Dieses baut im ersten Schritt die Anwendung und verwendet im zweiten Schritt ein schlankes Image und das gebaute Artefakt.

Allerdings müssen zwangsläufig zwei Images gebaut werden, da die Anwendung auf unterschiedlichen Architekturen (ARM & AMD) laufen soll. Würde man hier das normale Multi-Stage Dockerfile verwenden, würden sämliche Schritte doppelt ausgeführt werden und die Bauzeit durch die langsame ARM Emulation unnötig verlängert werden.

Daher wird für den GitHub Actions Build ein eigenes Dockerfile verwendet, das nur das finale Image auf der jeweiligen Architektur bereitstellt. Das eigentliche Installieren der Abhängigkeiten und das Bauen der Anwendung wird in der GitHub Actions Konfiguration durchgeführt.
Dies hat den Vorteil, dass die Bauzeit von etwa 10 Minuten auf etwa 2 Minuten reduziert wird. Ebenso können durch das vorgezogene Installieren der Abhängigkeiten die GitHub Cache Funktionen genutzt werden, um die Bauzeit weiter zu reduzieren.

## Zugriff auf die Container Registry

Die bereitgestellten Container können über die GitHub Container Registry bezogen werden. Dazu muss sich der Nutzer mit seinem GitHub Account anmelden und die Zugriffsrechte für das Repository besitzen.

Die einzelnen Schritt zur Authentifizierung und zum Bezug der Container sind in den jeweiligen Dokumentation zu [Docker](/usage/docker#vorgebaute-container-verwenden) und [Kubernetes](/usage/kubernetes#container-registry-einrichtung) beschrieben.
