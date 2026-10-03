# Student Feedback Portal - Jenkins CI/CD Project

This is my DevOps internship project. It's a small Java web app (student feedback portal) that I deploy to Tomcat using a Jenkins pipeline. The main thing I wanted to show is what happens when something goes wrong: a failing test should stop the pipeline, and a bad deployment should roll back to the last working version by itself.

## Why I made this

Before this, building and deploying meant doing everything by hand: build the WAR, copy it to Tomcat, check if it works. If a release was broken, the site was just down until I fixed it. With this pipeline all of that happens automatically on every push.

## What I used

- Java (Jakarta Servlets) and Maven
- Git and GitHub
- Jenkins (running on port 8080)
- Apache Tomcat (running on port 8081)
- Ubuntu on WSL

## How the pipeline works

1. **Build** - compiles the code and makes the WAR file
2. **Test** - runs the unit tests. If a test fails, it stops here and nothing gets deployed
3. **Backup** - copies the WAR that is currently running to `/opt/tomcat/backups/student-feedback-portal.war.bak`
4. **Deploy** - puts the new WAR in Tomcat's webapps folder
5. **Verify** - hits `/student-feedback-portal/health` with `curl -f`. If it doesn't return 200, the stage fails
6. **Rollback** - if the pipeline fails after the backup, it copies the `.bak` file back so the old version is live again

The health check is a small servlet (`HealthServlet.java`) that returns `OK - feedback count: N` when the app has started properly.

## What I tested

### Case A - a failing test

I added a test that fails on purpose and pushed it (build #2). The pipeline failed at the Test stage and nothing was deployed. After I reverted the commit, build #3 went green.

![Jenkins builds](screenshots/jenkins-summary.png)

### Case B - a bad deployment

This one passes the tests but breaks the app. I changed the health endpoint to return 503 and pushed it (build #4). Jenkins deployed it, the Verify stage got the 503, and then the pipeline restored the old WAR on its own. You can see it in the console log:

![Rollback log](screenshots/rollback-console.png)

This is the backup file that the rollback used:

![Backup file](screenshots/backup-file.png)

After that I reverted my change (build #5, green) and checked the health endpoint again. It returns 200:

![Health check](screenshots/health-200.png)

## Build history

| Build | What I did | Result |
|-------|-----------|--------|
| #1 | First working pipeline | Passed |
| #2 | Added a failing test | Failed at Test |
| #3 | Reverted the test | Passed |
| #4 | Made /health return 503 | Failed at Verify, rolled back |
| #5 | Reverted the health change | Passed |

## Running it

```bash
git clone https://github.com/radhakarthikeya/devops-ci-cd-project.git
cd devops-ci-cd-project
mvn clean package
```

For the pipeline, make a Jenkins pipeline job that points to this repo (it uses the Jenkinsfile in the root) and click Build Now. Tomcat needs to be running on port 8081 first.

## What I learned

- A green test run doesn't mean the deployment is healthy, so a separate health check after deploying is important.
- Taking a backup before every deploy makes automatic rollback simple.
- Keeping each failure as its own commit made it easy to show and to undo.

Radha Karthikeya
