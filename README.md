# webmethods-sample-project-layout
Sample project layout for webMethods assets. This sample demonstrates CI quick set up together with [https://github.com/SoftwareAG/sagdevops-ci-assets](https://github.com/SoftwareAG/sagdevops-ci-assets).

**Note**: this project is just a sample GIT setup for a typical webMethods project. Use this as your starting point. This project should include everything which is project specific, like variable substitution files for _this_ project, or the target environment specification, which defines the target servers applicable for _this_ project. Anything not project specific, i.e. all the generic deployment scripts etc. are part of the "[https://github.com/SoftwareAG/sagdevops-ci-assets](https://github.com/SoftwareAG/sagdevops-ci-assets)" project, which is invoked by this project. 

## Jump Start with webMethods Structure
The best way to start your webMethods project would be to fork this repository directly in Github. This will allow you to directly have a set-up copy of layout that will be completely under your control.

## Description

This sample webMethods project layout should serve as a template for organizing webMethods projects. It contains demo Integration Server packages with flow services and wM Unit Tests that are covering those.

Fork the repository to easily create a basis for you webMethods project.

## Deployment Pipeline Process

The basic example deployment pipeline process is as follows:

1. __Checkout__: Checkout this project from GitHub
2. __Build__
	1. Copy file [master_build_Reference/build.properties](./master_build_Reference/build.properties) to "${ABE_HOME}/master_build/"
	2. Invoke default ANT target in build.xml located at "${ABE_HOME}/master_build/build.xml"
	3. Store resulting File-based Repository in Jenkins workspace
3. __Deploy__: Deploy to Test
3. __Test__: Execute WmTestSuite tests on Test target environment
4. __Deploy__: If WmTestSuite tests have been successfull, deploy to QA

## Configuration

The project specific configuration is stored in the file [projects.properties](./projects.properties). It contains the following configurations:

* __config.assets.*__: The location of the source assets, like IS Packages, BPM Processes, etc. These relative paths are mapped to absolute paths in the build.xml. The absolute paths are substituted in the Asset Build Environment's build.properties file into the property "build.source.dir". Please see the file [master_build_Reference/build.properties](./master_build_Reference/build.properties). Examples are:
** isPackages: defines where the IntegrationServer packages are located, relative to this project
** isTests: defines where the WmTestSuite tests are located
** bpmProjects: defines where the BPM Process Models are located
** etc.
* __config.environments__: The location of the environments groovy definition file, e.g. "[ENV.groovy](./ENV.groovy)".
* __config.deployer.splitDelpoymentSets__: If "true", deployments are split into in multiple deployment sets with a matching number of deployment candidates. See [Splitting Deployments](#splitting-deployments) for details.
* __config.deployer.doVarSub__: Set to "true" in order to execute variable substitution.
* __config.deployer.varsubDir__: The location of the variable substitions files, please see below for details.
* __config.build.version__: The build version in the form of "${MAJOR}.${MINOR}.${PATCH}". Adjust to your liking, has to be changed for each release manually in this file. This build version is used to mark the FBRs, and it is passed along to ABE, which for example sets the build property of package's manifest.v3 file to this value.
* __config.build.fbr.type__: Either "local" or "artifactory":
	* __local__: The File-based Repository is stored in the Jenkins workspace and referenced by all build steps
	* __artifactory__: The File-based Repository is uploaded to Artifactory after ABE has finished creating it. See below for details on Artifactory settings. _Note: Further build steps still use the local FBR._
* __config.build.artifactory.*__: If you are using Artifactory as a build repository, set these parameter accordingly. _Note: the Artifactory server is configured in Jenkins itself and is referenced by the Jenkinsfile build pipeline via its name_
	* __config.build.artifactory.repository__: The artifactory repository name
	* __config.build.artifactory.path.org__: Since the default implementation uses the standard Ivy notation, provide a dot-separated organization, e.g. "com.softwareag".
	
	
## Assets

Assets mustn't necessarily be stored in a predefined structure, although the layout provided in this example project is recommended, i.e.:

* __assets__: Base folder containing sub folders for the different asset types
	* __BPM__: BPM Business Processe Projects
	* __IS__:  IntegrationServer assets
		* __Packages__: IntegrationServer packages (not zipped!)
			* __config__: Container for configuration assets, copied to this location from _${SAG_HOME}/IntegrationServer/instances/${INSTANCE}/config_. Note: this folder can be empty, but it must exists!
		* __Tests__: Contains IS Test packages, see below [WmTestSuite Tests](#wmtestsuite-tests) section for details
	* __MWS__: MWS assets like Open CAF projects, CAF Process Tasks etc.
	
The asset folder structure is injected into the Asset Build Environment. The paths to the assets are substituted in the Asset Build Environment's build.properties file into the property "build.source.dir". Please see the file [master_build_Reference/build.properties](./master_build_Reference/build.properties).

If you want to add other assets, like Trading Networks, Business Rules, Universal Messaging assets etc., create such a named folder and put your assets into it. Then create a property in the project.properties file which points to the respective asset folder. Now add the newly created property to master_build_Reference/build.properties](./master_build_Reference/build.properties) file, which is used by the ABE at runtime.

## Continuous Integration Pipelines

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

## Splitting Deployments

By using the configuration parameter "**config.deployer.splitDelpoymentSets**", you can control whether a deployment project should have only one deployment set (and thus only one deployment candidate), which contains all deployment assets to be deployed in one go, or if you want to have multiple deployment sets, one for each target IntegrationServer and one containing all other target nodes (i.e. non-IS nodes).

* __config.deployer.splitDelpoymentSets=false__: A single DeploymentSet is created, which contains all assets. For this single DeploymentSet a single DeploymentMap is created, which points to all target server specified in the environment definition. And finally, a single DeploymentCandidate is created which references the DeploymentMap, so that the deployment of all assets is done in one go.
* __config.deployer.splitDelpoymentSets=true__: For each IntegrationServer target node specified in the environment definition a separate DeploymentSet, and thus DeploymentMap as well as DeploymentCandiate is created. For all other target nodes (MWS, BPM, TN etc.) a single DelpoymentSet (and DeploymentMap and DeploymentCandidate) is created. Therefore, Deployer does as many deployments as DeploymentSets have been created. Note: variable substitution creation and import is done for each DeploymentMap created.   

## WmTestSuite Tests

WmTestSuite Tests are referenced by convention, and therefore must be placed in the following folder structure:

* __assets__
	* __IS__
		* __Tests__
			* __${TESET_PACKAGE_NAME}__: Create an IS package for tests, test stubs, supporting services etc. Name this package the same as the package for which these tests are, suffixed with "Test". E.g., tests for the example IS package "Fibonachi" should be placed in a test IS package named "FibonachiTest".
				* __resources__
					* __reports__: Auto generated at test execution time, the Unit test results are placed here
					* __test__: Folder containing all test assets, i.e. not only WmTestSuite tests, but also JMeter tests, SoapUI tests etc. Each test type is placed in its own sub-folder
						* __xml__: Sub-folder for the WmTestSuite tests.
							* __data__: Input and output test pipelines.
							* __setup__: WmTestSuite test files. 
							
Tests are executed by invoking the "test" target in the build.xml. Test results are published to Jenkins and are available for analysis in the Job overview.


## Variable Substitution

In order to ease variable substitution, so called "Variable Substitution Template" files can be stored in a "Variable Substitution Repository". 
			
_**Note**: currently only IntegrationServer package variable substitutions are supported!_

### Variable Substitution Repository structure 

The VarSub Repository is stored for each project and has the following structure:

* __resources/vs__: Base directory for the VarSub Repository (specified by property "config.deployer.varsubDir")
	* __${TARGET_ENV}__: For each target environment (e.g. DEV, TEST, QA, PROD) a separate folder must exist which will contain varsub templates for the given target environment.
		* __${ASSET_TYPE}__: For each asset type (e.g. "is", "mws", "bpm") a separate folder must exist which will contain varsub templates of the given asset type
			* __${COMPOSITE_NAME}.vs.xml__: For each composite for which a variable substitution exists a separate file must exist with the name of the composiste and the file extension "vs.xml".

### Variable Substitution Templates

Variable Substitution Templates are Composite and Target Environment specific XML files.

* "_Composite specific_" means that each composite has its own template which describes the variable substitutions for this composite only
* "_Target Environemnt specific_" means that varsub templates must exist for all possible target environments and must differ only in target environment specific values

Varsub templates therefore are a "stripped down" version of a "real" variable substitution file (as created by Deployer), i.e. they contain only variable substitution values for one specific composite and one specific target environment.

A varsub template has the following setup:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- root node -->
<VarSub>
	<!-- variable substitution values -->
	<Property propertyName="syncDocTypesToBroker" propertyValue="true" />
	....
</VarSub>

``` 

Note:

* The root node is named "VarSub". This differs from a standard Deployer varsub file, whose root node is "Root"
* There are no "DeploementSet" child nodes, since each varsub template only contains the variable substitution values of a single composite, which in a regular Deployer varsub file are represented by a "DeploementSet" node.
* The variable substitution values are exactly the same as in a regular Deployer varsub file. Note: When values are omitted, then Deployer uses the values provided by the source asset. 

### Examples

Taking a standard Deployer varsub file like the following, which represents a deployment set containing one IS package named "SimpleTestPackage", which contains a file polling port and a http port:

```xml
<?xml version="2.0" encoding="UTF-8"?><Root>
  <DeploementSet assetCompositeName="SimpleTestPackage" deploymentSetName="DeploymentSet" serverAliasName="gitHubPipeline_1.0.0.37_fbrRepo" targetServerName="is_node1" targetServerType="IS">
    <Property propertyName="activatePkgOnInstall" propertyValue="true"/>
    <Property propertyName="archivePkgOnInstall" propertyValue="true"/>
    <Property propertyName="compilePackage" propertyValue="true"/>
    <Property propertyName="fragPackage" propertyValue="true"/>
    <Property propertyName="clearACLs" propertyValue="false"/>
    <Property propertyName="disallowActivePackageInstall" propertyValue="false"/>
    <Property propertyName="packageExecutionCheck" propertyValue="0"/>
    <Property propertyName="suspendTriggersDuringDeploy" propertyValue="false"/>
    <Property propertyName="syncDocTypesToBroker" propertyValue="true"/>
  </DeploementSet>
  <DeploementSet assetCompositeName="isconfiguration" deploymentSetName="DeploymentSet" serverAliasName="gitHubPipeline_1.0.0.37_fbrRepo" targetServerName="is_node1" targetServerType="IS">
    <Property propertyName="suspendTasksDuringDeploy" propertyValue="none"/>
    <Property propertyName="activateTasksAfterDeploy" propertyValue="none"/>
    <Property propertyName="enablePortsAfterDeploy" propertyValue="false"/>
    <Property propertyName="reloadCacheManagersAfterDeployment" propertyValue="none"/>
    <Component name="Port.SimpleTestPackage.FilePollingListener.c.\tmp\citrus\csv\import" type="isport">
      <Property propertyName="pkg" propertyValue="SimpleTestPackage"/>
      <Property propertyName="enable" propertyValue="false"/>
      <Property propertyName="hostAccessMode" propertyValue="global"/>
      <Property propertyName="hostList" propertyValue=""/>
      <Property propertyName="monitorDir" propertyValue="/my/monitoring/dir/import"/>
      <Property propertyName="workDir" propertyValue=""/>
      <Property propertyName="completionDir" propertyValue=""/>
      <Property propertyName="errorDir" propertyValue=""/>
      <Property propertyName="clusterEnabled" propertyValue="no"/>
      <Property propertyName="runUser" propertyValue="Administrator"/>
      <Property propertyName="NFSDirectories" propertyValue="no"/>
    </Component>
    <Component name="Port.SimpleTestPackage.Regular.5111" type="isport">
      <Property propertyName="port" propertyValue="5114"/>
      <Property propertyName="bindAddress" propertyValue=""/>
      <Property propertyName="pkg" propertyValue="SimpleTestPackage"/>
      <Property propertyName="enable" propertyValue="false"/>
      <Property propertyName="hostAccessMode" propertyValue="global"/>
      <Property propertyName="hostList" propertyValue=""/>
    </Component>
  </DeploementSet>
</Root>

```

Creating a varsub repository for the target environment DEV out of this varsub file will result in two files:

"resources/vs/DEV/is/SimpleTestPackage.vs.xml":

```xml
<?xml version="2.0" encoding="UTF-8"?>
<VarSub>
    <Property propertyName="activatePkgOnInstall" propertyValue="true"/>
    <Property propertyName="archivePkgOnInstall" propertyValue="true"/>
    <Property propertyName="compilePackage" propertyValue="true"/>
    <Property propertyName="fragPackage" propertyValue="true"/>
    <Property propertyName="clearACLs" propertyValue="false"/>
    <Property propertyName="disallowActivePackageInstall" propertyValue="false"/>
    <Property propertyName="packageExecutionCheck" propertyValue="0"/>
    <Property propertyName="suspendTriggersDuringDeploy" propertyValue="false"/>
    <Property propertyName="syncDocTypesToBroker" propertyValue="true"/>
</VarSub>

```

"resources/vs/DEV/is/isconfiguration.vs.xml":

```xml
<?xml version="2.0" encoding="UTF-8"?>
<VarSub>
 	<Property propertyName="suspendTasksDuringDeploy" propertyValue="none"/>
    <Property propertyName="activateTasksAfterDeploy" propertyValue="none"/>
    <Property propertyName="enablePortsAfterDeploy" propertyValue="false"/>
    <Property propertyName="reloadCacheManagersAfterDeployment" propertyValue="none"/>
    <Component name="Port.SimpleTestPackage.FilePollingListener.c.\tmp\citrus\csv\import" type="isport">
      <Property propertyName="pkg" propertyValue="SimpleTestPackage"/>
      <Property propertyName="enable" propertyValue="false"/>
      <Property propertyName="hostAccessMode" propertyValue="global"/>
      <Property propertyName="hostList" propertyValue=""/>
      <Property propertyName="monitorDir" propertyValue="/my/monitoring/dir/import"/>
      <Property propertyName="workDir" propertyValue=""/>
      <Property propertyName="completionDir" propertyValue=""/>
      <Property propertyName="errorDir" propertyValue=""/>
      <Property propertyName="clusterEnabled" propertyValue="no"/>
      <Property propertyName="runUser" propertyValue="Administrator"/>
      <Property propertyName="NFSDirectories" propertyValue="no"/>
    </Component>
    <Component name="Port.SimpleTestPackage.Regular.5111" type="isport">
      <Property propertyName="port" propertyValue="5114"/>
      <Property propertyName="bindAddress" propertyValue=""/>
      <Property propertyName="pkg" propertyValue="SimpleTestPackage"/>
      <Property propertyName="enable" propertyValue="false"/>
      <Property propertyName="hostAccessMode" propertyValue="global"/>
      <Property propertyName="hostList" propertyValue=""/>
    </Component>>
</VarSub>

```

These two varsub files can now be copied to other target environment specific folders (e.g. "resources/vs/TEST") and adjusted accordingly (e.g. by changing the http port number for TEST from "5114" to "5115").

In order to create such varsub templates, you can either export a variable substitution file from Deployer and splitting it manually as explained above, or you can use the utility ANT script "buildDeployer_Varsub.xml" provided in the project "[https://github.com/SoftwareAG/sagdevops-ci-assets](https://github.com/SoftwareAG/sagdevops-ci-assets)".

