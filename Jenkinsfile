pipeline {
  agent any
  stages {
    stage('Git') {
      steps {
        git(poll: true, url: 'https://github.com/xAlexV/TCI-WebCrawler.git', branch: 'crawler')
        git(url: 'https://github.com/xAlexV/TCI-WebCrawler.git', branch: 'master', poll: true)
      }
    }
    stage('Gradle tasks') {
      steps {
        sh 'gradle clean'
        sh 'gradle test'
        sh 'gradle build'
      }
    }
    stage('Print message') {
      steps {
        echo 'Gradle build is successful'
      }
    }
  }
}