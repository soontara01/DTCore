# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

DTCore is a Spring Boot 3.3.4 microservice (Java 21) serving as the Digital Trading Core API for AIS (Advertising Information System). It integrates with SAP, Azure services, Kafka, and PostgreSQL.

- Base servlet path: `/DTWS`
- Default server port: `8080` (prod), `8090` (local profile)

## Build & Run Commands

```bash
# Build
./mvnw clean package
./mvnw clean package -DskipTests        # skip tests
./mvnw clean -Pnative package           # GraalVM native image

# Run
./mvnw spring-boot:run
./mvnw spring-boot:run -Dspring-boot.run.profiles=local

# Test
./mvnw test
./mvnw test -Dtest=ClassName            # single test class
./mvnw test -Dtest=ClassName#methodName # single test method

# Coverage
./mvnw verify                           # runs JaCoCo after tests
```

## Architecture

**Layered architecture** following a consistent domain-split pattern:

```
Controller (REST) → Service (interface + impl) → Repository (DAO + interface) → DB
```

**Functional domains** under each layer:
- `cm` — configuration/master data
- `mt` — catalog management
- `pr` — pricing
- `sap` — SAP integration (largest domain, 30+ services)
- `report` — reporting
- `so` — sales/order operations

**Package conventions:**
| Layer | Package pattern |
|---|---|
| Controllers | `core.controller.impl.<domain>` |
| Service interfaces | `core.service.core.interfaces.<domain>` |
| Service implementations | `core.service.core.impl.<domain>` |
| DAO interfaces | `core.repository.interfaces.<domain>` |
| DAO implementations | `core.repository.dao.hibernate.<domain>` |
| DTOs/Beans | `core.*.<domain>.*Bean` |

**Naming conventions:**
- Controller classes: `*Impl` or `*Web` suffix
- Service impls: `*ServiceImpl`; interfaces: `*Service`
- DAO impls: `Hibernate*Dao`; interfaces: `*Dao`
- DTOs: `*Bean`
- Use `@AllArgsConstructor` + Spring DI (no `@Autowired` on fields)

## Internal Libraries

These are local Maven projects that DTCore depends on. When modifying them, install to local Maven repo before building DTCore.

| Artifact | Local path |
|---|---|
| `dt-core-lib` | `D:\VS_CODE_code_claude\DTCore-Lib` |
| `dt-queue-lib` | `D:\VS_CODE_code_claude\DTCore-Queue-Lib` |

```bash
# Install a local lib so DTCore can pick it up
cd D:\VS_CODE_code_claude\DTCore-Lib
./mvnw clean install -DskipTests

cd D:\VS_CODE_code_claude\DTCore-Queue-Lib
./mvnw clean install -DskipTests
```

## Key Technical Details

**External integrations:**
- **SAP** — multiple transaction types via REST (largest module)
- **Kafka** — SASL/SSL secured; used for async event streaming
- **Azure Storage Queue** — asynchronous job processing
- **PostgreSQL** — primary database via Hibernate 6 + JPA
- **Office 365 SMTP** — email notifications
- **Privilege & SFF services** — external AIS systems

**Profiles & config:**
- Environment configs live in `src/main/resources/config/`: `dt-config-prd.properties`, `dt-config-uat.properties`, `dt-config-sit.properties`, `dt-config-test.properties`
- Profile YAML: `application.yaml` (default) and `application-local.yaml`
- Use `-Dspring.profiles.active=local` for local development

**Native image support:**
- GraalVM native-maven-plugin is configured
- Reflection hints are registered in `DtRuntimeHints`
- Use `@RegisterReflectionForBinding` on DTOs that need reflection at native runtime

**HTTP request/response logging:**
- All requests/responses are logged via a custom `LoggingFilter` with truncation limits; do not add redundant logging middleware

**Test state:**
- Many test methods are commented out (legacy/WIP); JUnit 4 and JUnit 5 (Jupiter) are both present alongside Mockito 5
