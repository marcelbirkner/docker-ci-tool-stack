def giturl = 'https://github.com/codecentric/conference-app'
job("conference-app") {
  scm {
    git {
      remote {
        url(giturl)
      }
      createTag(false)
      clean()
    }
  }
  triggers {
    scm('30/H * * * *')
    githubPush()
  }
  steps {
    maven {
      goals('clean install')
      mavenInstallation('Maven 3.3.3')
      rootPOM('monitoring/pom.xml')
    }
    maven {
      goals('clean install')
      mavenInstallation('Maven 3.3.3')
      rootPOM('app/pom.xml')
    }
  }
  publishers {
    archiveJunit('**/target/surefire-reports/*.xml')
  }
}
listView('Conference App') {
    description('')
    filterBuildQueue()
    filterExecutors()
    jobs {
        regex(/conference-app.*/)
    }
    columns {
        status()
        weather()
        name()
        lastSuccess()
        lastFailure()
        lastDuration()
        buildButton()
    }
}
