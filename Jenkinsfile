pipeline {
    agent any

    environment {
        APP_NAME     = 'student-feedback-portal'
        WAR_NAME     = "${APP_NAME}.war"
        TOMCAT_WEBAPPS = '/opt/tomcat/webapps'          // adjust to your Tomcat install path
        BACKUP_DIR   = '/opt/tomcat/backups'            // holds the previous good WAR for rollback
        HEALTH_URL   = "http://localhost:8081/${APP_NAME}/health"
    }

    options {
        timestamps()
        skipDefaultCheckout(false)
    }

    stages {

        stage('Checkout') {
            steps {
                echo "== Checking out source =="
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo "== Compiling =="
                sh 'mvn -B clean compile'
            }
        }

        stage('Test') {
            steps {
                echo "== Running unit tests =="
                sh 'mvn -B test'
            }
            post {
                always {
                    junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
                }
            }
        }

        stage('Package') {
            steps {
                echo "== Packaging WAR =="
                sh 'mvn -B package -DskipTests'
            }
        }

        stage('Archive Artifact') {
            steps {
                archiveArtifacts artifacts: "target/${WAR_NAME}", fingerprint: true
            }
        }

        stage('Backup Current Deployment') {
            steps {
                // Keep the currently-deployed WAR so we can roll back to it if the new
                // deploy fails its health check.
                sh """
                    mkdir -p ${BACKUP_DIR}
                    if [ -f ${TOMCAT_WEBAPPS}/${WAR_NAME} ]; then
                        cp ${TOMCAT_WEBAPPS}/${WAR_NAME} ${BACKUP_DIR}/${WAR_NAME}.bak
                        echo "Backed up existing WAR."
                    else
                        echo "No existing deployment found - first deploy."
                    fi
                """
            }
        }

        stage('Deploy to Tomcat') {
            steps {
                echo "== Deploying WAR to Tomcat =="
                sh """
                    cp target/${WAR_NAME} ${TOMCAT_WEBAPPS}/${WAR_NAME}
                """
                // Give Tomcat a few seconds to explode and start the WAR
                sh 'sleep 10'
            }
        }

        stage('Verify (Health Check)') {
            steps {
                script {
                    def status = sh(script: "curl -s -o /dev/null -w '%{http_code}' ${HEALTH_URL}", returnStdout: true).trim()
                    echo "Health check returned HTTP ${status}"
                    if (status != '200') {
                        error("Health check failed with status ${status} - triggering rollback")
                    }
                }
            }
        }
    }

    post {
        failure {
            echo "== Pipeline failed - attempting rollback to last known-good WAR =="
            sh """
                if [ -f ${BACKUP_DIR}/${WAR_NAME}.bak ]; then
                    cp ${BACKUP_DIR}/${WAR_NAME}.bak ${TOMCAT_WEBAPPS}/${WAR_NAME}
                    echo "Rollback complete: restored previous WAR."
                else
                    echo "No backup available - nothing to roll back to."
                fi
            """
        }
        success {
            echo "== Pipeline succeeded: ${APP_NAME} deployed and verified healthy =="
        }
        always {
            cleanWs()
        }
    }
}
