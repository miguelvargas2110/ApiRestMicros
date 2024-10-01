pipeline {
    agent any
    environment {
        APP_REPO_URL = 'https://github.com/miguelvargas2110/ApiRestMicros.git'
        TEST_REPO_URL = 'https://github.com/miguelvargas2110/AutomatizationTest.git'
        APP_DIR = 'app-dir'
        TEST_DIR = 'test-dir'
    }
    stages {
        stage('Clonar repositorios') {
            steps {
                dir(APP_DIR) {
                    git url: "${APP_REPO_URL}", branch: 'master'
                }
                dir(TEST_DIR) {
                    git url: "${TEST_REPO_URL}", branch: 'master'
                }
            }
        }

        stage('Compilar aplicación') {
            steps {
                dir(APP_DIR) {
                    sh 'chmod +x ./gradlew'
                    sh './gradlew clean build'
                }
            }
        }

        stage('Ejecutar pruebas') {
            steps {
                dir(TEST_DIR) {
                    sh 'chmod +x ./gradlew'
                    sh './gradlew test'
                }
            }
        }

        stage('Generar reportes') {
            steps {
                dir(TEST_DIR) {
                    // Ejecutar tu clase Java que genera el reporte HTML
                    sh './gradlew run -PmainClass=reports.GenerateCucumberReport'

                    // Publicar el archivo HTML del reporte de Cucumber
                    publishHTML (target: [
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'target',  // Cambia a la ruta correcta donde se genera el HTML
                        reportFiles: 'cucumber-reports.html',  // Este es el archivo HTML generado por tu clase
                        reportName: 'Reporte de Cucumber'
                    ])
                }
            }
        }
    }

    post {
        always {
            junit '**/build/test-results/test/*.xml'
            cucumber '**/build/reports/cucumber/*.json'
        }
    }
}
