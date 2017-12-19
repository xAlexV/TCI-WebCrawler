pipeline {
  agent any
  stages {
    stage('Git') {
      steps {
        git(poll: true, url: 'https://github.com/xAlexV/TCI-WebCrawler.git', branch: 'crawler')
      }
    }
    stage('Build the project') {
      parallel {
        stage('Build the project') {
          steps {
            build 'clean'
          }
        }
        stage('build') {
          steps {
            build 'build'
          }
        }
        stage('test') {
          steps {
            build 'test'
          }
        }
      }
    }
  }
}