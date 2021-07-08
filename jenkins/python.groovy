node {
    
    def DEPLOY_REPO="kyle-pypi-dev-local"
    
    stage('Prepare') {
        withCredentials([usernamePassword(credentialsId: 'art_username_password', passwordVariable: 'ART_PASSWORD', usernameVariable: 'ART_USERNAME')]) {
            sh 'jfrog rt c art1 --url=http://182.92.214.141:8081/artifactory --user=${ART_USERNAME} --password=${ART_PASSWORD}'
        }
        sh 'jfrog rt use art1'
    }
    stage('SCM') {
        // cleanWs()
        sh 'ls'
        git([url: 'https://github.com/kyle11235/python-example', branch: 'master'])
    }
    
    stage('Build') {
        sh "jfrog rt pipi -r requirements.txt --build-name=${env.JOB_NAME} --build-number=${env.BUILD_NUMBER} --module=babel"
        // sh "jfrog rt pipi --force-reinstall -r requirements.txt --build-name=${env.JOB_NAME} --build-number=${env.BUILD_NUMBER}"
    }
    
    stage('Package the project') {
        sh "python setup.py sdist bdist_wheel"
    }
    
    stage('Publish packages') {
        sh "jfrog rt u dist/ ${DEPLOY_REPO} --build-name=${env.JOB_NAME} --build-number=${env.BUILD_NUMBER}"
    }
    
    stage('Collect environment variables') {
        sh "jfrog rt bce ${env.JOB_NAME} ${env.BUILD_NUMBER}"
    }
    
    stage('Publish the build info') {
        sh "jfrog rt bp ${env.JOB_NAME} ${env.BUILD_NUMBER}"
    }
}