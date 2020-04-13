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
    // stage('Build Native') {
    //   steps {
    //     container('buildpipeline') {
    //       sh 'gradle buildNative'
    //     }
    //   }
    // }
    // stage('Skaffold Build') {
    //   steps {
    //     container('buildpipeline') {
    //       sh 'skaffold build --skip-tests=true -f src/main/pipeline/skaffold-dev.yaml'
    //     }
    //   }
    // }
  }
  post {
    always {
      junit 'build/test-results/test/*.xml'
    }
  }
}
