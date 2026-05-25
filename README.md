# Imperium Maledictum — Character Creator

Web-based character creator for the **Imperium Maledictum** tabletop RPG (Warhammer 40,000 Roleplay).
Guides the player through the full character creation process and generates a printable character sheet.

## Stack

| Layer | Technology |
|---|---|
| Backend | Spring Boot 4.0.6, Java 25 |
| Persistence | PostgreSQL + Spring Data JPA (Hibernate) |
| Templates | Thymeleaf |
| Serialization | Jackson |
| Frontend | Vanilla JS, CSS Grid |

---

## Character Creation Flow

```
/ (index)
 └─ /characteristics      — roll / assign starting characteristic values
 └─ /origins              — choose origin (+5 to primary stats, pick one secondary)
 └─ /factions             — choose faction (skills, talents, inventory, choice groups)
 └─ /roles                — choose role (skills, specializations, talents, choice groups)
 └─ /details              — name, appearance, goals
 └─ /summary              — full interactive character sheet
```

All selections are accumulated in `CharacterCreationModel` stored in the Spring HTTP session via `@SessionAttributes`.

---

## Character Sheet (summary)

The sheet is split into four visual pages:

1. **Identity & Stats** — name, origin/role/faction/species, characteristics table (starting + advances + current), core stats (fate, wounds, corruption, initiative, augmentics, handedness), influence table
2. **Skills & Talents** — skills and nested specializations with advance controls, talent/trait tag cloud with autocomplete, mutations
3. **Combat** — initiative, dodge, superiority, speed; all 22 combat actions from rulebook pp. 207–210 (with tooltips); melee, ranged, and shield weapon tables; armour by location; critical wounds; injuries; equipment
4. **Notes** — appearance, divination, short/long-term goals, biography

---

## Save & Load

Characters are saved to the `character_save` table in PostgreSQL.

- **Save** — click **Save Character** on the summary page. The server reads the current `CharacterCreationModel` from the session, combines it with the summary-page edits (advances, notes, equipment, etc.) and stores the result. Returns a **6-character code** (e.g. `HK3P2W`) to share or write down.
- **Load from index** — enter the code on the main page → redirected to `/character/restore/{code}`.
- **Load from summary** — enter the code in the Load field → same redirect.
- On restore, the server deserializes the saved `CharacterCreationModel` back into the session (so all skills render correctly) and passes the summary edits to the page for automatic DOM restoration.

Save codes use the charset `ABCDEFGHJKLMNPQRSTUVWXYZ23456789` (no I, O, 0, 1 to avoid ambiguity).

---

## Database

Hibernate `ddl-auto=update` creates tables automatically on first run.

Seed data must be loaded manually after the first startup:

```sql
-- Run in order:
src/main/resources/sql/specializations_data.sql
src/main/resources/sql/roles_data.sql
src/main/resources/sql/mutations_data.sql
src/main/resources/sql/combat_actions_data.sql
```

### Key tables

| Table | Description |
|---|---|
| `characteristics` | WS, BS, STR, TGH, AG, PER, WIL, FEL, INF |
| `origin` | 8 origins with primary/secondary characteristic bonuses |
| `faction` | Factions with skills, talents, inventory, choice groups |
| `role` | Roles with skill/spec/talent/inventory choice groups |
| `skill` / `skill_specializations` | All skills and their specializations |
| `talent` | Full talent list with descriptions |
| `mutation` | Mutations with descriptions |
| `combat_action` | 22 combat actions (IM Core Rulebook pp. 207–210) |
| `inventory` / `inventory_category` | Equipment catalogue |
| `character_save` | Saved characters — `save_code` (unique, 6 chars) + `data` (JSON) |

---

## Running Locally

**Prerequisites:** Java 25, Maven, PostgreSQL running on `localhost:5432`

1. Create a database (default config uses the `postgres` database):
   ```sql
   -- nothing extra needed; Hibernate auto-creates all tables
   ```

2. Configure credentials in `src/main/resources/application.properties` if needed:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
   spring.datasource.username=postgres
   spring.datasource.password=123
   ```

3. Start the application:
   ```bash
   mvn spring-boot:run
   ```

4. Open `http://localhost:8080`

5. After the first startup, load seed data (from the SQL files above) into the running database.

---

## Project Structure

```
src/main/java/portfolio/example/im_cc/
├── controllers/
│   ├── CharacteristicsController   GET /characteristics
│   ├── OriginController            GET /origins
│   ├── FactionController           GET /factions
│   ├── RoleController              GET /roles
│   ├── DetailsController           GET /details
│   ├── CharacterCreationController POST handlers for each creation step
│   ├── SummaryController           GET /summary
│   └── CharacterSaveController     POST /character/save · GET /character/restore/{code}
├── models/                         JPA entities + CharacterCreationModel session model
├── repositories/                   Spring Data JPA repositories
└── services/                       Business logic (SummaryServiceImpl builds the sheet DTO)

src/main/resources/
├── templates/                      Thymeleaf HTML pages
├── static/css/theme.css            Shared dark/gothic CSS theme
└── sql/                            Seed data scripts
```

---

## Notes

- All game content (rules text, ability descriptions, characteristic names) is © **Games Workshop Ltd**.
- This project is a personal portfolio piece and fan tool, not for commercial use.