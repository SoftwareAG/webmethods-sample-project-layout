podTemplate(
    label: 'mypod', 
    inheritFrom: 'default',
    serviceAccount: 'k8s-helm',
    containers: [
        containerTemplate(
            name: 'helm', 
            image: 'ianmward/k8s-helm:e86217f',
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
                def registry = "harbor.eks-iw.au-poc.com/library"
                def repository = "${registry}/bookstore"
                def tag = "2b7aaf7 "
                sh "helm  list"
                sh "helm  upgrade --install --force --timeout=10m0s --set image.repository=${repository},image.tag=${tag} softwareag-bookstore softwareag-bookstore"
                def API_ID = sh ( script: "curl -u Administrator:manage -X POST 'http://api-gateway.eks-iw.au-poc.com/rest/apigateway/apis' -H 'accept: application/json' -H 'Content-Type: multipart/form-data' -F 'file=@bookstore.swagger' -F 'apiName=Bookstore' -F 'apiDescription=Bookstore API' -F 'apiVersion=V3' -F 'type=swagger' | jq -r '.apiResponse.api.id'",returnStdout:true).trim()
                sh "curl -u Administrator:manage -X PUT 'http://api-gateway.eks-iw.au-poc.com/rest/apigateway/apis/'${API_ID}'/activate' -H 'accept: application/json' -H 'Content-Type: multipart/form-data'"
            }
        }
    }
}
