# DevOps CI/CD Project — Student Feedback Portal

A small Java web application used as the deploy target for a Jenkins CI/CD pipeline:
**Git &rarr; Maven build/test &rarr; package WAR &rarr; deploy to Tomcat &rarr; health check &rarr; rollback on failure.**

## What's included

| Path | Purpose |
|---|---|
| `pom.xml` | Maven build - compiles, tests, packages the app as a WAR |
| `src/main/java/com/feedback/` | Servlets: form submission (`/submit`) and health check (`/health`) |
| `src/main/webapp/` | JSP pages: home, feedback form, success page |
| `src/test/java/` | JUnit 5 tests for the in-memory feedback store |
| `Jenkinsfile` | Declarative pipeline: checkout, build, test, package, archive, backup, deploy, verify, rollback-on-failure |
| `data/sample-feedback.csv` | 20 rows of sample data (optional, for demo richness) |
| `docs/architecture.md` | Diagram, quality gates, branching model |
| `docs/setup-guide.md` | Step-by-step install of JDK/Maven/Git/Jenkins/Tomcat and pipeline setup |
| `docs/rollback-runbook.md` | Three failure/recovery demo scripts (failing test, bad deploy, git revert) |

## Local build (no Jenkins needed yet)

```bash
mvn clean
mvn test
mvn package
```
The WAR appears at `target/student-feedback-portal.war`.

## Full pipeline setup

See `docs/setup-guide.md` for installing Jenkins and Tomcat and wiring up the pipeline job,
and `docs/rollback-runbook.md` for the three failure/rollback demos required by the
project's Phase 6.

## Notes

- Data is stored in memory (`FeedbackStore`) - it resets on redeploy. That's intentional;
  no database is required for this project.
- Requires **Tomcat 10.1+** because the app targets the Jakarta EE 6.0 servlet namespace.
  If your environment only has Tomcat 9, downgrade the `jakarta.servlet-api` dependency
  in `pom.xml` to `javax.servlet:javax.servlet-api:4.0.1` and change `jakarta.servlet.*`
  imports to `javax.servlet.*` in the two servlet classes.
