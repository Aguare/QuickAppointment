pipeline {
    agent any
    tools {
        maven 'Maven Apache'
    }
    environment {
        JAVA_HOME = '/usr/lib/jvm/java-17-openjdk-amd64'
        PROFILES = 'test'
    }
    stages {
        stage('Checkout') {
            steps {
                script {
                    echo "Checking out branch: ${env.BRANCH_NAME}"
                    // Clone the repository with the corresponding branch
                    git url: 'https://github.com/Aguare/QuickAppointment.git', branch: env.BRANCH_NAME, credentialsId: 'github-pat-global'
                }
            }
        }
        stage('Build Backend and Generate JaCoCo report') {
            steps {
                dir('app-backend') {
                    // Build using Maven
                    sh '''
                        mvn clean test -D spring.profiles.active=test &&
                        mvn clean install -D spring.profiles.active=test
                    '''
                }
            }
        }

        stage('Verify Jacoco Exec') {
            steps {
                dir('app-backend/target') {
                    sh 'ls -l'
                }
            }
        }
        
    }
    post {
        success {
            script {
                jacoco (
                    execPattern: '**/target/*.exec',
                    classPattern: '**/target/classes',
                    sourcePattern: '**/src/main/java',
                    exclusionPattern: '**/target/test-classes',
                    changeBuildStatus: true,
                    minimumLineCoverage: '80'
                )

            }
            echo 'Backend build completed successfully!'
        }
        failure {
            echo 'Backend build failed.'
        }
    }
}