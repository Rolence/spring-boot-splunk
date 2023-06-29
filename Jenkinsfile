pipeline {
  agent any
    environment {
    DOCKERHUB_CREDENTIALS = credentials('docker-cred-with-token')
  }
  }
  stages {
    stage('Checkout') {
      steps {
        sh 'echo passed'
        git branch: 'master', url: 'https://github.com/Rolence/spring-boot-splunk.git'
      }
    }
    stage('Build and Test') {
    agent {
    docker {//maven:3.8.5-openjdk-17
      image 'maven:3.8.5-openjdk-17' //'openjdk:21-ea-17-jdk'  //shinyay/docker-mvn-jdk8:3.6.3
      args '--user root -v /var/run/docker.sock:/var/run/docker.sock' // mount Docker socket to access the host's Docker daemon
    }
      steps {
        sh 'ls -ltr'
        sh 'mvn install'
        // build the project and create a JAR file
        sh 'mvn package'
      }
    }
    stage('Build and Push Docker Image') {
      environment {
        DOCKER_IMAGE = "akarolence/spring-splunk:${BUILD_NUMBER}"
        DOCKERFILE_LOCATION = "Dockerfile"
        REGISTRY_CREDENTIALS = credentials('docker-cred')
      }
      steps {
        script {
            sh 'docker build -t ${DOCKER_IMAGE} .'
            def dockerImage = docker.image("${DOCKER_IMAGE}")
            docker.withRegistry('https://index.docker.io/v1/', "docker-cred") {
                dockerImage.push()
            }
        }
      }
    }
    stage('Static Code Analysis') {
      environment {
        SONAR_URL = "http://34.201.116.83:9000"
      }
      steps {
        withCredentials([string(credentialsId: 'sonarqube', variable: 'SONAR_AUTH_TOKEN')]) {
          sh 'mvn sonar:sonar -Dsonar.login=$SONAR_AUTH_TOKEN -Dsonar.host.url=${SONAR_URL}'
        }
      }
    }
    stage('Update Deployment File') {
        environment {
            GIT_REPO_NAME = "Jenkins-Zero-To-Hero"
            GIT_USER_NAME = "rolence"
        }
        steps {
            withCredentials([string(credentialsId: 'github', variable: 'GITHUB_TOKEN')]) {
                sh '''
                    git config user.email "akarolence12@gmail.com"
                    git config user.name "Achia Rolence"
                    BUILD_NUMBER=${BUILD_NUMBER}
                    sed -i "s/replaceImageTag/${BUILD_NUMBER}/g" java-maven-sonar-argocd-helm-k8s/spring-boot-app-manifests/deployment.yml
                    git add java-maven-sonar-argocd-helm-k8s/spring-boot-app-manifests/deployment.yml
                    git commit -m "Update deployment image to version ${BUILD_NUMBER}"
                    git push https://${GITHUB_TOKEN}@github.com/${GIT_USER_NAME}/${GIT_REPO_NAME} HEAD:main
                '''
            }
        }
    }
  }
}
