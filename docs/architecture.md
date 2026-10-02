# Architecture

## High-level flow

```
Developer
   | git push
   v
Git Repository (main / dev / feature/*)
   | webhook or poll
   v
Jenkins Pipeline
   |-- 1. Checkout
   |-- 2. Build            (mvn clean compile)
   |-- 3. Test             (mvn test)
   |-- 4. Package          (mvn package -> WAR)
   |-- 5. Archive Artifact (Jenkins keeps the WAR)
   |-- 6. Backup current deployment
   |-- 7. Deploy to Tomcat
   |-- 8. Verify (curl /health)
   v
Apache Tomcat
   v
Application URL (browser / curl)
```

## Quality gates

| Gate | Behavior |
|---|---|
| Unit test failure | Pipeline stops before packaging - nothing gets deployed |
| WAR not produced | `Package` stage fails, pipeline stops |
| Health check fails after deploy | Pipeline marks the build failed and the `post { failure }` block restores the previous WAR from `/opt/tomcat/backups` |

## Components

- **Git** - source of truth, branch model below.
- **Jenkins** - orchestrates checkout -> build -> test -> package -> deploy -> verify.
- **Maven** - compiles, runs tests, packages the WAR (`target/student-feedback-portal.war`).
- **Tomcat** - hosts the deployed WAR under `/opt/tomcat/webapps/`.
- **HealthServlet (`/health`)** - the endpoint Jenkins curls after deploy to confirm the app started correctly.

## Branching model

- `main` - stable, deployable code only.
- `dev` - integration branch for merged features.
- `feature/<name>` - short-lived branches for individual changes.

Commit convention: `feat: ...`, `fix: ...`, `ci: ...`, `docs: ...`.
