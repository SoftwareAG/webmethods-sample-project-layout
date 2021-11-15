podTemplate(
    label: 'mypod', 
    inheritFrom: 'default',
    serviceAccount: 'k8s-helm',
    containers: [
        containerTemplate(
            name: 'helm', 
            image: 'ianmward/k8s-helm:80ff3d2',
            ttyEnabled: true,
            command: 'cat'
        )
    ],
    envVars: [
        secretEnvVar(key: 'API_GATEWAY_USR', secretName: 'apigateway-cred', secretKey: 'username'),
        secretEnvVar(key: 'API_GATEWAY_PSW', secretName: 'apigateway-cred', secretKey: 'password'),
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
                def tag = "338b591 "
                sh "helm  list"
                sh "helm  upgrade --install --force --timeout=10m0s --set image.repository=${repository},image.tag=${tag} softwareag-bookstore softwareag-bookstore"
                def API_ID = sh ( script: "curl -u Administrator:manage -X POST 'https://api-gateway.eks.au-poc.com/rest/apigateway/apis' -H 'accept: application/json' -H 'Content-Type: multipart/form-data' -F 'file=@bookstore.swagger' -F 'apiName=Bookstore' -F 'apiDescription=Bookstore API' -F 'apiVersion=V3' -F 'type=swagger' | jq -r '.apiResponse.api.id'",returnStdout:true).trim()
                sh "curl -u $(API_GATEWAY_USR}:${API_GATEWAY_PSW} -X PUT 'https://api-gateway.eks.au-poc.com/rest/apigateway/apis/'${API_ID}'/activate' -H 'accept: application/json' -H 'Content-Type: multipart/form-data'"
            }
        }
    }
}
