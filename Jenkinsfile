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
                sh "helm install --wait --set image.repository=${repository},image.tag=3 --name softwareag-bookstore softwareag-bookstore"
            }
        }
    }
}
