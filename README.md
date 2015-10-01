# CI Tools Demo

This repo contains Dockerfiles for running a set of Continuous Integration Tools with a single command.

## Prerequisites (Mac)

You should have Docker Toolbox installed, see https://www.docker.com/toolbox

I am using docker-compose to start several docker container at once.
Since all containers run in a single VM (virtualbox), this VM needs enough memory.

### Step 1 - Stop your docker VM

```
docker-machine stop default
```

### Step 2 - Increase Memory via VirtualBox UI

I am using 6000MB for my VM.

![VirtualBox](screenshots/virtualbox.png)

### Step 3 - Start VM

```
docker-machine start default
```

## Getting started

To get all docker containers up and running use:

```
docker-compose up
```

To access Jenkins, go to:

- http://${docker-machine ip default}:8080/jenkins/

To access the SonarQube dashboard, go to:

- http://${docker-machine ip default}:9000/

To access the Nexus artifact Repository, go to:

- http://${docker-machine ip default}:8081/nexus

To access GitLab, go to:

- http://${docker-machine ip default}:10080/

## Screenshots

Here is an overview of all tools:

- GitLab is used for storing the Sourcecode
- Jenkins contains build job and is triggered once projects in GitLab are updated
- As part of the CI build, Jenkins triggers a static code analysis and the results are stored in SonarQube
- The Maven build uses Nexus as a Proxy Repository for all 3rd party libs. The build artefacts are deployed to the Nexus Release Repository

![Docker CI Tools](screenshots/docker-ci-tools.png)

### Jenkins Jobs

![Conference App Jobs](screenshots/jenkins-jobs-1.png)

![Conference App CI Job](screenshots/jenkins-jobs-2-conference-app-ci.png)

### SonarQube Dashboard

![Jenkins Jobs](screenshots/sonar-analysis-conference-app.png)

### Nexus Repository

![Nexus Proxy Repository](screenshots/nexus.png)

### Credentials

- Jenkins (no login required)
- SonarQube (admin/admin)
- Nexus (admin/admin123)
- Gitlab (root/5iveL!fe)

## Jenkins Build Jobs

There are several jobs preconfigured in Jenkins.
The Jobs cover the following tasks:

- Continuous Integration Build with Maven
- Unit Tests
- Static Source Analysis results are stored in SonarQube
- JaCoCo Test Coverage
- Deployment to Nexus
- Jenkins Job DSL examples
