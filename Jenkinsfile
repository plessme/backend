/**
 * Helper method for version ENV from gradle.properties
 */
def String getVersion() {
  node {
    //read gradle.properties
    checkout scm
    properties = new Properties()
    File propertiesFile = new File("${workspace}/gradle.properties")
    properties.load(propertiesFile.newDataInputStream())
    version = properties.version
    // calculate version suffix
    if(env.BRANCH_NAME == 'develop') {
      version += '-SNAPSHOT'
    }
    if(env.BRANCH_NAME.startsWith('release/')) {
      version += '-rc-' + env.BUILD_NUMBER
    }
  }
  return version
}
/**
 * Pipeline implementation
 */
pipeline {
  agent {
    kubernetes {
      yamlFile 'src/main/pipeline/build-pod.yaml'
    }
  }
  stages {
    stage('Prepare Environment') {
      steps {
        container('buildpipeline') {
          script {
            env.VERSION = getVersion()
          }
        }
      }
    }
    stage('Validate Code Format') {
      steps {
        container('buildpipeline') {
          sh 'gradle -Porg.gradle.parallel=true spotlessCheck'
        }
      }
    }
    stage('Unit Tests') {
      steps {
        container('buildpipeline') {
          sh 'gradle -Porg.gradle.parallel=true test'
        }
      }
    }
    stage('Javadocs with UML Classes') {
      steps {
        container('buildpipeline') {
          sh "gradle -Porg.gradle.parallel=true -Pversion=${VERSION} javadoc"
        }
      }
    }
    stage('Build Native') {
      steps {
        container('buildpipeline') {
          sh "gradle -Porg.gradle.parallel=true -Pversion=${VERSION} buildNative"
        }
      }
    }
    stage('Deploy for API tests') {
      steps {
        container('buildpipeline') {
          // TODO fix tests against kaniko
          sh 'skaffold run --skip-tests=true -f src/main/pipeline/skaffold-build.yaml'
        }
      }
    }
    stage('Execute Full API Tests') {
      steps {
        container('newman') {
          sh 'newman run src/test/api/users-api-test-collection.json --environment="src/test/api/api-test-build-env.json" --reporters junit --reporter-junit-export="build/test-results/test/newman-report.xml" --timeout 5000 --verbose'
        }
      }
    }
    stage ('Publish Build Info to Artifactory') {
      steps {
        rtPublishBuildInfo (serverId: 'jcr')
      }
    }
    stage('Deploy to Develop Environment') {
      when { branch 'develop'}
      steps {
        container('buildpipeline') {
          // TODO fix tests against kaniko
          sh 'skaffold run --skip-tests=true -f src/main/pipeline/skaffold-develop.yaml'
        }
      }
    }
    stage('Deploy to Staging Environment') {
      when { branch pattern: "release/*" }
      steps {
        container('buildpipeline') {
          // TODO fix tests against kaniko
          // TODO test if staging deployment is working
          sh 'skaffold run --skip-tests=true -f src/main/pipeline/skaffold-stage.yaml'
        }
      }
    }
    stage('Deploy to Integration Environment') {
      when { branch 'master'}
      steps {
        container('buildpipeline') {
          // TODO fix tests against kaniko
          // TODO test if integration deployment is working
          sh 'skaffold run --skip-tests=true -f src/main/pipeline/skaffold-integration.yaml'
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
