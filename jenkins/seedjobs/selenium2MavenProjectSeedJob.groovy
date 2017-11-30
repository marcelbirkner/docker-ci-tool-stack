def giturl = 'https://github.com/marcelbirkner/selenium2-maven-project'
job("selenium2-maven-project") {
  scm {
    git {
      remote {
        url(giturl)
      }
      extensions {
        cleanAfterCheckout()
      }
    }
  }
  triggers {
    scm('30/H * * * *')
    githubPush()
  }
  steps {
    maven {
        goals('clean test -Dgrid.server.url=http://seleniumhub:4444/wd/hub')
        mavenInstallation('Maven 3.3.3')
        mavenOpts('-Xms512m -Xmx1024m')
        providedGlobalSettings('bc30ebe0-68e1-4fa7-ab30-38092113a63c')
    }
  }
  publishers {
    chucknorris()
    archiveJunit('**/target/surefire-reports/*.xml')
  }
}
listView('Selenium') {
    description('')
    filterBuildQueue()
    filterExecutors()
    jobs {
        regex(/selenium.*/)
    }
    columns {
        status()
        buildButton()
        weather()
        name()
        lastSuccess()
        lastFailure()
        lastDuration()
    }
}
