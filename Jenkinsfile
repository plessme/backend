/**
 * Helper method to load version
 * string from gradle.properties.
 */
def String loadGradleVersion() {
  node {
    checkout scm
    properties = new Properties()
    File propertiesFile = new File("${workspace}/gradle.properties")
    properties.load(propertiesFile.newDataInputStream())
    return properties.version
  }
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
  environment {
    DOCKER_URL='jcr.bongladesch.com/docker'
    ARTIFACTORY_URL='https://jcr.bongladesch.com/artifactory'
    BUILD_NAME="plessme-backend-${BRANCH_NAME}"
  }
  stages {
    stage('Prepare Environment') {
      steps {
        withCredentials([
          usernamePassword(credentialsId: 'git-user', usernameVariable: 'GRGIT_USER', passwordVariable: 'GRGIT_PASS'),
          usernamePassword(credentialsId: 'artifactory-user', usernameVariable: 'ARTIFACTORY_USER', passwordVariable: 'ARTIFACTORY_PASSWORD')
        ]) {
          container('buildpipeline') {
            script {
              env.VERSION = version(loadGradleVersion())
              env.ENV_NAME = environment()
              env.REPO_NAME = repository()
            }
            sh "jfrog rt c jcr --interactive=false --url=${ARTIFACTORY_URL} --user=${ARTIFACTORY_USER} --password=${ARTIFACTORY_PASSWORD}"
            sh "jfrog rt gradlec --server-id-deploy=jcr --repo-deploy=generic-${REPO_NAME} --deploy-maven-desc=false --deploy-ivy-desc=false"
            sh "docker login ${DOCKER_URL} -u ${ARTIFACTORY_USER} -p ${ARTIFACTORY_PASSWORD}"
          }
        }
      }
    }
    stage('Java') {
      parallel {
        stage('Check Code Format') {
          steps {
            container('buildpipeline') {
              sh "jfrog rt gradle --build-name=${BUILD_NAME} --build-number=${BUILD_NUMBER} -Porg.gradle.parallel=true spotlessCheck"
            }
          }
        }
        stage('Unit Tests') {
          steps {
            container('buildpipeline') {
              sh "jfrog rt gradle --build-name=${BUILD_NAME} --build-number=${BUILD_NUMBER} -Porg.gradle.parallel=true test"
            }
          }
        }
        stage('Javadocs with UML Classes') {
          steps {
            container('buildpipeline') {
              sh "jfrog rt gradle --build-name=${BUILD_NAME} --build-number=${BUILD_NUMBER} -Porg.gradle.parallel=true -Pversion=${VERSION} javadoc"
            }
          }
        }
        stage('Build Native Java Image') {
          steps {
            container('buildpipeline') {
              sh "jfrog rt gradle --build-name=${BUILD_NAME} --build-number=${BUILD_NUMBER} -Porg.gradle.parallel=true -Pversion=${VERSION} buildNative"
            }
          }
          post {
            success {
              archiveArtifacts artifacts: "build/plessme-backend-${VERSION}-runner", fingerprint: true
            }
          }
        }
      }
    }
    stage('Publish Native Java Image') {
      when {
        anyOf {
          branch 'master'
          branch 'develop'
          branch pattern: 'release/*'
        }
      }
      steps {
        container('buildpipeline') {
          sh "jfrog rt u --build-name=${BUILD_NAME} --build-number=${BUILD_NUMBER} build/plessme-backend-${VERSION}-runner generic-${REPO_NAME}/backend/plessme-backend-${VERSION}-runner"
        }
      }
    }
    stage('Build Docker Image') {
      steps {
        container('buildpipeline') {
          sh "jfrog rt docker-pull ${DOCKER_URL}/ubuntu:18.04 docker --build-name=${BUILD_NAME} --build-number=${BUILD_NUMBER}"
          sh "jfrog rt docker-pull ${DOCKER_URL}/nginx:1.17.10 docker --build-name=${BUILD_NAME} --build-number=${BUILD_NUMBER}"
          sh "docker build -t ${DOCKER_URL}-${REPO_NAME}/plessme/backend:${VERSION} -f src/main/docker/Dockerfile ."
          sh "docker build -t ${DOCKER_URL}-${REPO_NAME}/plessme/backend-docs:${VERSION} -f src/main/docker/Dockerfile.docs ."
        }
      }
    }
    stage('Test Docker Image') {
      steps {
        container('buildpipeline') {
          sh "container-structure-test test --image ${DOCKER_URL}-${REPO_NAME}/plessme/backend:${VERSION} --config src/test/docker/backend-tests.yaml"
        }
      }
    }
    stage('Push Docker Image') {
      steps {
        container('buildpipeline') {
          sh "jfrog rt docker-push ${DOCKER_URL}-${REPO_NAME}/plessme/backend:${VERSION} docker-${REPO_NAME} --build-name=${BUILD_NAME} --build-number=${BUILD_NUMBER}"
          sh "jfrog rt docker-push ${DOCKER_URL}-${REPO_NAME}/plessme/backend-docs:${VERSION} docker-${REPO_NAME} --build-name=${BUILD_NAME} --build-number=${BUILD_NUMBER}"
        }
      }
    }
    stage('Deploy for Full API Tests') {
      steps {
        container('buildpipeline') {
          dir("${WORKSPACE}/src/main/kustomize/build") {
            sh "kustomize edit set image jcr.bongladesch.com/docker/plessme/backend=jcr.bongladesch.com/docker/plessme/backend:${VERSION}"
          }
          sh 'skaffold deploy --status-check -f src/main/pipeline/skaffold-build.yaml'
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
    stage('Deploy to Environment') {
      when { 
        anyOf {
          branch 'master'
          branch 'develop'
          branch pattern: 'release/*'
        }
      }
      steps {
        container('buildpipeline') {
          // TODO test if staging and integration deployment is working
          dir("src/main/kustomize/${ENV_NAME}") {
            sh "kustomize edit set image jcr.bongladesch.com/docker/plessme/backend=jcr.bongladesch.com/docker/plessme/backend:${VERSION} jcr.bongladesch.com/docker/plessme/backend-docs=jcr.bongladesch.com/docker/plessme/backend-docs:${VERSION}"
          }
          sh "skaffold deploy --status-check -f src/main/pipeline/skaffold-${ENV_NAME}.yaml"
        }
      }
    }
    stage('Create Github Release') {
      when {
        anyOf {
          branch 'master'
          branch pattern: 'feature/*' // TODO for tests
        }
      }
      steps {
        container('buildpipeline') {
          sh "jfrog rt gradle --build-name=${BUILD_NAME} --build-number=${BUILD_NUMBER} -Porg.gradle.parallel=true -Pversion=${VERSION} -PgitUser=${GRGIT_USER} -PgitPassword=${GRGIT_PASS} createRelease"
        }
      }
    }
  }
  post {
    always {
      junit 'build/test-results/test/*.xml'
      container('buildpipeline') {
        sh "jfrog rt bce ${BUILD_NAME} ${BUILD_NUMBER}"
        sh "jfrog rt bag ${BUILD_NAME} ${BUILD_NUMBER} ."
        sh "jfrog rt bp ${BUILD_NAME} ${BUILD_NUMBER}"
      }
    }
    cleanup {
      container('buildpipeline') {
        sh 'skaffold delete -f src/main/pipeline/skaffold-build.yaml'
      }
    }
  }
}
