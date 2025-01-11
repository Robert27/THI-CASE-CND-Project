# Intervall Monitoring

Der Intervall Monitoring Service dient der Überwachung des Bestellzyklus.
Der Service wird in einem festen Intervall ausgeführt und prüft, ob eine Bestellung an dem aktuellen Tag fällig ist. Ist dies der Fall, wird der Bestellservice darüber informiert.

- **Autor**: Robert Eggl
- **Architektur**: n/a
- **Technologie**: Quarkus (Java)

## Architekturbeschreibung

Der **Intervall-Monitoring-Service** ist ein Dienst zur Überwachung des Bestellzyklus.

### Funktionsweise

Der Service kommuniziert mit dem **Nutzerverwaltungsservice**, um eine Liste aller Nutzer zu erhalten. Diese Liste wird anschließend mit der Log-Datenbanktabelle abgeglichen. Dadurch werden nur die Nutzer berücksichtigt, die am aktuellen Tag noch keine Bestellung getätigt haben.

Die nachfolgenden Schritte erfolgen in **Batch-Operationen**, um die Belastung der verbundenen Services zu minimieren:

1. Für die verbleibenden Nutzer wird über den **Objekt-Management-Service** geprüft, ob eine Bestellung fällig ist.
2. Die Liste der fälligen Bestellungen wird pro Nutzer an den **Bestellservice** übergeben.
3. Bei Eingang einer Bestellbestätigung wird der jeweilige Nutzer in der Log-Datenbanktabelle als „für den heutigen Tag bestellt“ markiert.

### Warum wird die Nutzerliste abgefragt?

Es stellt sich die Frage, warum nicht direkt alle heutigen Bestellungen abgefragt und anschließend durch die Logs gefiltert werden.  
Der Grund liegt in der Kombination aus **vorherigem Filtern** und **Batch-Operationen**, wodurch:

- die Belastung der verbundenen Services reduziert wird, und
- eine stärkere Entkopplung zwischen den Services erreicht wird.

Diese Architektur stellt sicher, dass der Prozess effizient und skalierbar bleibt.

## Start ohne Docker

Der Service kann ohne Docker gestartet werden. Dazu muss die Anwendung lokal gebaut und gestartet werden. Allerdings erfordert dieser Dienst eine hohe Abhängigkeit von anderen Services und der Datenbank. Die Schritte der anderen Services müssen daher ebenfalls ausgeführt werden, andernfalls wird der Service nicht ordnungsgemäß funktionieren.

Gehen Sie wie folgt vor:

1. **Voraussetzungen**: Stellen Sie sicher, dass Maven und eine Java 21 JDK installiert sind.

2. **Projekt klonen**: Klonen Sie das Repository auf Ihren lokalen Rechner.

   ```sh
   git clone https://github.com/roberteggl/THI-CASE-CND-Projekt.git
   cd THI-CASE-CND-Projekt/interval-monitor
   ```

3. **Maven Build**: Führen Sie den Maven-Build aus, um die Anwendung zu erstellen.

   ```sh
   mvn package
   ```

4. **Anwendung starten**: Starten Sie die Anwendung mit dem folgenden Befehl:

   ```sh
   java -Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager -jar target/quarkus-app/quarkus-run.jar
   ```

5. **Zugriff auf die Anwendung**: Die Anwendung ist nun unter `http://localhost:8080` erreichbar.
