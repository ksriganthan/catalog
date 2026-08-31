# Catalog Service

Produktkatalog-Microservice des E-Commerce-Systems aus dem Modul
**Agile Application Lifecycle Management**
FHNW Hochschule für Wirtschaft, Olten

Dieser Service stellt eine REST-API für die Buchsuche bereit und wird vom
Order-Service konsumiert.

| Teil | Repository |
|---|---|
| Projektübersicht | [ksriganthan/Agile_Application_Lifecycle_Management](https://github.com/ksriganthan/Agile_Application_Lifecycle_Management) |
| Catalog-Service | dieses Repository |
| Order-Service | [mladen98/order](https://github.com/mladen98/order) |

---

## Rolle im System

Polyrepo-Architektur: Beide Services liegen in eigenen Repositories, sind
getrennt deploybar und kommunizieren ausschliesslich über REST.

```
  Browser
     │
     ▼
┌──────────────┐   GET /books/search?keyword=…   ┌──────────────┐
│ Order-Service│ ──────────────────────────────▶ │Catalog-Service│
│  Port 8081   │ ◀────────────── JSON ────────── │  Port 8080   │
│  Web-UI      │                                  │  REST-API   │
└──────────────┘                                  └──────┬───────┘
                                                         ▼
                                                  ┌──────────────┐
                                                  │  PostgreSQL  │
                                                  └──────────────┘
```

Der Catalog-Service kennt den Order-Service nicht – die Abhängigkeit läuft nur
in eine Richtung.

---

## API

Basis-Pfad: `/books`

### `GET /books/search`

Sucht Bücher über einen oder mehrere Suchbegriffe.

| Parameter | Typ | Pflicht | Bemerkung |
|---|---|---|---|
| `keyword` | Liste von Strings | nein | mehrfach angebbar; ohne Parameter werden alle Bücher zurückgegeben |

Die Suche ist **case-insensitive** und **UND-verknüpft**: Ein Buch wird nur
zurückgegeben, wenn *jeder* Suchbegriff vorkommt. Durchsucht werden ISBN, Titel,
Beschreibung und die zugeordneten Autoren gemeinsam als ein Textfeld.

```bash
curl "http://localhost:8080/books/search?keyword=harry&keyword=rowling"
```

```json
[
  {
    "isbn": "9783551557414",
    "title": "Harry Potter und der Stein der Weisen",
    "description": "Harry Potter Teil 1",
    "authors": [ { "name": "Joanne K.", "surname": "Rowling" } ]
  }
]
```

Zusätzlich sind über Spring Boot Actuator sämtliche Endpunkte unter `/actuator`
freigeschaltet, unter anderem `/actuator/health`.

---

## Datenmodell

```
┌────────────────────────┐        ┌────────────────────────┐
│ Book                   │        │ Author                 │
├────────────────────────┤   n:m  ├────────────────────────┤
│ ISBN  (PK, String)     │◀──────▶│ authorId (PK, int)     │
│ Titel        not null  │        │ Name         not null  │
│ Beschreibung not null  │        │ Nachname     not null  │
└────────────────────────┘        └────────────────────────┘
```

Die Many-to-Many-Beziehung wird von `Author` aus verwaltet; `Book` bindet die
Autoren über `mappedBy` mit `FetchType.EAGER` ein, damit sie in der Suchantwort
direkt enthalten sind.

Das Schema entsteht über `ddl-auto=update`; Migrationen gibt es nicht.

### Beispieldaten

Der `DataInitializer` legt beim Start **6 Bücher und 4 Autoren** an – aber nur,
wenn die Tabelle leer ist. Enthalten sind unter anderem zwei Harry-Potter-Bände,
„ES" und drei Titel von Sebastian Fitzek und Guillaume Musso. Damit lässt sich
die Suche ohne eigene Datenerfassung ausprobieren.

---

## Starten

### Voraussetzungen

- JDK 21 und Maven (Wrapper `mvnw` liegt bei)
- eine erreichbare PostgreSQL-Datenbank

### Lokal

Datenbank bereitstellen, zum Beispiel:

```bash
docker run --name catalog-postgres -e POSTGRES_USER=catalog -e POSTGRES_PASSWORD=catalog -e POSTGRES_DB=catalog -p 5432:5432 -d postgres:16
```

Anschliessend:

```bash
./mvnw spring-boot:run
```

Der Service läuft auf `http://localhost:8080`.

### Als Container

```bash
docker build -t catalog .
```

Das Dockerfile baut in zwei Stufen (Maven/JDK 21 → JRE 21) und überspringt dabei
die Tests.

### Gesamtsystem

Eine `docker-compose.yml`, die PostgreSQL, den Catalog- und den Order-Service
zusammen startet, liegt im [Order-Repository](https://github.com/mladen98/order).
Sie zieht die Images von Docker Hub statt sie lokal zu bauen.

---

## Konfiguration

| Property | Standard | Bemerkung |
|---|---|---|
| `spring.datasource.url` | `jdbc:postgresql://localhost:5432/catalog` | im Compose-Setup über `SPRING_DATASOURCE_URL` auf den Service-Namen `postgres` umgebogen |
| `spring.datasource.username` / `.password` | `catalog` / `catalog` | siehe Einschränkungen |
| `spring.jpa.hibernate.ddl-auto` | `update` | |
| `spring.jpa.show-sql` | `true` | |
| `management.endpoints.web.exposure.include` | `*` | alle Actuator-Endpunkte offen |

---

## Tests

```bash
./mvnw test
```

| Ebene | Test | Werkzeug |
|---|---|---|
| Unit | `BookServiceTest` | JUnit 5, Mockito |
| Repository | `BookRepositoryTest` | Spring Data JPA, H2 |
| Repository gegen echte DB | `PostgresContainerTest` | Testcontainers (PostgreSQL) |
| Controller | `BookControllerTest` | MockMvc |
| Controller-Integration | `BookControllerIntegrationTest` | voller Spring-Kontext |
| Kontext | `CatalogApplicationTests` | Smoke-Test |
| Last | `BookSimulation` | Gatling |

Die Coverage wird über **JaCoCo** erhoben. Für die Testcontainers-Tests muss
Docker laufen.

---

## Bekannte Einschränkungen

- **Nur Lesezugriff über REST.** Die API bietet ausschliesslich
  `GET /books/search`. Anlegen, Ändern und Löschen von Büchern und Autoren ist
  über die Repositories im Code möglich, aber nicht als Endpunkt exponiert.
- **Suche filtert im Arbeitsspeicher.** `BookService.searchBooks()` lädt über
  `findAll()` den gesamten Katalog und filtert anschliessend in Java. Für den
  Umfang dieses Projekts unkritisch, mit wachsendem Datenbestand aber der erste
  Punkt, den man auf eine datenbankseitige Query umstellen würde.
- **Datenbank-Zugangsdaten im Klartext** in `application.properties`. Für die
  lokale Übungsumgebung bewusst so; produktiv gehören sie in Umgebungsvariablen.
- **Alle Actuator-Endpunkte offen** (`exposure.include=*`), ohne Absicherung.
- **Kein Schema-Management.** Das Schema entsteht über `ddl-auto=update`.
- **Toter Konstruktor.** `BookService` besitzt einen parameterlosen Konstruktor,
  der das Repository sich selbst zuweist. Weil Spring das Feld per
  `@Autowired` injiziert, bleibt das folgenlos – aufräumen sollte man es
  trotzdem.
