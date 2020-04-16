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
        container('buildpipeline') {
          // TODO implement full api tests with newman
          sh 'echo "THIS STAGE IS NOT IMPLEMENTED."'
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
      // TODO add test results from newman
    }
  }
}
