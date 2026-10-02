# Rollback Runbook

Three failure/recovery cases to demonstrate for the assignment.

## Case A - Failing unit test blocks deploy

1. Edit `FeedbackStoreTest.java` and change an assertion so it's intentionally wrong,
   e.g. `assertEquals(before + 2, store.count());`.
2. Commit and push to the branch Jenkins watches.
3. Run the pipeline. Expected result: the **Test** stage fails, the pipeline stops,
   and nothing after `Package` runs - the previous deployment on Tomcat is untouched.
4. Revert the change and push again to confirm the pipeline goes green.

## Case B - Bad deploy triggers automatic rollback

1. Temporarily break the health check on purpose - e.g. rename `/health` to `/health2`
   in `HealthServlet.java`'s `@WebServlet` annotation, or stop Tomcat right before
   the `Verify` stage runs.
2. Run the pipeline. The `Deploy to Tomcat` stage succeeds but `Verify (Health Check)`
   fails, which triggers `error(...)`.
3. The `post { failure }` block in the Jenkinsfile runs automatically and restores
   `/opt/tomcat/backups/student-feedback-portal.war.bak` over the broken deployment.
4. Confirm recovery: `curl http://localhost:8081/student-feedback-portal/health`
   should return `200 OK` again, serving the previous good version.
5. Undo the intentional break and re-run to confirm a clean deploy.

## Case C - Git-level rollback

1. Identify the last known-good commit: `git log --oneline`.
2. Revert the bad commit: `git revert <bad-commit-sha>` (or `git reset --hard <good-sha>`
   followed by a force-push on a feature branch - never force-push `main`).
3. Push the revert.
4. Re-run the Jenkins pipeline against the reverted commit and confirm it deploys
   and passes health check.

## Evidence to capture for the report

- Console output / screenshot of the failing pipeline for Case A and Case B.
- Console output / screenshot of the successful rollback and re-verification.
- `git log` output showing the revert commit for Case C.
