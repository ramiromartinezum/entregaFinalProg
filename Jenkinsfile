pipeline {
    agent any
    
    stages {
        stage('Compilar') {
            steps {
                script {
                    // Compilar el proyecto usando Maven
                    sh 'mvn clean compile'
                }
            }
        }

        stage('Pruebas Unitarias') {
            steps {
                script {
                    // Ejecutar pruebas unitarias con Maven
                    sh 'mvn test'
                }
            }
            post {
                always {
                    // Publicar los resultados de las pruebas unitarias
                    junit '**/target/surefire-reports/*.xml'
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
                    // Guardar la documentación generada como artefacto
                    archiveArtifacts artifacts: 'target/site/apidocs/**', allowEmptyArchive: true
                }
            }
        }

        stage('Cobertura de Código') {
            steps {
                script {
                    // Generar reporte de cobertura con JaCoCo
                    sh 'mvn jacoco:report'
                }
            }
            post {
                always {
                    // Publicar el reporte de cobertura de código
                    jacoco execPattern: 'target/jacoco.exec', classPattern: 'target/classes', sourcePattern: 'src/main/java', exclusionPattern: ''
                }
            }
        }
    }

    post {
        success {
            mail to: 'fdelcampo01@gmail.com',
                 subject: "Pipeline Exitoso: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                 body: "El pipeline se completó con éxito.\nRevisa los resultados en Jenkins: ${env.BUILD_URL}"
        }
        failure {
            mail to: 'fdelcampo01@gmail.com',
                 subject: "Pipeline Fallido: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                 body: "El pipeline falló. Revisa los detalles en Jenkins: ${env.BUILD_URL}"
        }
    }
}
