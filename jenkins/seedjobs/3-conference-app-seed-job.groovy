def giturl = 'https://github.com/codecentric/conference-app'
job("conferenceapp-seed-1-ci") {
  parameters {
    stringParam("BRANCH", "master", "Define TAG or BRANCH to build from")
  }
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
        goals('clean versions:set -DnewVersion=DEV-\${BUILD_NUMBER}')
        mavenInstallation('Maven 3.3.3')
        rootPOM('app/pom.xml')
        mavenOpts('-Xms512m -Xmx1024m')
        providedGlobalSettings('MyGlobalSettings')
    }
    maven {
      goals('clean install')
      mavenInstallation('Maven 3.3.3')
      rootPOM('app/pom.xml')
      mavenOpts('-Xms512m -Xmx1024m')
      providedGlobalSettings('MyGlobalSettings')
    }
    maven {
      goals('clean install')
      mavenInstallation('Maven 3.3.3')
      rootPOM('app/pom.xml')
      mavenOpts('-Xms512m -Xmx1024m')
      providedGlobalSettings('MyGlobalSettings')
    }
  }
  publishers {
    chucknorris()
    archiveJunit('**/target/surefire-reports/*.xml')
    publishCloneWorkspace('**', '', 'Any', 'TAR', true, null)
    downstreamParameterized {
      trigger('conferenceapp-seed-2-sonar') {
        currentBuild()
      }
    }
  }
}
job("conferenceapp-seed-2-sonar") {
  parameters {
    stringParam("BRANCH", "master", "Define TAG or BRANCH to build from")
  }
  scm {
    cloneWorkspace("conferenceapp-seed-1-ci")
  }
  steps {
    maven {
        goals('clean versions:set -DnewVersion=DEV-\${BUILD_NUMBER}')
        mavenInstallation('Maven 3.3.3')
        rootPOM('app/pom.xml')
        mavenOpts('-Xms512m -Xmx1024m')
        providedGlobalSettings('MyGlobalSettings')
    }
    maven {
      goals('org.jacoco:jacoco-maven-plugin:0.7.4.201502262128:prepare-agent install -Psonar')
      mavenInstallation('Maven 3.3.3')
      rootPOM('app/pom.xml')
      mavenOpts('-Xms512m -Xmx1024m')
      providedGlobalSettings('MyGlobalSettings')
    }
    maven {
      goals('sonar:sonar -Psonar')
      mavenInstallation('Maven 3.3.3')
      rootPOM('app/pom.xml')
      mavenOpts('-Xms512m -Xmx1024m')
      providedGlobalSettings('MyGlobalSettings')
    }
  }
  publishers {
    chucknorris()
  }
}
job("conferenceapp-monitoring-seed-1-ci") {
  parameters {
    stringParam("BRANCH", "master", "Define TAG or BRANCH to build from")
  }
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
        goals('clean versions:set -DnewVersion=DEV-\${BUILD_NUMBER}')
        mavenInstallation('Maven 3.3.3')
        rootPOM('monitoring/pom.xml')
        mavenOpts('-Xms512m -Xmx1024m')
        providedGlobalSettings('MyGlobalSettings')
    }
    maven {
      goals('clean install')
      mavenInstallation('Maven 3.3.3')
      rootPOM('monitoring/pom.xml')
      mavenOpts('-Xms512m -Xmx1024m')
      providedGlobalSettings('MyGlobalSettings')
    }
    maven {
      goals('clean install')
      mavenInstallation('Maven 3.3.3')
      rootPOM('monitoring/pom.xml')
      mavenOpts('-Xms512m -Xmx1024m')
      providedGlobalSettings('MyGlobalSettings')
    }
  }
  publishers {
    chucknorris()
    publishCloneWorkspace('**', '', 'Any', 'TAR', true, null)
    downstreamParameterized {
      trigger('conferenceapp-monitoring-seed-2-sonar') {
        currentBuild()
      }
    }
  }
}
job("conferenceapp-monitoring-seed-2-sonar") {
  parameters {
    stringParam("BRANCH", "master", "Define TAG or BRANCH to build from")
  }
  scm {
    cloneWorkspace("conferenceapp-monitoring-seed-1-ci")
  }
  steps {
    maven {
        goals('clean versions:set -DnewVersion=DEV-\${BUILD_NUMBER}')
        mavenInstallation('Maven 3.3.3')
        rootPOM('monitoring/pom.xml')
        mavenOpts('-Xms512m -Xmx1024m')
        providedGlobalSettings('MyGlobalSettings')
    }
    maven {
      goals('org.jacoco:jacoco-maven-plugin:0.7.4.201502262128:prepare-agent install -Psonar')
      mavenInstallation('Maven 3.3.3')
      rootPOM('monitoring/pom.xml')
      mavenOpts('-Xms512m -Xmx1024m')
      providedGlobalSettings('MyGlobalSettings')
    }
    maven {
      goals('sonar:sonar -Psonar')
      mavenInstallation('Maven 3.3.3')
      rootPOM('monitoring/pom.xml')
      mavenOpts('-Xms512m -Xmx1024m')
      providedGlobalSettings('MyGlobalSettings')
    }
  }
  publishers {
    chucknorris()
  }
}
listView('ConferenceApp Seed') {
    description('')
    filterBuildQueue()
    filterExecutors()
    jobs {
        regex(/conferenceapp-.*/)
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
listView('Conference App') {
    description('')
    filterBuildQueue()
    filterExecutors()
    jobs {
        regex(/conference-app-.*/)
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
