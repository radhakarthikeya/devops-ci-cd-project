# Setup Guide (Ubuntu Linux)

## 1. Install prerequisites

```bash
sudo apt update

# JDK 17 (matches pom.xml compiler target)
sudo apt install -y openjdk-17-jdk

# Maven
sudo apt install -y maven

# Git
sudo apt install -y git
```

Verify:
```bash
java -version
mvn -version
git --version
```

## 2. Install Jenkins

```bash
curl -fsSL https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key | sudo tee \
  /usr/share/keyrings/jenkins-keyring.asc > /dev/null
echo "deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc]" \
  "https://pkg.jenkins.io/debian-stable binary/" | sudo tee \
  /etc/apt/sources.list.d/jenkins.list > /dev/null
sudo apt update
sudo apt install -y jenkins

sudo systemctl enable jenkins
sudo systemctl start jenkins
systemctl status jenkins
```

Jenkins runs on port **8080** by default. Get the initial admin password:
```bash
sudo cat /var/lib/jenkins/secrets/initialAdminPassword
```
Open `http://<server-ip>:8080`, finish the setup wizard, install the suggested plugins
plus **Pipeline** and **Maven Integration**.

## 3. Install Tomcat (use Tomcat 10.1+ - the app uses the Jakarta EE 6.0 servlet API)

```bash
sudo useradd -m -d /opt/tomcat -U -s /bin/false tomcat
cd /tmp
curl -O https://dlcdn.apache.org/tomcat/tomcat-10/v10.1.30/bin/apache-tomcat-10.1.30.tar.gz
sudo tar xzf apache-tomcat-10.1.30.tar.gz -C /opt/tomcat --strip-components=1
sudo chown -R tomcat:tomcat /opt/tomcat
```

Since Jenkins already uses 8080, set Tomcat to **8081** in `/opt/tomcat/conf/server.xml`:
```xml
<Connector port="8081" protocol="HTTP/1.1" ... />
```

Create a systemd service (`/etc/systemd/system/tomcat.service`):
```ini
[Unit]
Description=Apache Tomcat
After=network.target

[Service]
Type=forking
User=tomcat
Group=tomcat
Environment=JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
Environment=CATALINA_HOME=/opt/tomcat
ExecStart=/opt/tomcat/bin/startup.sh
ExecStop=/opt/tomcat/bin/shutdown.sh
RemainAfterExit=yes

[Install]
WantedBy=multi-user.target
```

```bash
sudo systemctl daemon-reload
sudo systemctl enable tomcat
sudo systemctl start tomcat
systemctl status tomcat
```

Give the `jenkins` user permission to write into `/opt/tomcat/webapps` and create
`/opt/tomcat/backups` (used for rollback):
```bash
sudo mkdir -p /opt/tomcat/backups
sudo usermod -aG tomcat jenkins
sudo chmod -R g+w /opt/tomcat/webapps /opt/tomcat/backups
```

## 4. First local build (before wiring up Jenkins)

```bash
cd devops-ci-cd-project
mvn clean
mvn test
mvn package
ls target/student-feedback-portal.war
```

## 5. Create the Jenkins pipeline job

1. New Item -> Pipeline -> name it `student-feedback-portal`.
2. Pipeline -> Definition: **Pipeline script from SCM**.
3. SCM: Git, point at your repository URL and branch (`main`).
4. Script Path: `Jenkinsfile` (already in the repo root).
5. Save, then **Build Now**.

## 6. Verify end to end

```bash
curl -i http://localhost:8081/student-feedback-portal/
curl -i http://localhost:8081/student-feedback-portal/health
```

You should see the home page and an `HTTP 200` with `OK - feedback count: N` from `/health`.
