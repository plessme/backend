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
    // stage('Build & Push & Deploy') {
    //   steps {
    //     container('buildpipeline') {
    //       sh 'skaffold run'
    //     }
    //   }
    // }
  }
}
