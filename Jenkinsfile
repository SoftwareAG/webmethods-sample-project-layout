podTemplate(
    label: 'mypod', 
    inheritFrom: 'default',
    containers: [
        containerTemplate(
            name: 'docker', 
            image: 'docker:18.02',
            ttyEnabled: true,
            command: 'cat'
        ),
        containerTemplate(
            name: 'ci-is', 
            image: 'docker.devopsinitiative.com/softwareag/ci-is:10.3.0.1-sagdevops',
            alwaysPullImage: true,
            ttyEnabled: true,
            command: '/opt/softwareag/entrypoint.sh'
        )

    ],
    envVars: [
        secretEnvVar(key: 'DOCKER_USR', secretName: 'docker-store-cred', secretKey: 'username'),
        secretEnvVar(key: 'DOCKER_PSW', secretName: 'docker-store-cred', secretKey: 'password'),
        secretEnvVar(key: 'NEXUS_USR', secretName: 'docker-nexus-cred', secretKey: 'username'),
        secretEnvVar(key: 'NEXUS_PSW', secretName: 'docker-nexus-cred', secretKey: 'password')
    ],
    volumes: [
        hostPathVolume(
            hostPath: '/var/run/docker.sock',
            mountPath: '/var/run/docker.sock'
        )
    ],
    imagePullSecrets: [ 'regcred' ],
) {
    node('mypod') {
        def commitId
        stage ('Extract') {
            checkout scm
            commitId = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
        }
        def repository
        stage('Build'){
            container('ci-is') {
                sh "/bin/bash ./wait_for_is.sh"
                sh "/opt/softwareag/common/lib/ant/bin/ant -DSAGHome=/opt/softwareag -DSAG_CI_HOME=/opt/softwareag/sagdevops-ci-assets -DprojectName=${env.JOB_NAME} build"
            }
        }
        stage('Deploy') {
            container('ci-is') {
                sh "/opt/softwareag/common/lib/ant/bin/ant -DSAGHome=/opt/softwareag -DSAG_CI_HOME=/opt/softwareag/sagdevops-ci-assets -DprojectName=${env.JOB_NAME} deploy"
            }
        }
        stage('Test') {
            container('ci-is') {
                sh "/opt/softwareag/common/lib/ant/bin/ant -DSAGHome=/opt/softwareag -DSAG_CI_HOME=/opt/softwareag/sagdevops-ci-assets -DprojectName=${env.JOB_NAME} test"
                junit 'report/'
            }
        }
        stage('Image') {
            container('docker') {
                sh "docker login -u ${env.NEXUS_USR} -p ${env.NEXUS_PSW} docker.devopsinitiative.com"
                sh "cp target/bookstore/build/IS/*.zip image/"
                sh "cd image; for pkg in *.zip; do basefilename=`echo \${pkg} | sed 's/.zip\$//'`; md5sum \${basefilename}.zip > \${basefilename}.md5; done"
                sh "cd image; docker build -t docker.devopsinitiative.com/bookstore:1 -f Dockerfile ."
                sh "docker push docker.devopsinitiative.com/bookstore:1"
            }
        }
    }
}
