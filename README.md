# file-converter-lambda
## Overview
This is a Java project using the Gradle build system. It contains Handler functions that are able to be run via AWS Lambda, and also includes a CloudFormation template to setup any necessary infrastructure.

This is part of a larger project detailed here:  [Omni File Converter](https://github.com/kenmhayes/omni-file-converter)

## Functionality

### LambdaHandler

Takes as input a S3 PutObject event notification. Will download the file from S3 and then attempt to convert it into the requested file format, putting a converted version in the S3 bucket if successful.

## Build

To package for Lambda as a ZIP archive, run the command "gradle -q buildZip". You should expect to see the ZIP in the 'build/distributions' folder.

## Deploy

A pre-requisite is a S3 bucket to hold the code artifacts (ZIP archives). This can be named in any way, so long as you enter the correct name when creating the CloudFormation stack. The ZIP should be uploaded prior to running the CloudFormation template.

This project is also dependent on the infrastructure created as part of this repository:  [Conversion Session Lambda](https://github.com/kenmhayes/conversion-session-lambda)

Follow the deployment instructions linked above and obtain the ID of the Rest API created as part of this process.

Then, deploy the stack for this project using the _cloudformationtemplate.yml_, passing in the name of the code artifacts S3 bucket and the REST API Id. Everything else can be left as their defaults.

## Frameworks

The following libraries and frameworks are used in this project:

- Gradle
- Dagger 2
- Guava
- AWS SDK

- JUnit
- Mockito
