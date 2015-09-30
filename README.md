# CI Tools Demo

This repo contains Dockerfiles for running a set of Continuous Integration Tools with a single command.
To get all docker containers up and running use:

```
docker-compose up
```

To access Jenkins, go to:

- http://${boot2docker ip}:8080/jenkins/

To access the SonarQube dashboard, go to:

- http://${boot2docker ip}:9000/

To access the Nexus artifact Repository, go to:

- http://${boot2docker ip}:8081/nexus

To access GitLab, go to:

- http://${boot2docker ip}:10080/

## Installed Software

- Jenkins (no login required)
- SonarQube (admin/admin)
- Nexus (admin/admin123)
- Gitlab (root/5iveL!fe)

### Jenkins Build Jobs

- Build & Deploy to Nexus

## Additional ToDo

- Sonar analysis with JaCoCo
- Test Coverage
