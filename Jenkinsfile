podTemplate(
    label: 'mypod', 
    inheritFrom: 'default',
    containers: [
        containerTemplate(
            name: 'helm', 
            image: 'docker.devopsinitiative.com/k8s-helm:2.10.0',
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
    ],
    imagePullSecrets: [ 'regcred' ]
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
                sh "helm upgrade --install --wait --set image.repository=${repository},image.tag=bbbf486 softwareag-bookstore softwareag-bookstore"
                sh "content=$(curl -u Administrator:manage -X POST 'https://api.devopsinitiative.com/rest/apigateway/apis' -H 'accept: application/json' -H 'Content-Type: multipart/form-data' -F 'file=@bookstore.swagger' -F 'apiName=Bookstore' -F 'apiDescription=Bookstore API' -F 'apiVersion=V3' -F 'type=swagger' )"
                sh "content=$(curl -u Administrator:manage -X POST 'https://api.devopsinitiative.com/rest/apigateway/apis' -H 'accept: application/json' -H 'Content-Type: multipart/form-data' -F 'file=@bookstore.swagger' -F 'apiName=Bookstore' -F 'apiDescription=Bookstore API' -F 'apiVersion=V3' -F 'type=swagger' );  api-id=$( echo jq -r '.apiResponse.api.id' <<< $content) ; echo $api-id"
            }
        }
    }
}
