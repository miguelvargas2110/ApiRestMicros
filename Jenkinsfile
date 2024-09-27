pipeline {
    agent any
    environment {
        APP_REPO_URL = 'https://github.com/usuario/app-repo.git'    // URL del repo de la aplicación
        TEST_REPO_URL = 'https://github.com/usuario/test-repo.git'  // URL del repo de las pruebas
        APP_DIR = 'app-dir'   // Directorio donde clonarás el repo de la aplicación
        TEST_DIR = 'test-dir' // Directorio donde clonarás el repo de pruebas
    }
    stages {
        stage('Clonar repositorios') {
            steps {
                // Clonando el repositorio de la aplicación
                dir(APP_DIR) {
                    git url: "${APP_REPO_URL}", branch: 'main'  // Cambia la rama según sea necesario
                }
                // Clonando el repositorio de pruebas
                dir(TEST_DIR) {
                    git url: "${TEST_REPO_URL}", branch: 'main'
                }
            }
        }

        stage('Compilar aplicación') {
            steps {
                dir(APP_DIR) {
                    // Comandos para construir la aplicación, si es necesario
                    sh './mvnw clean install'  // o cualquier otro comando para compilar la app
                }
            }
        }

        stage('Ejecutar pruebas') {
            steps {
                dir(TEST_DIR) {
                    // Ejecutar pruebas desde el proyecto de tests
                    sh './mvnw test'  // Aquí se ejecutarán las pruebas
                }
            }
        }

        stage('Generar reportes') {
            steps {
                dir(TEST_DIR) {
                    // Generar reportes Cucumber, por ejemplo
                    sh './mvnw verify'  // Verificar y generar reportes
                }
            }
        }
    }

    post {
        always {
            // Publicar los reportes o cualquier cosa que necesites después de la ejecución
            junit '**/target/surefire-reports/*.xml'  // Publicar reportes JUnit
            cucumber '**/target/cucumber-reports/*.json'  // Publicar reportes Cucumber
        }
    }
}
