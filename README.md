# Smart Building Backend

A REST API for managing a smart building: rooms, devices, sensors, automation rules and schedules. It includes JWT authentication with role-based access, an event log of every change, and a built-in sensor simulator, so the whole system can be tested without real hardware.

## Features

- **Rooms, devices and sensors** – full CRUD; devices and sensors are assigned to rooms
  - Device types: `LIGHT`, `HEATER`, `AIR_CONDITIONER`, `FAN`, `BLINDS`, `LOCK`, `OUTLET`, `SPEAKER`
  - Sensor types: `TEMPERATURE`, `HUMIDITY`, `MOTION`, `DOOR_WINDOW`
- **Automation rules** – "if sensor value is above/below a threshold, change a device's state"
  - Operators: `LT`, `LTE`, `GT`, `GTE`, `EQ`, `NEQ`
  - Rules are evaluated on every new sensor reading, with a 30-second debounce to avoid repeated triggering
- **Schedules** – change device states using cron expressions, targeted at a single device type, a room or the whole building; schedules can be switched on and off
- **Sensor simulator** – generates readings for all enabled sensors at a configurable interval
- **Event log** – records device state changes, triggered automations and executed schedules, with the source of each change (user, automation or schedule)
- **Security** – sign-up and sign-in with JWT, three roles: `ADMIN`, `BUILDING_MANAGER`, `RESIDENT`
- Global exception handling and request validation

## Architecture

The code is organised by feature (`room`, `device`, `sensor`, `automation`, `schedule`, `eventlog`, `user`, `auth`), each with its own controller, service, repository, entities and DTOs.

Automation is event-driven:

```
SensorSimulator ──(every 5 s)──► saves readings ──► SensorReadingCreatedEvent
                                                            │
                                                            ▼
                                     RuleEngine (rules cached per sensor, refreshed every 30 s)
                                                            │
                                                            ▼
                                  RuleEvaluator ──► DeviceStateService ──► EventLog
```

`ScheduleExecutor` runs every minute, finds schedules whose cron expression is due and applies them to the target devices.

## Tech stack

- Java 21, Spring Boot 4
- Spring Web MVC, Spring Security, JWT (jjwt)
- Spring Data JPA / Hibernate, JPA auditing
- MariaDB
- Lombok, Bean Validation, Maven

## API overview

All endpoints except `/api/auth/**` require a JWT token in the `Authorization: Bearer <token>` header.

| Area | Base path | Main operations |
|---|---|---|
| Auth | `/api/auth` | `POST /signup`, `POST /signin` |
| Users | `/api/users` | list, get by id, `GET /me`, update (admin) |
| Rooms | `/api/rooms` | list, get, create, delete |
| Devices | `/api/devices` | list, by room, get, create, update state, delete |
| Sensors | `/api/sensors` | list, by room, get, create, update, delete, readings |
| Automation | `/api/automation/rules` | list, get, create, update, delete |
| Schedules | `/api/schedules` | list, by room, get, create, enable/disable, delete |
| Event log | `/api/logs` | list events |

Write operations require the `ADMIN` or `BUILDING_MANAGER` role; deleting rooms, devices and sensors is limited to `ADMIN`.

## Getting started

**Requirements:** Java 21 and a running MariaDB instance.

1. Create a database:
   ```sql
   CREATE DATABASE smart_building;
   ```
2. Set the database connection and JWT settings in `src/main/resources/application.properties`.
3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

The API starts at `http://localhost:8080`. An admin account is created on startup from the `admin.username` / `admin.password` properties.

### Simulator settings

```properties
app.simulator.enabled=true
app.simulator.interval-ms=5000
```

## Example

```bash
# sign in
curl -X POST http://localhost:8080/api/auth/signin \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"<your-password>"}'

# list rooms
curl http://localhost:8080/api/rooms -H "Authorization: Bearer <token>"
```

## Planned improvements

- Docker / Docker Compose setup
- Unit and integration tests
- Live updates for clients over WebSocket
- API documentation with OpenAPI / Swagger
