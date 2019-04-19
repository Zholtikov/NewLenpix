pipeline {
    agent {
        docker { image 'openjdk:11-jdk' }
    }
    stages {
        stage('Build') {
            steps {
                sh './gradlew --no-daemon --no-parallel --no-build-cache test'
            }
        }

        stage('Test') {
            steps {
                sh './gradlew --no-daemon --no-parallel --no-build-cache test'
            }
        }

        stage('Create distZip') {
            steps {
                sh './gradlew --no-daemon --no-parallel --no-build-cache distZip'
                archiveArtifacts artifacts: 'build/distributions/*.zip', fingerprint: true
            }
        }

        stage('Create jlinkZip') {
            steps {
                sh './gradlew --no-daemon --no-parallel --no-build-cache jlinkZip'
                archiveArtifacts artifacts: 'build/image.zip'
            }
        }
    }
}
