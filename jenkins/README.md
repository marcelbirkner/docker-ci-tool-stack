## Jenkins Docker Container

Usage:
```
$ docker build -t jenkins .
$ docker run -d -p=8080:8080 jenkins
```

Once Jenkins is up and running go to http://192.168.59.103:8080

## Update Plugins

Install and update all plugins via the Jenkins Plugin Manager.
* http://<jenkins-url:port>/pluginManager/

After that use the Script Console to output all plugins including the version in the correct format for the **plugin.txt**.
* http://<jenkins-url:port>/script

```shell
def plugins = jenkins.model.Jenkins.instance.pluginManager.plugins
plugins.sort{it}
plugins.each {
  println it.shortName + ':' + it.getVersion()
}
```

More example scripts can be found in the **groovy** folder.

### Links

- Job DSL API https://jenkinsci.github.io/job-dsl-plugin/
