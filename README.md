# webmethods-sample-project-layout
Sample project layout for webMethods assets. This sample also demonstrates CI quick set up together with https://github.com/SoftwareAG/sagdevops-ci-assets

## Jump Start with webMethods Structure
The best way to start your webMethods project would be to fork this repo directly in github. This will allow you to directly have a set-up copy of layout that will be completely under your control.

## Description

This sample webMethods project layout should serve as a template for organizing webMethods projects. It contains demo Integration Server packages with flow services and wM Unit Tests that are covering those.
Fork the repository to easily create a basis for you webMethods project.

## CI (continuous integration)

Jenkinsfiles.win and Jenkinfiles.unix in the root of the project contain Jenkins Pipeline declaration. With these and our [DevOps asset library for version 9.x and 10.0](https://github.com/SoftwareAG/sagdevops-ci-assets) you will be able to set up you CI in a matter of minutes.

## Environments

The file ENV.groovy defines all you environments, e.g. DEV, TEST, QA, PROD. Adjust this file to your infrastructure, i.e. change hosts and ports, adjust the Administrator user and password.

Note that the Jenkinsfiles reference the environments defined in the EVN.groovy by using the parameter "bda.targetEnv". 

