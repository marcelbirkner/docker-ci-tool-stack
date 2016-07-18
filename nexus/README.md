## Nexus Docker Container

The Dockerfile builds and starts a Nexus repository and configures the JBoss Enterprise maven repos on Nexus.

### Usage - Pull Image from Docker Hub

```
docker pull mbirkner/docker-ci-tool-stack-nexus
docker run -d -p 8081:8081 nexus
```

### Usage - Build manually

```
docker build -t nexus .
docker run -d -p 8081:8081 nexus
```
