pipeline {
  agent {
    kubernetes {
      yamlFile 'src/main/pipeline/pod.yaml'
    }
  }
  stages {
    stage('Validate Code Format') {
      steps {
        container('buildpipeline') {
          sh 'gradle spotlessCheck'
        }
      }
    }
    stage('Unit Tests') {
      steps {
        container('buildpipeline') {
          sh 'gradle test'
        }
      }
    }
    stage('Javadocs with UML Classes') {
      steps {
        container('buildpipeline') {
          sh 'gradle test'
        }
      }
    }
    stage('Build Native') {
      steps {
        container('buildpipeline') {
          sh 'gradle buildNative'
        }
      }
    }
    stage('Skaffold Run') {
      steps {
        container('buildpipeline') {
          // TODO fix tests against kaniko
          sh 'skaffold run --skip-tests=true -f src/main/pipeline/skaffold-build.yaml'
        }
      }
    }
    stage('Full API Tests') {
      steps {
        container('newman') {
          sh 'newman run src/test/api/users-api-test-collection.json -e /etc/postman/api-test-build-env.json -r junit --reporter-junit-export="build/test-results/test/newman-report.xml'
        }
      }
    }
    stage('Skaffold Delete') {
      steps {
        container('buildpipeline') {
          sh 'skaffold delete -f src/main/pipeline/skaffold-build.yaml'
        }
      }
    }
  }
  post {
    always {
      junit 'build/test-results/test/*.xml'
    }
  }
}
