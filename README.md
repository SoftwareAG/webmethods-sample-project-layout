# webmethods-sample-project-layout
Sample project layout for webMethods assets. This sample demonstrates CI quick set up together with [https://github.com/SoftwareAG/sagdevops-ci-assets](https://github.com/SoftwareAG/sagdevops-ci-assets).

**Note**: this project is just a sample GIT setup for a typical webMethods project. Use this as your starting point. This project should include everything which is project specific, like variable substitution files for _this_ project, or the target environment specification, which defines the target servers applicable for _this_ project. Anything not project specific, i.e. all the generic deployment scripts etc. are part of the "https://github.com/SoftwareAG/sagdevops-ci-assets](https://github.com/SoftwareAG/sagdevops-ci-assets" project, which is invoked by this project. 

## Jump Start with webMethods Structure
The best way to start your webMethods project would be to fork this repository directly in Github. This will allow you to directly have a set-up copy of layout that will be completely under your control.

## Description

This sample webMethods project layout should serve as a template for organizing webMethods projects. It contains demo Integration Server packages with flow services and wM Unit Tests that are covering those.

Fork the repository to easily create a basis for you webMethods project.

## Process

1. Checkout
2. Build
	1. Copy file [master_build_Reference/build.properties](./master_build_Reference/build.properties) to "${ABE_HOME}/master_build/"
	2. Invoke default ANT target in build.xml located at "${ABE_HOME}/master_build/build.xml"
3. Deploy to Test
3. Test WmTestSuite on Test
4. Deploy to QA

## Configuration

The project specific configuration is stored in the file [projects.properties](./projects.properties). It contains the following configurations:

* __The location of the source assets__, like IS Packages, BPM Processes, etc. These relative paths are mapped to absolute paths in the build.xml. The absolute paths are substituted in the Asset Build Environment's build.properties file intot the property "build.source.dir". Please see the file [master_build_Reference/build.properties](./master_build_Reference/build.properties). Examples are:
** isPackages: defines where the IntegrationServer packages are located, relative to this project
** isTests: defines where the WmTestSuite tests are located
** bpmProjects: defines where the BPM Process Models are located
** etc.
* __config.environments__: The location of the environments groovy defintion file, e.g. "[ENV.groovy](./ENV.groovy)".
* __config.deployer.varsubDir__: The location of the variable substitions files, please see below for details.
* __config.build.version__: The build version in the form of "${MAJOR}.${MINOR}.${PATCH}". Adjust to your liking, has to be changed for each release manually in this file. This build version is used to mark the FBRs, and it is passed along to ABE, which for example sets the build property of package's manifest.v3 file to this value.
* __config.build.artifactory.*__: If you are using Artifactory as a build repository, set these parameter accordingly:
	* __config.build.artifactory.repository__: The artifactory repository name
	* __config.build.artifactory.path.org__: Since the default implementation uses the standard Ivy notation, provide a dot-separated organization, e.g. "com.softwareag".

## CI (continuous integration)

Jenkinsfiles.win and Jenkinfiles.unix in the root of the project contain Jenkins Pipeline declaration. With these and our [DevOps asset library for version 9.x and 10.0](https://github.com/SoftwareAG/sagdevops-ci-assets) you will be able to set up you CI in a matter of minutes.

The following Jenkinsfile examples are available:

* __Jenkinsfile.unix, Jenkinsfile.win__: A basic build and deployment pipeline which stores the File-based Repository (FBR) created by the Asset Build Environment in a temporary folder of the Jenkins Workspace. This local FBR is then used for all deployments.
* __Jenkisfile.artifactoryUpload.unix__: An extension to the basic build and deployment pipeline, where the FBR is uploaded to an Artifactory repository after a successful build by the ABE.
* __Jenkinsfile.artifactoryDownload.unix__: An extension to the _Jenkisfile.artifactoryUpload.unix_ file, where an uploaded FBR is later downloaded again from Artifactory.

## Environments

The file ENV.groovy defines all your **target environments**, e.g. DEV, TEST, QA, PROD. Adjust this file to your infrastructure, i.e. add/remove/change target environments, change hosts and ports, adjust the Administrator user and password etc. The environments defined in the ENV.groovy file are used for creating the Project Automator templates, which define the Deployer Projects. The ENV.groovy file also is used for creating the variable substituation files at runtime, as well as executing WmTestSuite tests an all servers of a target environment.

Note that the Jenkinsfiles references the target environments defined in the EVN.groovy by using the parameter "bda.targetEnv". E.g., the command 

> _sh "${env.SAG_HOME}/common/AssetBuildEnvironment/ant/bin/ant -f ${currentDir}/build.xml -DSAGHome=${env.SAG_HOME} -DSAG_CI_HOME=${env.SAG_CI_HOME} -Dbda.projectName=${env.JOB_NAME} -D**bda.targetEnv=TEST** deploy_ 

will deploy the current pipeline to the **TEST** environment. 

### Default values

For parameters in the environment file which are the same for many servers, you can use the default section. E.g. if all IntegrationServers are of version 9.12, instead of adding _"version='9.12'"_ to all IntegrationServer definitions, you can add _"version:'9.12'"_ to the "IntegrationServer > default" array:

```javascript
IntegrationServer {
	defaults =
			[version                : "9.12",
			 installDeployerResource: 'true',
			 test                   : 'true',
			 executeACL             : 'Administrators',
			 useSSL                 : 'false']
}
environments {
	DEV {
		IntegrationServers {
			is_node1 {
				host = "localhost"
				port = "8094"
				username = "Administrator"
				pwd = "manage"
			}
		}
	}
	TEST {
		IntegrationServers {
			is_node1 {
				host = "localhost"
				port = "8093"
				username = "Administrator"
				pwdHandle = "ADMIN_IS_TEST",
				useSSL = "true"
			}
		}
	}
}
```

### Passwords

ProjectAutomator can handle cleartext passwords (see above "**pwd**"), but also password handles. A password handle must be defined in Deployer and can be referenced in a Project Automator template by using "**pwdHandle**" instead of "pwd". Please refer to the [webMethods Deployer documentation](http://documentation.softwareag.com/webmethods/wmsuites/wmsuite9-12/Deployer/9-12_Deployer_Users_Guide.pdf) "**Automating Project Creation**".

## Variable Substitution

