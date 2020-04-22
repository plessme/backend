//properties = null
version = null
def loadProperties() {
    node {
        //read gradle.properties
        checkout scm
        properties = new Properties()
        File propertiesFile = new File("${workspace}/gradle.properties")
        properties.load(propertiesFile.newDataInputStream())
        version = properties.repo
        // calculate version suffix
        if (env.BRANCH_NAME == 'master') {
          echo version 
        }
        if(env.BRANCH_NAME == 'develop') {
          version += '-SNAPSHOT'
        }
        if(env.BRANCH_NAME.startsWith('release/')) {
          version += '-rc-' + env.BUILD_NUMBER
        }
        if(env.BRANCH_NAME.startsWith('feature/')) {
          echo version
        }
        echo version
    }
}

pipeline {
  agent {
    kubernetes {
      yamlFile 'src/main/pipeline/build-pod.yaml'
    }
  }
  stages {
    stage('Prepare environment') {
      steps {
        container('buildpipeline') {
          script {
            loadProperties()
          }
        }
      }
    }
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
          sh 'gradle javadoc'
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
          sh 'newman run src/test/api/users-api-test-collection.json --environment="src/test/api/api-test-build-env.json" --reporters junit --reporter-junit-export="build/test-results/test/newman-report.xml" --timeout 5000 --verbose'
        }
      }
    }
  }
  post {
    always {
      junit 'build/test-results/test/*.xml'
    }
    cleanup {
      container('buildpipeline') {
        sh 'skaffold delete -f src/main/pipeline/skaffold-build.yaml'
      }
    }
  }
}
