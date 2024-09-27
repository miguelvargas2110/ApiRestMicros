pipeline {
    agent any
    environment {
        APP_REPO_URL = 'https://github.com/miguelvargas2110/ApiRestMicros.git'    // URL del repo de la aplicación
        TEST_REPO_URL = 'https://github.com/miguelvargas2110/AutomatizationTest.git'  // URL del repo de las pruebas
        APP_DIR = 'app-dir'   // Directorio donde clonarás el repo de la aplicación
        TEST_DIR = 'test-dir' // Directorio donde clonarás el repo de pruebas
    }
    stages {
        stage('Clonar repositorios') {
            steps {
                // Clonando el repositorio de la aplicación
                dir(APP_DIR) {
                    git url: "${APP_REPO_URL}", branch: 'master'  // Cambia la rama según sea necesario
                }
                // Clonando el repositorio de pruebas
                dir(TEST_DIR) {
                    git url: "${TEST_REPO_URL}", branch: 'master'
                }
            }
        }

        stage('Compilar aplicación') {
            steps {
                dir(APP_DIR) {
                    // Asegurarse de que el script gradlew sea ejecutable
                    sh 'chmod +x ./gradlew'  // Cambia permisos de ejecución
                    // Comandos para construir la aplicación usando Gradle
                    sh './gradlew clean build'  // Ejecuta la construcción de la app con Gradle
                }
            }
        }

        stage('Ejecutar pruebas') {
            steps {
                dir(TEST_DIR) {
                    // Asegurarse de que el script gradlew sea ejecutable
                    sh 'chmod +x ./gradlew'  // Cambia permisos de ejecución
                    // Ejecutar pruebas desde el proyecto de tests usando Gradle
                    sh './gradlew test'  // Ejecuta las pruebas usando Gradle
                }
            }
        }

        stage('Generar reportes') {
            steps {
                dir(TEST_DIR) {
                    // Generar reportes desde el proyecto de pruebas usando Gradle
                    sh './gradlew test'  // Generalmente, los reportes se generan en la fase de test
                }
            }
        }
    }

    post {
        always {
            // Publicar los reportes o cualquier cosa que necesites después de la ejecución
            junit '**/build/test-results/test/*.xml'  // Publicar reportes JUnit en la carpeta de resultados de Gradle
            cucumber '**/build/reports/cucumber/*.json'  // Publicar reportes Cucumber si los tienes en tu proyecto
        }
    }
}
