pipeline {
    agent {
        docker { image 'openjdk:11-jdk' }
    }
    stages {
        stage('Clean') {
            steps {
                sh './gradlew --no-daemon clean'
            }
        }

        stage('Test') {
            steps {
                sh './gradlew --no-daemon test'
            }

            post {
                always {
                    junit '**/build/test-results/test/*.xml'
                }
            }
        }

        stage('Create package') {
            steps {
                sh './gradlew --no-daemon distZip jlinkZip'
            }

            post {
                always {
                    archiveArtifacts artifacts: 'build/distributions/*.zip', fingerprint: true
                    archiveArtifacts artifacts: 'build/image.zip'
                }
            }
        }
    }
}
