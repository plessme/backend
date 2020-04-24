/**
 * Helper method for version string from
 * gradle.properties or git information.
 */
def String getVersion() {
  node {
    //read verson from gradle.properties
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
    if(env.BRANCH_NAME.startsWith('feature/')) {
      commit = "${GIT_COMMIT}"
      version = commit.substring(0, 7)
    }
  }
  // return calculated version string
  return version
}
// def String getEnv() {
//   node {
//     env = ""
//     if(env.BRANCH_NAME == 'develop') {
//       env = 'develop'
//     }
//     if(env.BRANCH_NAME.startsWith('release/')) {
//       env = 'staging'
//     }
//     if(env.BRANCH_NAME.startsWith('master')) {
//       env = 'integration'
//     }
//     return env
//   }
// }
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
            // env.ENV_NAME = getEnv()
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
    // stage ('Publish artifacts to Artifactory') {
    //   when {
    //     anyOf {
    //       branch 'develop'
    //       branch pattern: "release/*"
    //       branch pattern: "feature/*" // for testing purposes
    //       branch 'master'
    //     }
    //   }
    //   steps {
    //     rtUpload (
    //       serverId: 'jcr',
    //       spec: '''{
    //         "files": [
    //           {
    //             "pattern": "build/*-runner",
    //             "target": "plessme-generic-develop/backend/"
    //           }
    //         ]
    //       }'''
    //     )
    //     rtPublishBuildInfo (serverId: 'jcr')
    //   }
    // }
    stage('Build & Push Docker Image') {
      steps {
        container('buildpipeline') {
          // TODO fix tests against kaniko
          sh 'skaffold build --skip-tests=true --file-output=tags.json -f src/main/pipeline/skaffold-build.yaml'
        }
      }
    }
    stage('Deploy for Full API Tests') {
      steps {
        container('buildpipeline') {
          // TODO fix tests against kaniko
          sh 'skaffold deploy --build-artifacts=tags.json -f src/main/pipeline/skaffold-build.yaml'
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
    stage("Deploy to Environment") {
      when { 
        anyOf {
          branch 'master'
          branch 'develop'
          branch pattern: "release/*"
        }
      }
      steps {
        container('buildpipeline') {
          // TODO fix tests against kaniko
          // TODO test if staging and integration deployment is working
          // sh "skaffold deploy --status-check -f src/main/pipeline/skaffold-${ENV_NAME}.yaml"
          sh "skaffold deploy --status-check -f src/main/pipeline/skaffold-develop.yaml"
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
