# Bestellungsverwaltung

<!-- Content for Bestellungsverwaltung -->
Bestelllistenservice 
Autor: Leonie Rößler
Architektur: Hexagonal
Technologie: Quarkus (Java)


Anlegen von Bestellobjekten in einer Bestellliste, Ausgabe Bestellliste für User, Durchführen von Bestellungen, Löschen ("Stornieren") von Bestellungen

Adapter:
Inbound:
GRPC: OrderListGrpcService: generieren von Datenbankobjekten / Bestellobjekten mit UserId, ItemId
REST: getOpenOrders, performOrder, abortOrder (JWT)

Outbound:
GRPC: Obj.Management, Price Check
jpa: Bestellobjekte speichern
REST: Order execution 


Ports:

Domain: 
