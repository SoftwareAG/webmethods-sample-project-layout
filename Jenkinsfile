podTemplate(
    label: 'mypod', 
    inheritFrom: 'default',
    containers: [
        containerTemplate(
            name: 'helm', 
            image: 'lachlanevenson/k8s-helm:v2.9.1',
            ttyEnabled: true,
            command: 'cat'
        )
    ],
    envVars: [
        secretEnvVar(key: 'API_GATEWAY_USR', secretName: 'apigateway-cred', secretKey: 'username'),
        secretEnvVar(key: 'API_GATEWAY_PSW', secretName: 'apigateway-cred', secretKey: 'password'),
    ],
    volumes: [
        hostPathVolume(
            hostPath: '/var/run/docker.sock',
            mountPath: '/var/run/docker.sock'
        )
    ]
) {
    node('mypod') {
        def commitId
        stage ('Extract') {
            checkout scm
            commitId = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
        }
        stage ('Deploy') {
            container ('helm') {
                def registry = "docker.devopsinitiative.com"
                repository = "${registry}/bookstore"
                sh "helm list"
		 sh "helm delete softwareag-bookstore --purge"
                sh "helm install --wait --set image.repository=${repository},image.tag=bbbf486 --name softwareag-bookstore softwareag-bookstore"
                sh 'curl -u ${env.API_GATEWAY_USR}:${env.API_GATEWAY_PSW} -X POST "http://apigateway.devopsinitiative.com/rest/apigateway/v103/apis" -H "accept: application/json" -H "Content-Type: multipart/form-data" -F "file=@bookstore.swagger" -F "apiName=Bookstore" -F "apiDescription=Bookstore API" -F "apiVersion=V3" -F "type=swagger"'
            }
        }
    }
}
