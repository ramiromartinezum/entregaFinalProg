pipeline {
    agent any
    
    stages {
        stage('Compilar') {
            steps {
                script {
                    // Compilar el proyecto usando Gradle o Maven
                    sh 'mvn clean compile' // usa 'gradle build' si usas Gradle en lugar de Maven
                }
            }
        }

        stage('Pruebas Unitarias') {
            steps {
                script {
                    // Ejecutar pruebas unitarias
                    sh 'mvn test'
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
                    // Generar documentación con Javadoc
                    sh 'mvn javadoc:javadoc'
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
                    // Generar reporte de cobertura (usa JaCoCo o Cobertura)
                    sh 'mvn jacoco:report'
                }
            }
            post {
                always {
                    // Publicar resultados de cobertura de código
                    jacoco execPattern: 'target/jacoco.exec', classPattern: 'target/classes', sourcePattern: 'src/main/java', exclusionPattern: ''
                }
            }
        }
    }

    post {
        success {
            mail to: 'tu_email@example.com',
                 subject: "Pipeline Exitoso: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                 body: "El pipeline se completó con éxito.\nRevisa los resultados en Jenkins: ${env.BUILD_URL}"
        }
        failure {
            mail to: 'tu_email@example.com',
                 subject: "Pipeline Fallido: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                 body: "El pipeline falló. Revisa los detalles en Jenkins: ${env.BUILD_URL}"
        }
    }
}
