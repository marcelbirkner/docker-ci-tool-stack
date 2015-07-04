## Nexus Docker Container

The Dockerfile builds and starts a Nexus repository and configures the JBoss Enterprise maven repos on Nexus.

Usage:
```
$ docker build -t nexus .
$ docker run -d -p 9000:8081 nexus
```
