# CI Tools Demo

This GitHub repository contains Dockerfiles for running a set of Continuous Integration Tools with a single command. The diagram contains all tools used in the Docker containers.

![Docker CI Tools](screenshots/docker-ci-tools.png)

Blog article on the CI Docker Container, https://blog.codecentric.de/en/2015/10/continuous-integration-platform-using-docker-container-jenkins-sonarqube-nexus-gitlab

## Prerequisites (Mac)

# With Docker Toolbox (incl. VirtualBox)

You should have Docker Toolbox installed, see https://www.docker.com/toolbox

I am using docker-compose to start several docker container at once.
Since all containers run in a single VM (virtualbox), this VM needs enough memory.

### Step 0 - Check Docker Machine version

Ensure that you are using version 0.3.0 or greater of `docker-machine`.

```
# docker-machine version
docker-machine version 0.8.2, build e18a919
```

### Step 1 - Start Docker Machine

Start the machine, using the `--virtualbox-memory` option to increase it’s memory.
I use 6000 MB to accommodate all the docker images.

```
# docker-machine create -d virtualbox --virtualbox-memory "6000" default
Running pre-create checks...
Creating machine...
(default) Creating VirtualBox VM...
(default) Creating SSH key...
(default) Starting VM...
Waiting for machine to be running, this may take a few minutes...
Machine is running, waiting for SSH to be available...
Detecting operating system of created instance...
Detecting the provisioner...
Provisioning with boot2docker...
Copying certs to the local machine directory...
Copying certs to the remote machine...
Setting Docker configuration on the remote daemon...
Checking connection to Docker...
Docker is up and running!
To see how to connect Docker to this machine, run: docker-machine env default
```

### Step 2 - Set Docker Machine Connection

Configure shell environment to connect to your new Docker instance

```
eval "$(docker-machine env default)"
```

### Step 3 - clone Repository

Clone Repository

```
git clone git@github.com:marcelbirkner/docker-ci-tool-stack.git
cd docker-ci-tool-stack
```


# With Docker Mac Native

If you want to use new Docker Mac Native implementation without VirtualBox and Docker Toolbox, follow these steps:

### Step 0 - Install Docker Mac Native

Install Docker Mac Native (https://docs.docker.com/docker-for-mac/)[https://docs.docker.com/docker-for-mac/] and if you had Toolbox before, make sure to follow these steps here https://docs.docker.com/docker-for-mac/docker-toolbox/

or install via __brew update__ & __brew cask install docker --force__ .

If everything went fine, docker --version should give something like this (or a higher version number):

```
$ docker --version
Docker version 1.12.0, build 8eab29e

$ docker-compose --version
docker-compose version 1.8.0, build f3628c7
```

If there´s also docker-machine on your machine, don´t forget to do the mentioned steps [here](https://docs.docker.com/docker-for-mac/docker-toolbox/) to remove it.

### Step 1 - Create needed osxfs mountpoints

Create folders (see [Issue 26](https://github.com/marcelbirkner/docker-ci-tool-stack/issues/26))

* /opt/jenkins via __sudo mkdir /opt/jenkins__
* /opt/postgres via __sudo mkdir /opt/postgres__
* /opt/gitlab via __sudo mkdir /opt/gitlab__

Change the owning user to your account, e.g. via __sudo chown yourUserName gitlab/__

Then configure these folders in Docker / Preferences / File Sharing:

![docker_preferences_file_sharing.png](screenshots/docker_preferences_file_sharing.png)

### Step 2 - Configure correct path to docker binary

Clone Repository

```
# Clone Repository and startup all docker container
# Option A: clone via https
git clone https://github.com/marcelbirkner/docker-ci-tool-stack.git

# Option B: if you have your ssh keys configured for your GitHub account
git clone git@github.com:marcelbirkner/docker-ci-tool-stack.git

cd docker-ci-tool-stack
```

# Getting started

To get all docker containers up and running, in __docker-ci-tool-stack__ use:

```
docker-compose up
```

## UPDATE for Linux Users

### Automatically

Please run **docker-compose.yml.native-docker-fix.sh** before executing **docker-compose up**.
This will fix the Docker binary PATH, see [Issue 24](https://github.com/marcelbirkner/docker-ci-tool-stack/issues/24).
This script patches the docker-compose.yml.

### Manually

Please alter Line 23 in docker-compose.yml under jenkins / volumes from

- /usr/local/bin/docker:/usr/bin/docker

to

- /usr/bin/docker:/usr/bin/docker

This is because '/usr/bin/docker' is the docker binary, see [Issue 24](https://github.com/marcelbirkner/docker-ci-tool-stack/issues/24).

## Access Tools

#### With docker machine

| *Tool* | *Link* | *Credentials* |
| ------------- | ------------- | ------------- |
| Jenkins | http://${docker-machine ip default}:18080/ | no login required |
| SonarQube | http://${docker-machine ip default}:19000/ | admin/admin |
| Nexus | http://${docker-machine ip default}:18081/nexus | admin/admin123 |
| GitLab | http://${docker-machine ip default}/ | root/5iveL!fe |
| Selenium Grid | http://${docker-machine ip default}:4444/grid/console | no login required |
| Conference App | http://${docker-machine ip default}:48080/currentSessions | no login required |

#### With Docker Mac Native

| *Tool* | *Link* | *Credentials* |
| ------------- | ------------- | ------------- |
| Jenkins | http://localhost:18080/ | no login required |
| SonarQube | http://localhost:19000/ | admin/admin |
| Nexus | http://localhost:18081/nexus | admin/admin123 |
| GitLab | http://localhost | root/5iveL!fe |
| Selenium Grid | http://localhost:4444/grid/console | no login required |
| Conference App | http://localhost:48080/currentSessions | no login required |

## Screenshots

Here is an overview of all tools:

- GitLab is used for storing the Source Code
- Jenkins contains build job and is triggered once projects in GitLab are updated
- As part of the CI build, Jenkins triggers a static code analysis and the results are stored in SonarQube
- The Maven build uses Nexus as a Proxy Repository for all 3rd party libs. The build artifacts are deployed to the Nexus Release Repository
- The Selenium Grid contains Docker containers running Chrome and Firefox and is used for UI tests

### Jenkins Jobs

There are several jobs preconfigured in Jenkins.
The Jobs cover the following tasks:

- Continuous Integration Build with Maven
- Unit Tests
- Static Source Analysis results are stored in SonarQube
- JaCoCo Test Coverage
- Deployment to Nexus
- Jenkins Job DSL examples
- Selenium UI Test

![Conference App Jobs](screenshots/jenkins-jobs-1.png)

![Conference App CI Job](screenshots/jenkins-jobs-2-conference-app-ci.png)

### SonarQube Dashboard

![Jenkins Jobs](screenshots/sonar-analysis-conference-app.png)

### Nexus Repository

![Nexus Proxy Repository](screenshots/nexus.png)

### Selenium Grid

![Selenium Grid](screenshots/selenium-grid.png)

## Testing Upgrades

In order to test new versions, I prefer starting out with a blank VirtualBox image.
That eliminates any side effects. Afterwards you can throw away the image.

```
# Create new image
docker-machine create --driver virtualbox --virtualbox-memory 6000 docker-ci-v1

# Configure shell environment
eval $(docker-machine env docker-ci-v1)
```
