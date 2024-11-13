pipeline {
    agent any

    stages {
        stage('Compilar') {
            steps {
                script {
                    // Compilar el proyecto usando Maven en Windows
                    bat 'mvn clean compile'
                }
            }
        }

        stage('Pruebas Unitarias') {
            steps {
                script {
                    // Ejecutar pruebas unitarias con Maven en Windows
                    bat 'mvn test'
                }
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml' // Publicar resultados de pruebas
                }
            }
        }

        stage('Generar Documentación') {
            steps {
                script {
                    // Generar documentación con Javadoc en Windows
                    bat 'mvn javadoc:javadoc'
                }
            }
            post {
                always {
                    archiveArtifacts artifacts: 'target/site/apidocs/**', allowEmptyArchive: true
                }
            }
        }

        stage('Cobertura de Código') {
            steps {
                script {
                    // Generar reporte de cobertura en Windows
                    bat 'mvn jacoco:report'
                }
            }
            post {
                always {
                    jacoco execPattern: 'target/jacoco.exec', classPattern: 'target/classes', sourcePattern: 'src/main/java', exclusionPattern: ''
                }
            }
        }
    }
}

