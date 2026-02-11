pipeline {
    agent { label 'docker-agent' }

    parameters {
        string(
            name: 'BRANCH_NAME',
            defaultValue: 'develop',
            description: 'Branch to build'
        )
    }

    environment {
        GIT_REPO     = 'https://github.com/erickperez091/user-service.git'
        DOCKER_IMAGE = 'erickperez091/dev-user-service'
    }

    stages {

        stage('Checkout') {
            steps {
                echo "Cloning branch: ${params.BRANCH_NAME}"
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: "*/${params.BRANCH_NAME}"]],
                    userRemoteConfigs: [[url: env.GIT_REPO]]
                ])
            }
        }

        stage('Read version from POM') {
            steps {
                script {
                    def version = sh(
                        script: "mvn -q -Dexec.executable=echo -Dexec.args='\\${project.version}' --non-recursive exec:exec",
                        returnStdout: true
                    ).trim()

                    env.PROJECT_VERSION = version
                    echo "Detected version: ${env.PROJECT_VERSION}"
                }
            }
        }

        stage('Build & Deploy to Nexus') {
            steps {
                configFileProvider([
                    configFile(
                        fileId: 'nexus-settings',
                        variable: 'MAVEN_SETTINGS'
                    )
                ]) {
                    sh """
                        mvn clean deploy \
                          -s $MAVEN_SETTINGS \
                          -DskipTests \
                          -DallowInsecureProtocol=true
                    """
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh """
                    docker build \
                      --build-arg JAR_FILE=target/user-service-${env.PROJECT_VERSION}.jar \
                      -t ${DOCKER_IMAGE}:${env.PROJECT_VERSION} \
                      -t ${DOCKER_IMAGE}:latest \
                      -f Dockerfile .
                """
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-credentials',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )
                ]) {
                    sh """
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push ${DOCKER_IMAGE}:${env.PROJECT_VERSION}
                        docker push ${DOCKER_IMAGE}:latest
                    """
                }
            }
        }
    }

    post {
        success {
            echo "SUCCESS → ${params.BRANCH_NAME} → ${env.PROJECT_VERSION}"
        }
        failure {
            echo "FAILED → ${params.BRANCH_NAME}"
        }
    }
}
